package player;

import android.content.Context;
import android.view.TextureView;

/**
 * Created by Tian on 2017/11/10.
 * 自定义一个类继承TextureView，适配视频的宽高和旋转
 */

public class VideoTextureView extends TextureView {
    private int videoWidth;  //播放器的宽度
    private int videoHeigth;  //播放器的高度

    public VideoTextureView(Context context) {
        super(context);
    }
    //自动适应播放器的大小
    public void adaptVideoSize(int videoWidth,int videoHeigth){
        if (this.videoWidth!=videoWidth&&this.videoHeigth!=videoHeigth){
            this.videoWidth=videoWidth;
            this.videoHeigth=videoHeigth;
            //当我们动态移动一个View的位置，或者View的大小、形状发生了变化的时候，我们可以在view中调用这个方法，
            requestLayout();
        }
    }
    /*设置旋转*/
    @Override
    public void setRotation(float rotation) {
        if (rotation!=getRotation()){
            super.setRotation(rotation);
            requestLayout();
        }
    }
    /*测量*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float viewRotation=getRotation();
        // 如果判断成立，则说明显示的TextureView和本身的位置是有90度的旋转的，所以需要交换宽高参数。
        if (viewRotation==90f||viewRotation==270f){
            int tempMesureSpec=heightMeasureSpec;
            widthMeasureSpec=heightMeasureSpec;
            heightMeasureSpec=tempMesureSpec;
        }
        int width=getDefaultSize(videoWidth,widthMeasureSpec);
        int heigth=getDefaultSize(videoHeigth,heightMeasureSpec);
        if (videoWidth>0&&videoHeigth>0){
            int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
            int heigthSpecMode=MeasureSpec.getMode(heightMeasureSpec);
            int heigthSpecSize=MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode==MeasureSpec.EXACTLY&&heigthSpecMode==MeasureSpec.EXACTLY){
                //宽度、高度都是精确模式，精确模式MeasureSpec.EXACTLY
                width=widthSpecSize;
                heigth=heigthSpecSize;

                if (videoWidth*heigth<width*videoHeigth){
                    width=heigth*videoWidth/videoHeigth;
                }else if (videoWidth*heigth>width*videoHeigth){
                    heigth=width*videoHeigth/videoWidth;
                }
            }else if (widthSpecMode==MeasureSpec.EXACTLY){
                //只有宽度是精确模式
                width=widthSpecSize;
                heigth=width*videoHeigth/videoWidth;
                //如果高度处于MeasureSpec.AT_MOST最大模式
                if (heigthSpecMode==MeasureSpec.AT_MOST&&heigth>heigthSpecSize){
                    heigth=heigthSpecSize;
                    width=heigth*videoWidth/videoHeigth;
                }
            }else if (heigthSpecMode==MeasureSpec.EXACTLY){
                //只有高度是精确模式
                heigth=heigthSpecSize;
                width=heigth*videoWidth/videoHeigth;
                //如果宽度处于最大模式MeasureSpec.AT_MOST
                if (widthSpecMode==MeasureSpec.AT_MOST&&width>widthSpecSize){
                    width=widthSpecSize;
                    heigth=width*videoHeigth/videoWidth;
                }
            }else{
                //宽度和高度都处于未指定模式
                width=videoWidth;
                heigth=videoHeigth;
                if (heigthSpecMode==MeasureSpec.AT_MOST&&heigth>heigthSpecSize){
                    heigth=heigthSpecSize;
                    width=heigth*videoWidth/videoHeigth;
                }
                if (widthSpecMode==MeasureSpec.AT_MOST&&width>widthSpecSize){
                    width=widthSpecSize;
                    heigth=width*videoHeigth/videoWidth;
                }
            }
        }else{

        }
        setMeasuredDimension(width,heigth);
    }
}
