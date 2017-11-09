package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.news.teachapp.R;

import java.util.List;

/**
 * Created by Tian on 2017/11/6.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<String> listTitles;
    private Context context;

    public MyPagerAdapter(Context context,List<String> listTitles){
        this.listTitles=listTitles;
        this.context = context;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context, R.layout.fragment_titles,null);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return listTitles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }
}
