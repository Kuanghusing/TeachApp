package utils;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by Tian on 2017/10/31.
 *设置PopupWindow的工具类
 */

public class PopupWindowFactory {

    private Context mContext;
    private PopupWindow popupWindow;


    /*view,PopupWindow显示的布局文件*/
    public PopupWindowFactory(Context mContext, View view) {
      this(mContext,view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public PopupWindowFactory(Context mContext, View view, int width, int heigth) {
       init(mContext,view,width,heigth);
    }

    private void init(Context mContext, View view, int width, int heigth) {
      this.mContext=mContext;

        //必须要有
        view.setFocusable(true);//让PopupWindow获得焦点
        view.setFocusableInTouchMode(true);

        popupWindow=new PopupWindow(view,width,heigth,true);
        popupWindow.setFocusable(true);

        //重写onkeyListener，按返回键消失
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
               if (keyCode==KeyEvent.KEYCODE_BACK){
                   popupWindow.dismiss();
                   return true;
               }
                return false;
            }
        });
        /*点击其他地方消失*/
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               if (popupWindow!=null&&popupWindow.isShowing()){
                   popupWindow.dismiss();
                   return true;
               }
                return false;
            }
        });

    }

    public PopupWindow getPopupWindow(){
        return popupWindow;
    }

    /*PopupWindow显示的位置
    相对于父控件的相对位置：showAtLocation(View parent, int gravity, int x, int y)：相对于父控件的位置，同时可以设置偏移量。*/
    public void showAtLocation(View parent, int gravity, int x, int y){
        if (popupWindow.isShowing()){
            return;
        }
        popupWindow.showAtLocation(parent,gravity,x,y);

    }

    /*PopupWindow显示的位置
    相对于视图中某个控件的相对位置：
showAsDropDown(View anchor)：相对某个控件的位置（正左下方），无偏移。
showAsDropDown(View anchor, int xoff, int yoff)：相对某个控件的位置，同时可以设置偏移。
showAsDropDown(View anchor, int xoff, int yoff, int gravity)：相对某个控件的位置，对齐方式（尝试过，但似乎没有效果），同时可以设置偏移。
*/
    public void showAsDropDown(View anchor){
        showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff){
        if (popupWindow.isShowing()){
            return;
        }
        popupWindow.showAsDropDown(anchor, xoff, yoff);
    }
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity){
        showAtLocation(anchor, xoff, yoff, gravity);
    }

    /*隐藏PopupWindow*/
    public void hidePopupWindow(){
        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
}
