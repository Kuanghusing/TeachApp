package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.news.teachapp.R;

/**
 * Created by Tian on 2017/11/6.
 * 放置题目的对与错信息
 */

public class GridViewAdapter extends BaseAdapter{
    Context context;
    public GridViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.grid_item,null);
        return view;
    }
}
