package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.teachapp.R;

import java.util.List;

import entity.OnlineInfos;

/**
 * Created by Tian on 2017/11/14.
 * 在线情况的适配器
 */

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ViewHolder> {
    private List<OnlineInfos> onlineInfoses;

    public OnlineAdapter(List<OnlineInfos> onlineInfoses) {
        this.onlineInfoses = onlineInfoses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder holder;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.online_items,parent,false);
        holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OnlineInfos infos=onlineInfoses.get(position);
        holder.headers.setImageResource(infos.getImageId());
        holder.userName.setText(infos.getUserName());
       // holder.userType.setText(infos.getType());

    }

    @Override
    public int getItemCount() {
        return onlineInfoses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView headers;
        TextView userName;
        TextView userType;
        public ViewHolder(View itemView) {
            super(itemView);
            headers=itemView.findViewById(R.id.iv_headers);
            userName= itemView.findViewById(R.id.tv_userName);
            userType=itemView.findViewById(R.id.tv_userType);
        }
    }
}
