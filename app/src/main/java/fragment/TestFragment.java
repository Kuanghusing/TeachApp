package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.news.teachapp.R;
import com.news.teachapp.TestActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tian on 2017/10/30.
 * 测验
 */

public class TestFragment extends Fragment implements View.OnClickListener{
    private View view;
    @Bind(R.id.btn_testTitle)
    Button testTitlel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_test,container,false);
        ButterKnife.bind(this,view);
        testTitlel.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getContext(), TestActivity.class);
        startActivity(intent);
    }
}
