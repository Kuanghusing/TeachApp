package interfaces;

import java.util.Map;

/**
 * Created by Tian on 2017/11/10.
 * 自定义一个接口类
 */

public interface IVideoPlayer {
    /*
    设置视频的url,headers
    */
    void setUp(String url, Map<String,String> headers);
    /*
    * 开始播放
    * */
    void onStart();
    /*
    从指定的位置播放
    */
    void onStart(long psition);
    /*
    * 暂停播放
    * */
    void onPause();
    /*
    重新播放
    */
    void restart();
    /*
    seek到指定的位置播放
    */
    void seekTo(long position);
    /*
    从上一次播放的位置继续播放
    */
    void continueFromLastposition(boolean continueFromLastposition);
    /*
    播放器在当前播放的状态
    */
    boolean isIdle(); //播放未开始
    boolean isPreparing(); //正在准备状态
    boolean isPrepared(); //准备好了
    boolean isBufferingPlaying(); //正在缓冲播放
    boolean isBufferingPaused(); //正在缓冲暂停
    boolean isPlaying(); //正在播放
    boolean isPaused(); //暂停
    boolean isError(); //播放出错
    boolean isCompleted(); //播放完成
    /*
    播放器的模式
    */
    boolean isFullScreen();//全屏模式
    boolean isNormal();//普通模式
    /*获取视频总时长*/
    long getDuration();
    /*获取当前播放的位置*/
    long getCurrentPosition();
    /*获取视频缓冲百分比*/
    int getBufferPrecentage();
    /*获取播放速度*/
    float getSpeed(float speed);
    /*获取网络加载速度*/
    long getHttpSpeed();
    /*进入全屏模式*/
    void enterFullScreen();
    /*退出全屏模式*/
    boolean exitFullScreen();
    /*此处只释放播放器，不恢复初始状态*/
    void releasePlayer();
    /*释放资源，控制器恢复到最初始状态*/
    void release();
    /*获取当前音量*/
    int getVolume();
    /*获取最大音量*/
    int getMaxVolume();
    /*设置音量*/
    void setVolume(int volume);
    /**
     * 设置播放速度，目前只有IjkPlayer有效果，原生MediaPlayer暂不支持
     *
     * @param speed 播放速度
     */
    void setSpeed(float speed);

}
