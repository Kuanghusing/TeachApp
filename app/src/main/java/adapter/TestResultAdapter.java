package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.teachapp.R;

import java.util.List;

import entity.TestResultInfos;

/**
 * Created by Tian on 2017/10/31.
 * 测验结果
 */

public class TestResultAdapter  extends RecyclerView.Adapter<TestResultAdapter.ViewHolder>{
    private List<TestResultInfos> resultInfoses;

    public TestResultAdapter(List<TestResultInfos> resultInfoses) {
        this.resultInfoses = resultInfoses;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageUrl;
        TextView userName;
        TextView totalTitle;
        TextView correctTitle;
        TextView score;
        TextView errorTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            imageUrl=itemView.findViewById(R.id.user_image);
            userName=itemView.findViewById(R.id.userName);
            totalTitle=itemView.findViewById(R.id.total);
            correctTitle=itemView.findViewById(R.id.correctTitle);
            score=itemView.findViewById(R.id.score);
            errorTitle=itemView.findViewById(R.id.errortTitle);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())

                .inflate(R.layout.test_result_list,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TestResultInfos infos=resultInfoses.get(position);
        holder.imageUrl.setImageResource(infos.getImageUrl());
        holder.userName.setText(infos.getUseName());
        holder.totalTitle.setText(infos.getTotal()+"");
        holder.correctTitle.setText(infos.getCorrectTitle()+"");
        holder.score.setText((int) infos.getScore()+"");
        holder.errorTitle.setText(infos.getErrorTitle()+"");
    }
    @Override
    public int getItemCount() {
        return resultInfoses.size();
    }



}
