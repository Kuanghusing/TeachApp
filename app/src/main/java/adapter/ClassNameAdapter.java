package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.teachapp.R;

import java.util.HashMap;
import java.util.List;

import entity.ClassNameInfos;

/**
 * Created by Tian on 2017/11/13.
 * 分配选择权限的班级
 */

public class ClassNameAdapter extends BaseAdapter {
    private List<ClassNameInfos> classNameInfoses;
    private Context mContext;
    //checkbox的选中状态
    private static HashMap<Integer,Boolean> isSelected ;

    public ClassNameAdapter(Context context, List<ClassNameInfos> classNameInfoses, HashMap<Integer,Boolean> isSelected) {
        this.mContext=context;
        this.classNameInfoses = classNameInfoses;
        this.isSelected=isSelected;
        initData();

    }

    private void initData() {
        for (int i = 0; i <classNameInfoses.size() ; i++) {
            isSelected.put(i,false);
          //  Toast.makeText(mContext, ""+i, Toast.LENGTH_SHORT).show();

        }
    }


    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ClassNameAdapter.isSelected = isSelected;
    }
    public static HashMap<Integer,Boolean> getIsSelected(){
        return isSelected;
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
        final ViewHolder holder;
        if (convertView==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.class_name_items,parent,false);
            holder=new ViewHolder();
            holder.className=view.findViewById(R.id.tv_class_name);
            holder.checkBox=view.findViewById(R.id.checkBox);
            holder.ll=view.findViewById(R.id.ll_class_name);
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
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected.get(position)){
                    isSelected.put(position,false);
                    setIsSelected(isSelected);
                }else{
                    isSelected.put(position,true);
                    setIsSelected(isSelected);
                }
                notifyDataSetChanged();
            }
        });
     //   Toast.makeText(mContext, ""+getIsSelected().get(0), Toast.LENGTH_SHORT).show();
       holder.checkBox.setChecked(getIsSelected().get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected.get(position)){
                    isSelected.put(position,false);
                    setIsSelected(isSelected);
                }else{
                    isSelected.put(position,true);
                    setIsSelected(isSelected);
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }
    class ViewHolder{
        TextView className;
        CheckBox checkBox;
        LinearLayout ll;
    }
}
