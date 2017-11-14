package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.news.teachapp.R;

import java.util.List;

import entity.ClassNameInfos;

/**
 * Created by Tian on 2017/11/13.
 * 分配选择权限的班级
 */

public class ClassNameAdapter extends BaseAdapter {
    private List<ClassNameInfos> classNameInfoses;
    private Context mContext;

    public ClassNameAdapter(Context context,List<ClassNameInfos> classNameInfoses) {
        this.mContext=context;
        this.classNameInfoses = classNameInfoses;
    }

    @Override
    public int getCount() {
        return classNameInfoses.size();
    }

    @Override
    public Object getItem(int position) {
        return classNameInfoses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.class_name_items,parent,false);
            holder=new ViewHolder();
            holder.className=view.findViewById(R.id.tv_class_name);
            holder.checkBox=view.findViewById(R.id.checkBox);
            view.setTag(holder);
        }else{
            view=convertView;
            holder= (ViewHolder) view.getTag();
        }
        holder.className.setText(classNameInfoses.get(position).getClassName());
        if (classNameInfoses.get(position).getType()==ClassNameInfos.TYPE_CHECK){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    classNameInfoses.get(position).setType(ClassNameInfos.TYPE_CHECK);
                }else{
                    classNameInfoses.get(position).setType(ClassNameInfos.TYPE_NOCHECK);
                }
            }
        });
        return view;
    }
    class ViewHolder{
        TextView className;
        CheckBox checkBox;
    }
}
