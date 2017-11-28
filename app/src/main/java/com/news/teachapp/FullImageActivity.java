package com.news.teachapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.FullImageInfo;
import utils.Utils;
import widget.BaseDragZoomImageView;

/**
 * 显示点进去的图片
 */
public class FullImageActivity extends Activity implements View.OnClickListener{

  /*  @Bind(R.id.full_image)
    ImageView fullImage;*/
    @Bind(R.id.full_lay)
  LinearLayout fullLay;
    private Button savePhoto;
    private int mLeft;
    private int mTop;
    private float mScaleX;  //x缩放比例
    private float mScaleY;  //y缩放比例
    private Drawable mBackground;
    BaseDragZoomImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉actionbar
        setContentView(R.layout.activity_full_image);
        savePhoto=findViewById(R.id.btn_save);
        savePhoto.setOnClickListener(this);
         imageView=findViewById(R.id.full_image);

     //  Glide.with(this).load().into(imageView);
      ButterKnife.bind(this);
        /*注册EventBus*/


        EventBus.getDefault().register(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onDataSynEvent(final FullImageInfo fullImageInfo) {
        /*拿到图片的坐标，设置他的宽高*/
        final int left = fullImageInfo.getLocationX();
        final int top = fullImageInfo.getLocationY();
        final int width = fullImageInfo.getWidth();
        final int height = fullImageInfo.getHeight();
        mBackground = new ColorDrawable(Color.BLACK);
       fullLay.setBackground(mBackground);


        Toast.makeText(this, fullImageInfo.getImageUrl(), Toast.LENGTH_SHORT).show();

        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                int location[] = new int[2];
                imageView.getLocationOnScreen(location);
                mLeft = left - location[0];
                mTop = top - location[1];
                mScaleX = width * 1.0f / imageView.getWidth();
                mScaleY = height * 1.0f / imageView.getHeight();
               // activityEnterAnim();
                return true;
            }
        });
       /* Bitmap bitmap= BitmapUtils.decodeSampledBitmapFromResource(getResources()
                ,R.mipmap.ic_launcher,900,500);
        fullImage.setImageBitmap(bitmap);*/
      /*  Bitmap bitmap= BitmapUtils.decodeSampledBitmapFromResource(getResources(),fullImageInfo.getImageUrl().,100,100);
        imageView.setImageBitmap(bitmap);*/
//Glide加载图片
       Glide.with(this).load(fullImageInfo.getImageUrl()).into(imageView);




    }
   //  点击图片的动画效果
   /* private void activityEnterAnim() {
        fullImage.setPivotX(0);
        fullImage.setPivotY(0);
        fullImage.setScaleX(mScaleX);
        fullImage.setScaleY(mScaleY);
        fullImage.setTranslationX(mLeft);
        fullImage.setTranslationY(mTop);
        fullImage.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
*/
    /*@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void activityExitAnim(Runnable runnable) {
        fullImage.setPivotX(0);
        fullImage.setPivotY(0);
        fullImage.animate().scaleX(mScaleX).scaleY(mScaleY).translationX(mLeft).translationY(mTop).
                withEndAction(runnable).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 255, 0);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }*/
   /*按下返回键*/
    @Override
    public void onBackPressed() {
        finish();
      /*  activityExitAnim(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });*/
    }

    @OnClick(R.id.full_image)
    public void onClick() {

        finish();
       /* activityExitAnim(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });*/
    }



    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                Toast.makeText(this, "你点击了我", Toast.LENGTH_SHORT).show();
                final FullImageInfo image=new FullImageInfo();
                image.setImageUrl("http://images.cnitblog.com/blog2015/708076/201504/290845399748950.png");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url=new URL(image.getImageUrl());
                            InputStream is=url.openStream();
                            Bitmap b= BitmapFactory.decodeStream(is);
                            Utils.saveFile(b,image.getImageUrl());
                        } catch (Exception e) {
                            Log.v("lqnlog", ""+e.toString());
                            e.printStackTrace();
                        }

                    }
                }).start();
              /*  String path= Environment.getExternalStorageDirectory().getAbsolutePath();
                File file=new File(path);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                this.sendBroadcast(intent);
                Toast.makeText(this, "图片保存成功"+path, Toast.LENGTH_SHORT).show();*/
                break;
        }
    }
}
