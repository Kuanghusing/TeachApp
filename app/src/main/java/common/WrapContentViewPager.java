package common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tian on 2017/11/7.
 */

public class WrapContentViewPager extends ViewPager {

    public WrapContentViewPager(Context context) {
        super(context);
    }

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       int heigth=0;
        for (int i = 0; i <getChildCount() ; i++) {
            View child=getChildAt(i);
            child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
            int h=child.getMeasuredHeight();
            if (h>heigth){
                heigth=h;
            }
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(heigth,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
