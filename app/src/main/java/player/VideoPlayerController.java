package player;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import utils.VideoUtil;

/**
 * Created by Tian on 2017/11/11.
 * 控制器抽象类
 */

public abstract class VideoPlayerController extends FrameLayout implements View.OnTouchListener {
    private Context mContext;
    protected VideoPlayer videoPlayer;

    private Timer mUpdateProgressTimer;
    private TimerTask mUpdateProgressTimerTask;

    private float mDownX;
    private float mDownY;
    private boolean needChangePosition; //改变位置
    private boolean needChangeVolume; //改变声音
    private boolean needChangeBrightness; //改变亮度

    private static final int THRESHOLD = 80; //临界点
    private long mGestureDownPosition;  //手势按下的位置
    private float mGestureDownBrightness;//手势按下的亮度
    private int mGestureDownVolume; //手势按下的声音
    private long mNewPosition;  //改变后的位置

    public VideoPlayerController(Context context) {
        super(context);
        this.mContext = context;
        this.setOnTouchListener(this);
    }

    public void setVideoPlayer(VideoPlayer videoPlayer) {
        this.videoPlayer = videoPlayer;
    }

    /*设置播放的视频的标题*/
    public abstract void setTitle(String title);

    /*视频底图资源*/
    public abstract void setImage(int resId);

    /*视频底图image view控件，提供给外部用图片加载工具来加载网络图片*/
    public abstract ImageView imageView();

    /*设置总时长*/
    public abstract void setLength(long length);

    /*当播放器的播放状态发送改变时*/
    protected abstract void onPlayStateChanged(int playState);

    /*当播放器的模式发生改变时*/
    protected abstract void onPlayModeChanged(int playMode);

    /*重置控制器，将控制器恢复到初始状态*/
    protected abstract void reset();

    /*开启更新进度的计时器*/
    protected void startUpdateProgressTimer() {
        cancelUpdateProgressTimer();//取消更新进度的计时器
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = new Timer();
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = new TimerTask() {
                @Override
                public void run() {
                    VideoPlayerController.this.post(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress();//更新进度
                        }
                    });
                }
            };
        }
        mUpdateProgressTimer.schedule(mUpdateProgressTimerTask, 0, 1000);
    }

    /*更新进度,包括进度条进度，当前位置的播放时长，总时长等*/
    protected abstract void updateProgress();

    /*取消更新进度的计时器*/
    protected void cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel();
            mUpdateProgressTimer = null;
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask.cancel();
            mUpdateProgressTimerTask = null;
        }
    }
    /*触摸事件*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //只有在全屏的时候才能拖动位置、亮度、声音
        if (!videoPlayer.isFullScreen()) {
            return false;
        }
        //只有在播放、暂停、缓冲的时候才能拖动位置、亮度、声音
        if (videoPlayer.isIdle() ||
                videoPlayer.isError() ||
                videoPlayer.isPrepared() ||
                videoPlayer.isPreparing() ||
                videoPlayer.isCompleted()) {
            hideChangePosition();
            hideChangeBrightness();
            hideChangeVolume();
            return false;
        }
        /*拿到当前屏幕的坐标*/
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            /*手指按下*/
            case MotionEvent.ACTION_DOWN:
                mDownX=x;
                mDownY=y;
                needChangeBrightness=false;
                needChangePosition=false;
                needChangeVolume=false;
                break;
            /*手指在屏幕移动*/
            case MotionEvent.ACTION_MOVE:
                float deltaX=x-mDownX;
                float deltaY=y-mDownY;
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);
                if (!needChangePosition&&!needChangeVolume&&!needChangeBrightness){
                    //只有在播放，暂停，缓冲的时候才能拖动改变位置，亮度、声音
                    if (absDeltaX>=THRESHOLD){
                        cancelUpdateProgressTimer();
                        needChangePosition=true;
                        mGestureDownPosition=videoPlayer.getCurrentPosition();
                    }else if (absDeltaY>=THRESHOLD){
                        if (mDownX<getWidth()*0.5f){
                            //左侧改变亮度
                            needChangeBrightness=true;
                            mGestureDownBrightness= VideoUtil.scanForActivity(mContext)
                                    .getWindow().getAttributes().screenBrightness;
                        }else{
                            //右侧改变声音
                            needChangeVolume=true;
                            mGestureDownVolume=videoPlayer.getVolume();
                        }
                    }
                }
                //改变位置
                if (needChangePosition){
                    //视频总时长
                    long duration=videoPlayer.getDuration();
                    long toPosition= (long) (mGestureDownPosition+duration*deltaX/getWidth());
                    mNewPosition=Math.max(0,Math.min(duration,toPosition));
                    int newPositionProgress=(int)(100f*mNewPosition/duration);
                    //改变后的位置，视频时长
                    showChangePosition(duration,newPositionProgress);
                }
                //改变亮度
                if (needChangeBrightness){
                    deltaY=-deltaY;
                    float deltaBrightness = deltaY * 3 / getHeight();
                    float newBrightness = mGestureDownBrightness + deltaBrightness;
                    newBrightness=Math.max(0,Math.min(newBrightness,1));
                    float newBrightnessPercentage = newBrightness;
                    WindowManager.LayoutParams params=VideoUtil.scanForActivity(mContext)
                            .getWindow().getAttributes();
                    params.screenBrightness=newBrightnessPercentage;
                    VideoUtil.scanForActivity(mContext).getWindow().setAttributes(params);
                    int newBrightnessProgress = (int) (100f * newBrightnessPercentage);
                    showChangeBrightness(newBrightnessProgress);
                }
                //改变声音
                if (needChangeVolume){
                    deltaY=-deltaY;
                    int maxVolume=videoPlayer.getMaxVolume();
                    int deltaVolume= (int) (maxVolume * deltaY * 3 / getHeight());
                    int newVolume = mGestureDownVolume + deltaVolume;
                    newVolume = Math.max(0, Math.min(maxVolume, newVolume));
                    //设置音量
                    videoPlayer.setVolume(newVolume);
                    int newVolumeProgress = (int) (100f * newVolume / maxVolume);
                    showChangeVolume(newVolumeProgress);
                }

                break;
            /*手指弹起*/
            case MotionEvent.ACTION_UP:
                if (needChangePosition){
                    videoPlayer.seekTo(mNewPosition);
                    hideChangePosition();
                    startUpdateProgressTimer();
                    return true;
                }
                if (needChangeBrightness){
                    hideChangeBrightness();
                    return true;
                }
                if (needChangeVolume){
                    hideChangeVolume();
                    return true;
                }
                break;
        }
        return false;
    }
   /*在手势滑动ACTION_MOVE的过程中，会不断调用此方法。newVolumeProgress新的音量的进度*/
    protected abstract void showChangeVolume(int newVolumeProgress);

    /*在手势滑动ACTION_MOVE的过程中，会不断调用此方法。newBrightnessProgress新的亮度进度*/
    protected abstract void showChangeBrightness(int newBrightnessProgress);

    /*在手势滑动ACTION_MOVE的过程中，会不断调用此方法，duration视频总时长，newPositionProgress新的位置的进度*/
    protected abstract void showChangePosition(long duration, int newPositionProgress);

    /*在手势ACTION_UP或ACTION_CANCEL时调用，隐藏拖动位置*/
    protected abstract void hideChangePosition();

    /*在手势ACTION_UP或ACTION_CANCEL时调用，隐藏改变的亮度*/
    protected abstract void hideChangeBrightness();

    /*在手势ACTION_UP或ACTION_CANCEL时调用，隐藏改变声音*/
    protected abstract void hideChangeVolume();
}
