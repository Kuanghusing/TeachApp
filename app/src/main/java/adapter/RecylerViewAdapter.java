package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.teachapp.MainActivity;
import com.news.teachapp.R;

import java.util.List;

import entity.TestNameInfos;

/**
 * Created by Tian on 2017/11/13.
 * 测验室的适配器
 */

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {
    private List<TestNameInfos> testNameInfosList;

    public RecylerViewAdapter(List<TestNameInfos> testNameInfosList) {
        this.testNameInfosList = testNameInfosList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_name_items, parent,false);
        holder = new ViewHolder(view);
        holder.ll_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(v.getContext(), "12233", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TestNameInfos infos = testNameInfosList.get(position);
        holder.headers.setImageResource(infos.getImageId());
        holder.testName.setText(infos.getTestName());
        holder.createName.setText(infos.getCreateName());
        holder.createDate.setText(infos.getCreateDate());
        holder.createTime.setText(infos.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return testNameInfosList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headers;
        TextView testName;
        TextView createName;
        TextView createDate;
        TextView createTime;
        LinearLayout ll_test;

        public ViewHolder(View itemView) {
            super(itemView);
            headers =itemView.findViewById(R.id.iv_headers);
            testName=itemView.findViewById(R.id.test_name);
            createName =itemView.findViewById(R.id.create_name);
            createDate = itemView.findViewById(R.id.create_date);
            createTime =itemView.findViewById(R.id.create_time);
            ll_test=itemView.findViewById(R.id.ll_test);
        }
    }
}
