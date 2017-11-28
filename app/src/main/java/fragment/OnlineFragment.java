package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.news.teachapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.OnlineAdapter;
import entity.OnlineInfos;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.HttpUtils;
import utils.RecyclerViewItemDecoration;


/**
 * Created by Tian on 2017/10/30.
 */

public class OnlineFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private OnlineAdapter adapter;
    private List<OnlineInfos> onlineInfoses = new ArrayList<>();
    private static final String TAG = "OnlineFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        recyclerView = view.findViewById(R.id.recylerView);
        initDatas();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration());
        adapter = new OnlineAdapter(onlineInfoses);
        recyclerView.setAdapter(adapter);

        String url="http://202.175.64.187:8080/education/servlet/teacher/user/ben";

        HttpUtils.getData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,e.toString());
                
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data=response.body().string();
                Log.d(TAG,data);

            }
        });


   /*     String url="http://120.25.253.79:9080/education/servlet;



            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data=response.body().string();
                Log.v("sssss",data);

            }
        });
        HttpUtils.postData(address, requestBody,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("qqqq",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data=response.body().string();
                Log.v("zzzz",data);
            }
        });
        return view;*/
    return view;

}


    private void initDatas() {
        OnlineInfos infos=new OnlineInfos(R.mipmap.ic_launcher,"lqn",OnlineInfos.TYPE_ENTER);
        onlineInfoses.add(infos);
        OnlineInfos infos1=new OnlineInfos(R.mipmap.ic_launcher,"tian",OnlineInfos.TYPE_EXIT);
        onlineInfoses.add(infos1);


    }
}
