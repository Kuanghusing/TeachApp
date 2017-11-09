package widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.teachapp.R;

import org.greenrobot.eventbus.EventBus;

import base.MyApplication;
import entity.MessageInfo;
import utils.AudioRecoderUtils;
import utils.PopupWindowFactory;
import utils.Utils;

/**
 * Created by Tian on 2017/10/31.
 *
 * 输入框管理类
 */

public class EmotionInputDetector {
    private Activity mActivity;
    private InputMethodManager methodManager;//输入法管理器
    private SharedPreferences sp;
    private View mContentView;//显示的内容
    private EditText mEditText;  //输入框
    private View mSendButton;//发送按钮
    private View mAddButton;//添加按钮
    private View mEmotionLayout;//显示表情
   private ViewPager mViewPager;
    private boolean isShowEmotion=false;  // 是否显示你表情按钮
    private boolean isShowAdd=false;  //是否显示添加按钮
    private TextView mVoiceText;  //按住说话的文本
    private TextView mPopVoiceText;  //popuwindown  显示的文本
    private PopupWindowFactory mVoicePop;   //显示popuwindown
    private AudioRecoderUtils mAudioRecoderUtils;//录音工具类


    private EmotionInputDetector() {
    }
    //初始化
    public static EmotionInputDetector with(Activity activity){
        EmotionInputDetector emotionInputDetector=new EmotionInputDetector();
        emotionInputDetector.mActivity=activity;
        emotionInputDetector.methodManager= (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        emotionInputDetector.sp=activity.getSharedPreferences("emotion",Context.MODE_PRIVATE);
        return emotionInputDetector;
    }
    /*绑定内容*/
    public EmotionInputDetector bindToContent(View contentView){
        mContentView=contentView;
        return this;
    }
    /*绑定文本输入框*/
    public EmotionInputDetector bindToEditText(EditText editText){
       mEditText=editText;
        //设置是否获得焦点
        mEditText.requestFocus();
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              if (event.getAction()==MotionEvent.ACTION_UP&&mEmotionLayout.isShown()){
                  lockContentHeight();
                  hideEmotionLayout(true);
                  mEditText.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          unlockContentHeightDelayed();
                      }
                  },2000);
              }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if (s.length()>0){
                     mAddButton.setVisibility(View.GONE);
                     mSendButton.setVisibility(View.VISIBLE);
                 }else{
                     mAddButton.setVisibility(View.VISIBLE);
                     mSendButton.setVisibility(View.GONE);
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return this;
    }

    /*绑定添加按钮*/
    public EmotionInputDetector bindToAddButton(View addButton) {
        mAddButton = addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionLayout.isShown()) {
                    if (isShowEmotion) {
                        mViewPager.setCurrentItem(1);//显示图片，拍摄
                        isShowAdd = true;
                        isShowEmotion = false;
                    } else {
                        lockContentHeight();
                        hideEmotionLayout(true);
                        isShowAdd = false;
                        unlockContentHeightDelayed();
                    }
                } else {
                    if (isSoftInputShown()) {
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();
                    }
                    mViewPager.setCurrentItem(1);
                    isShowAdd = true;
                }
            }
        });
        return this;
    }
    /*绑定表情按钮*/
    public EmotionInputDetector bindToEmotionButton(View emtionButton){
        emtionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionLayout.isShown()){
                    if (isShowAdd){
                        mViewPager.setCurrentItem(0);
                        isShowEmotion=true;
                        isShowAdd=false;
                    }else{
                        lockContentHeight();
                        hideEmotionLayout(true);
                        isShowEmotion=false;

                        unlockContentHeightDelayed();
                    }
                }else{
                    if (isSoftInputShown()) {
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();
                    }
                    mViewPager.setCurrentItem(0);
                    isShowEmotion = true;

                }
            }
        });
        return this;
    }



    /*绑定说话的按钮*/
    public EmotionInputDetector bindToVoiceButton(View voiceButton){
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmotionLayout(false);
                hideSoftInput();
                mVoiceText.setVisibility(mVoiceText.getVisibility()==View.GONE?
                View.VISIBLE: View.GONE);
                mEditText.setVisibility(mVoiceText.getVisibility()==View.GONE?
                View.VISIBLE: View.GONE);
            }
        });
        return this;
    }
    /*绑定发送的按钮*/
    public EmotionInputDetector bindToSendButton(View sendButton){
        mSendButton=sendButton;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddButton.setVisibility(View.VISIBLE);
                mSendButton.setVisibility(View.GONE);
                MessageInfo info=new MessageInfo();
                info.setContent(mEditText.getText().toString());
                //eventbus是一个基于观察者模式的事件发布/订阅框架
                EventBus.getDefault().post(info);
                mEditText.setText("");
                Log.d("aaa",mEditText.getText().toString());
            }
        });
        return this;
    }
    /*绑定说话的文本*/
    public EmotionInputDetector bindToVoiceText(TextView voiceText){
        mVoiceText=voiceText;
        mVoiceText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*x坐标*/
                int x= (int)event.getX();
                   /*y坐标*/
                int y= (int) event.getY();
               switch (event.getAction()){
                   case MotionEvent.ACTION_DOWN://手指按下
                       mVoicePop.showAtLocation(v, Gravity.CENTER,0,0);
                       mVoiceText.setText("松开结束");
                       mPopVoiceText.setText("手指上滑，取消发送");
                       mVoiceText.setTag(1);
                       //开始录音
                       mAudioRecoderUtils.startRecord(mActivity);
                       break;
                   case MotionEvent.ACTION_MOVE://手指移动
                       if (wantToCancel(x,y)){
                           mVoiceText.setText("松开结束");
                           mPopVoiceText.setText("松开手指，取消发送");
                           mVoiceText.setTag("2");
                       }else {
                           mVoiceText.setText("松开结束");
                           mPopVoiceText.setText("手指上滑，取消发送");
                           mVoiceText.setTag(1);
                       }
                       break;
                   case MotionEvent.ACTION_UP://手指放开
                       mVoicePop.hidePopupWindow();
                       if (mVoiceText.getTag().equals("2")){
                           //取消录音（删除录音文件）
                           mAudioRecoderUtils.cancelRecord();

                       }else {
                           //结束录音（保存录音文件）
                         mAudioRecoderUtils.stopRecord();
                       }
                       mVoiceText.setText("按住说话");
                       mVoiceText.setTag("3");
                       mVoiceText.setVisibility(View.GONE);
                       mEditText.setVisibility(View.VISIBLE);
                       break;
               }
                return true;
            }
        });
        return this;
    }
    /*取消录音*/
    private boolean wantToCancel(int x, int y) {
        //超过按钮的宽度
        if (x<0||x>mVoiceText.getWidth()){
            return true;
        }
        //超过按钮的高度
        if (y<-50||y>mVoiceText.getHeight()+50){
            return true;
        }
        return false;
    }
    public EmotionInputDetector setEmotionView(View emotionView) {

        mEmotionLayout = emotionView;
        return this;
    }

    public EmotionInputDetector setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        return this;
    }
    public EmotionInputDetector build() {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        hideSoftInput();
        mAudioRecoderUtils = new AudioRecoderUtils();

        View view = View.inflate(mActivity, R.layout.layout_microphone, null);
        mVoicePop = new PopupWindowFactory(mActivity, view);

        //PopupWindow布局文件里面的控件
        final ImageView mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        final TextView mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mPopVoiceText = (TextView) view.findViewById(R.id.tv_recording_text);
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(Utils.long2String(time));
            }

            @Override
            public void onStop(long time, String filePath) {
                mTextView.setText(Utils.long2String(0));
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setFilePath(filePath);
                messageInfo.setVoiceTime(time);
                EventBus.getDefault().post(messageInfo);
            }
            @Override
            public void onError() {
                mVoiceText.setVisibility(View.GONE);
                mEditText.setVisibility(View.VISIBLE);
            }
        });
        return this;
    }

    /*隐藏软键盘*/
    public void hideSoftInput() {
        methodManager.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
    }

    /*隐藏表情布局*/
    public void hideEmotionLayout(boolean showSoftInput) {
        if (mEmotionLayout.isShown()){
            mEmotionLayout.setVisibility(View.GONE);
            if (showSoftInput){
                showSoftInput();
            }
        }
    }
    /*锁定不点击表情按钮弹出的高度*/
    private void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams)mContentView.getLayoutParams()).weight=1.0f;
            }
        },2000);
    }
    /*锁定点击表情按钮弹出的高度*/
    private void lockContentHeight() {
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height=mContentView.getHeight();
        params.weight=0.0f;
        Log.v("tian",params.height+"params heigth");
    }
    /*是否显示键盘*/
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight()!=0;
    }
    /*键盘弹出的高度*/
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        if (softInputHeight < 0) {
            Log.w("EmotionInputDetector", "Warning: value of softInputHeight is below zero!");
        }
        if (softInputHeight > 0) {
            sp.edit().putInt("softInputHeight", softInputHeight).apply();
        }
        return softInputHeight;
    }

    /*底部虚拟按键栏的高度*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
   /*显示软键盘*/
    private void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                methodManager.showSoftInput(mEditText, 0);
            }
        });
    }
    /*按下返回键*/
    public boolean interceptBackPress() {
        if (mEmotionLayout.isShown()) {
            hideEmotionLayout(false);
            return true;
        }
        return false;
    }
   /*显示表情的布局*/
    private void showEmotionLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = sp.getInt("showEmotionLayout", (int) (MyApplication.screenHeight * 0.45));
        }
        hideSoftInput();//隐藏软键盘
        mEmotionLayout.getLayoutParams().height = softInputHeight;
        mEmotionLayout.setVisibility(View.VISIBLE);
    }
}
