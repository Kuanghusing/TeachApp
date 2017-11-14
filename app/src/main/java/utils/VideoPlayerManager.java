package utils;


import player.VideoPlayer;

/**
 * Created by Tian on 2017/11/11.
 * 视频播放器管理器
 */

public class VideoPlayerManager  {
    private VideoPlayer mVideoPlayer;
    private static VideoPlayerManager instance;

    private VideoPlayerManager(){}

    public static synchronized VideoPlayerManager getInstance(){
        if (instance==null){
            instance=new VideoPlayerManager();
        }
        return instance;
    }
    public VideoPlayer getCurrentVideoPlayer(){
        return mVideoPlayer;
    }
    public void setCurrentVideoPlayer(VideoPlayer videoPlayer){
        if (mVideoPlayer!=videoPlayer){
            releaseVideoPlayer();
            mVideoPlayer=videoPlayer;
        }
    }
    /*释放播放器*/
    public void releaseVideoPlayer() {
        if (mVideoPlayer!=null){
            mVideoPlayer.release();
            mVideoPlayer=null;
        }
    }
    public void suspendVideoPlayer(){
        if (mVideoPlayer!=null&&(mVideoPlayer.isPlaying()||mVideoPlayer.isBufferingPlaying())){
            mVideoPlayer.onPause();
        }
    }
    public void resumeVideoPlayer(){
        if (mVideoPlayer!=null&&(mVideoPlayer.isPaused()||mVideoPlayer.isBufferingPaused())){
            mVideoPlayer.restart();
        }
    }
    public boolean onBackPressed(){
        if (mVideoPlayer!=null){
            if (mVideoPlayer.isFullScreen()){
                return mVideoPlayer.exitFullScreen();
            }
        }
        return false;
    }
}
