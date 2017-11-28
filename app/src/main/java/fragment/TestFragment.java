package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.news.teachapp.R;

import java.util.ArrayList;
import java.util.List;

import adapter.TitleinfosAdapter;
import entity.TitlesInfos;
import utils.RecyclerViewItemDecoration;

/**
 * Created by Tian on 2017/10/30.
 * 测验
 */

public class TestFragment extends Fragment{
    private View view;
    private RecyclerView recyclerView;
    private TitleinfosAdapter adapter;
    private List<TitlesInfos>  titlesInfoses=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_test,container,false);
        recyclerView=view.findViewById(R.id.recylerView);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration());
        TitlesInfos infos=new TitlesInfos("文言文测试1");
        titlesInfoses.add(infos);
        TitlesInfos infos1=new TitlesInfos("文言文测试2");
        titlesInfoses.add(infos1);
        adapter=new TitleinfosAdapter(titlesInfoses);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
