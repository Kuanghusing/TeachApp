package player;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import interfaces.IVideoPlayer;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import utils.Constants;
import utils.VideoPlayerManager;
import utils.VideoUtil;

/**
 * Created by Tian on 2017/11/10.
 * 播放器
 */
public class VideoPlayer extends FrameLayout
        implements IVideoPlayer,
        TextureView.SurfaceTextureListener {
    /*播放出错*/
    public static final int STATE_ERROR=-1;
    //播放未开始
    public static final int STATE_IDLE=0;
    //播放准备中
    public static final int STATE_PREPARING=1;
    //播放准备就绪
    public static final int STATE_PREPARED=2;
    //正在播放
    public static final int STATE_PLAYING=3;
    //播放停止
    public static final int STATE_PAUSED=4;
    //正在缓冲
    public static final int STATE_BUFFERING_PLAYING=5;
    //正在缓冲
    public static final int STATE_BUFFERING_PAUSED=6;
    //播放完成
    public static final int STATE_COMPLETED=7;
    /*全屏模式*/
    public static final int MODE_FULL_SCREEN=8;
    /*普通模式*/
    public static final int MODE_NORMAL=9;
    /*IjkPlayer*/
    public static final int TYPE_IJK=10;
    /*MediaPlayer*/
    public static final int TYPE_NATIVE=11;

    private FrameLayout mContainer;  //播放视频的容器
    private Context mContext;
    private int mCurrentState=STATE_IDLE;  //当前播放器的状态
    private int mCurrentMode=MODE_NORMAL;  //当前播放器的模式
    private boolean continueFromLastposition=true;  //是否继续从上次播放的位置继续播放
    private AudioManager mAudioManager;  //音频管理器
    private VideoTextureView mVideoTextureView; //重写TextureView，适配视频的宽高
    private String mUrl; //视频url地址
    private Map<String,String> mHeaders;//视频的头
    private VideoPlayerController mController;  //控制器抽象类
    private int mPlayerType=TYPE_IJK;

    private IMediaPlayer mIMediaPlayer;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private long skipToPosition;
    private int mBufferPercentage;  //缓冲百分百

    public VideoPlayer(Context context) {
        this(context, null);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }
    /*初始化容器*/
    private void init() {
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer, params);
    }
    /*设置视频的url,headers*/
    public void setUp(String url, Map<String, String> headers) {
        mUrl = url;
        mHeaders = headers;

    }
    /*设置控制器*/
    public void setController(VideoPlayerController controller) {
        mContainer.removeView(mController);
        mController = controller;
        mController.reset();
        mController.setVideoPlayer(this);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mController, params);
    }

    /*设置播放器类型*/
    public void setPlayerType(int playerType) {
        mPlayerType = playerType;
    }

    /**
     * 是否从上一次的位置继续播放
     *
     * @param continueFromLastPosition true从上一次的位置继续播放
     */
    @Override
    public void continueFromLastposition(boolean continueFromLastPosition) {
        this.continueFromLastposition = continueFromLastPosition;
    }
    /*设置播放速度*/
    @Override
    public void setSpeed(float speed) {
        if (mIMediaPlayer instanceof IjkMediaPlayer) {
            ((IjkMediaPlayer) mIMediaPlayer).setSpeed(speed);
        } else {
            Log.v(Constants.TAG,"只有IjkPlayer才能设置播放速度");
        }
    }
    /*开始播放*/
    @Override
    public void onStart() {
        if (mCurrentState == STATE_IDLE) {
            VideoPlayerManager.getInstance().setCurrentVideoPlayer(this);
            initAudioManager();//初始化音频管理器
            initMediaPlayer();//初始化音乐播放器
            initTextureView();//初始化TextureView
            addTextureView();//往TextureView添加
        } else {
           Log.v(Constants.TAG,"VideoPlayer只有在mCurrentState == STATE_IDLE时才能调用start方法.");
        }
    }
    /*开始播放*/
    @Override
    public void onStart(long position) {
        skipToPosition = position;
        onStart();
    }
    /*重新播放*/
    @Override
    public void restart() {
        if (mCurrentState == STATE_PAUSED) {
            mIMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
        } else if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mIMediaPlayer.start();
            mCurrentState = STATE_BUFFERING_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
        } else if (mCurrentState == STATE_COMPLETED || mCurrentState == STATE_ERROR) {
            mIMediaPlayer.reset();
            openMediaPlayer();
        } else {
        }
    }
    /*暂停播放*/
    @Override
    public void onPause() {
        if (mCurrentState == STATE_PLAYING) {
            mIMediaPlayer.pause();
            mCurrentState = STATE_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
        }
        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mIMediaPlayer.pause();
            mCurrentState = STATE_BUFFERING_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
        }
    }
    /*跳转到指定位置播放*/
    @Override
    public void seekTo(long pos) {
        if (mIMediaPlayer != null) {
            mIMediaPlayer.seekTo(pos);
        }
    }
    /*设置音量*/
    @Override
    public void setVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }

    @Override
    public boolean isPreparing() {
        return mCurrentState == STATE_PREPARING;
    }

    @Override
    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }

    @Override
    public boolean isBufferingPlaying() {
        return mCurrentState == STATE_BUFFERING_PLAYING;
    }

    @Override
    public boolean isBufferingPaused() {
        return mCurrentState == STATE_BUFFERING_PAUSED;
    }

    @Override
    public boolean isPlaying() {
        return mCurrentState == STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return mCurrentState == STATE_PAUSED;
    }

    @Override
    public boolean isError() {
        return mCurrentState == STATE_ERROR;
    }

    @Override
    public boolean isCompleted() {
        return mCurrentState == STATE_COMPLETED;
    }

    @Override
    public boolean isFullScreen() {
        return mCurrentMode == MODE_FULL_SCREEN;
    }


    @Override
    public boolean isNormal() {
        return mCurrentMode == MODE_NORMAL;
    }
    /*最大音量*/
    @Override
    public int getMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }
    /*当前音量*/
    @Override
    public int getVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }

    @Override
    public long getDuration() {
        return mIMediaPlayer != null ? mIMediaPlayer.getDuration() : 0;
    }

    @Override
    public long getCurrentPosition() {
        return mIMediaPlayer != null ? mIMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public int getBufferPrecentage() {
        return mBufferPercentage;
    }

    @Override
    public float getSpeed(float speed) {
        if (mIMediaPlayer instanceof IjkMediaPlayer) {
            return ((IjkMediaPlayer) mIMediaPlayer).getSpeed(speed);
        }
        return 0;
    }

    @Override
    public long getHttpSpeed() {
        if (mIMediaPlayer instanceof IjkMediaPlayer) {
            return ((IjkMediaPlayer) mIMediaPlayer).getTcpSpeed();
        }
        return 0;
    }
    /*初始化音频管理器*/
    private void initAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }
    /*初始化音乐播放器*/
    private void initMediaPlayer() {
        if (mIMediaPlayer == null) {
            switch (mPlayerType) {
                case TYPE_NATIVE:
                    mIMediaPlayer = new AndroidMediaPlayer();
                    break;
                case TYPE_IJK:
                default:
                    mIMediaPlayer = new IjkMediaPlayer();
                    ((IjkMediaPlayer)mIMediaPlayer).setOption(1, "analyzemaxduration", 100L);
                    ((IjkMediaPlayer)mIMediaPlayer).setOption(1, "probesize", 10240L);
                    ((IjkMediaPlayer)mIMediaPlayer).setOption(1, "flush_packets", 1L);
                    ((IjkMediaPlayer)mIMediaPlayer).setOption(4, "packet-buffering", 0L);
                    ((IjkMediaPlayer)mIMediaPlayer).setOption(4, "framedrop", 1L);
                    break;
            }
            mIMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }
    /*初始化TextureView*/
    private void initTextureView() {
        if (mVideoTextureView == null) {
            mVideoTextureView = new VideoTextureView(mContext);
            mVideoTextureView.setSurfaceTextureListener(this);
        }
    }
    /*添加TextureView*/
    private void addTextureView() {
        mContainer.removeView(mVideoTextureView);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mContainer.addView(mVideoTextureView, 0, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Toast.makeText(mContext, "onSurfaceTextureAvailable", Toast.LENGTH_SHORT).show();
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture;
            openMediaPlayer();
        } else {
            mVideoTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }
    /*打开播放器*/
    private void openMediaPlayer() {
        // 屏幕常亮
        mContainer.setKeepScreenOn(true);
        // 设置监听
        mIMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mIMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mIMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mIMediaPlayer.setOnErrorListener(mOnErrorListener);
        mIMediaPlayer.setOnInfoListener(mOnInfoListener);
        mIMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        // 设置dataSource
        try {
            mIMediaPlayer.setDataSource(mContext.getApplicationContext(), Uri.parse(mUrl), mHeaders);
            if (mSurface == null) {
                mSurface = new Surface(mSurfaceTexture);
            }
            mIMediaPlayer.setSurface(mSurface);
            mIMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            mController.onPlayStateChanged(mCurrentState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return mSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    private IMediaPlayer.OnPreparedListener mOnPreparedListener
            = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            mCurrentState = STATE_PREPARED;
            mController.onPlayStateChanged(mCurrentState);
            mp.start();
            // 从上次的保存位置播放
            if (continueFromLastposition) {
                long savedPlayPosition = VideoUtil.getSavedPlayPosition(mContext, mUrl);
                mp.seekTo(savedPlayPosition);
            }
            // 跳到指定位置播放
            if (skipToPosition != 0) {
                mp.seekTo(skipToPosition);
            }
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener
            = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            mVideoTextureView.adaptVideoSize(width, height);
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener
            = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            mCurrentState = STATE_COMPLETED;
            mController.onPlayStateChanged(mCurrentState);
            // 清除屏幕常亮
            mContainer.setKeepScreenOn(false);
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener
            = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            // 直播流播放时去调用mediaPlayer.getDuration会导致-38和-2147483648错误，忽略该错误
            if (what != -38 && what != -2147483648 && extra != -38 && extra != -2147483648) {
                mCurrentState = STATE_ERROR;
                mController.onPlayStateChanged(mCurrentState);
            }
            return true;
        }
    };

    private IMediaPlayer.OnInfoListener mOnInfoListener
            = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                // 播放器开始渲染
                mCurrentState = STATE_PLAYING;
                mController.onPlayStateChanged(mCurrentState);
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                // MediaPlayer暂时不播放，以缓冲更多的数据
                if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_BUFFERING_PAUSED;
                } else {
                    mCurrentState = STATE_BUFFERING_PLAYING;
                }
                mController.onPlayStateChanged(mCurrentState);
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // 填充缓冲区后，MediaPlayer恢复播放/暂停
                if (mCurrentState == STATE_BUFFERING_PLAYING) {
                    mCurrentState = STATE_PLAYING;
                    mController.onPlayStateChanged(mCurrentState);
                }
                if (mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_PAUSED;
                    mController.onPlayStateChanged(mCurrentState);
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
                // 视频旋转了extra度，需要恢复
                if (mVideoTextureView != null) {
                    mVideoTextureView.setRotation(extra);
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
               Log.v(Constants.TAG,"视频不能seekTo，为直播视频");
            } else {
               Log.d(Constants.TAG,"onInfo ——> what：" + what);
            }
            return true;
        }
    };

    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener
            = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            mBufferPercentage = percent;
        }
    };

    /*进入全屏模式
    * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期*/
    @Override
    public void enterFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) return;

        // 隐藏ActionBar、状态栏，并横屏
        VideoUtil.hideActionBar(mContext);
        VideoUtil.scanForActivity(mContext)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ViewGroup contentView = (ViewGroup) VideoUtil.scanForActivity(mContext)
                .findViewById(android.R.id.content);
        if (mCurrentMode==MODE_FULL_SCREEN){
            contentView.removeView(mContainer);
        }else{
            this.removeView(mContainer);
        }
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(mContainer, params);
        mCurrentMode = MODE_FULL_SCREEN;
        mController.onPlayModeChanged(mCurrentMode);
    }

    /*退出全屏模式
     * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
      * 以避免Activity重新走生命周期.*/
    @Override
    public boolean exitFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            VideoUtil.showActionBar(mContext);
            VideoUtil.scanForActivity(mContext)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ViewGroup contentView = (ViewGroup) VideoUtil.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);
            mCurrentMode = MODE_NORMAL;
            mController.onPlayModeChanged(mCurrentMode);
            return true;
        }
        return false;
    }

    /*释放播放器资源，不恢复最初状态*/
    @Override
    public void releasePlayer() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(null);
            mAudioManager = null;
        }
        if (mIMediaPlayer != null) {
            mIMediaPlayer.release();
            mIMediaPlayer = null;
        }
        mContainer.removeView(mVideoTextureView);
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCurrentState = STATE_IDLE;
    }
    /*释放所有资源，控制器恢复最初状态*/
    @Override
    public void release() {
        // 保存播放位置
        if (isPlaying() || isBufferingPlaying() || isBufferingPaused() || isPaused()) {
            VideoUtil.savePlayPosition(mContext, mUrl, getCurrentPosition());
        } else if (isCompleted()) {
            VideoUtil.savePlayPosition(mContext, mUrl, 0);
        }
        // 退出全屏
        if (isFullScreen()) {
            exitFullScreen();
        }
        mCurrentMode = MODE_NORMAL;
        // 释放播放器
        releasePlayer();
        // 恢复控制器
        if (mController != null) {
            mController.reset();
        }
        Runtime.getRuntime().gc();
    }
}
