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

import adapter.TestResultAdapter;
import entity.TestResultInfos;
import utils.RecyclerViewItemDecoration;

/**
 * Created by Tian on 2017/10/30.
 * 测验成绩
 */

public class TestResultFragment extends Fragment {
    View view;
    private List<TestResultInfos> infosList=new ArrayList<>();
    private RecyclerView recyclerView;
    private TestResultAdapter adapter=new TestResultAdapter(infosList);

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_test_result,container,false);
        recyclerView=view.findViewById(R.id.recylerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        /*为recyclerView添加分割线*/
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration());
        initResults();
        return view;
    }
    private void initResults() {
        TestResultInfos result=new TestResultInfos(R.mipmap.ic_launcher,"aaa",
                2,3,4,9);
        TestResultInfos result1=new TestResultInfos(R.mipmap.ic_launcher,"aaa",
                22,32,42,92);
        infosList.add(result);
        infosList.add(result1);
    }
}
