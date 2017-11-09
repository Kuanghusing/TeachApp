package widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Tian on 2017/10/31.
 * 自定义view pager不可左右滑动
 */

public class NoScrollViewPager extends ViewPager {
    private boolean isScroll=false;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

            return super.onInterceptTouchEvent(ev);


    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScroll){
            return super.onTouchEvent(ev);
        }else{
            return false;
        }

    }
    public void setScroll(boolean scroll){
        isScroll=scroll;
    }
}
