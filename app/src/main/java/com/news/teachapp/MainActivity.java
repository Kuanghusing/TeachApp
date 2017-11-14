package com.news.teachapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentAdapter;
import fragment.ChatFragment;
import fragment.OnlineFragment;
import fragment.TeachPlanFragment;
import fragment.TestFragment;
import fragment.TestResultFragment;

import static fragment.ChatFragment.mDetector;
/*https://github.com/CHENGTIANG/TeachApp.git*/

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Fragment> fragmentList=new ArrayList<>();
    private MyFragmentAdapter fragmentAdapter;
    private TabLayout tabLayout;
    private List<String> listTitle=new ArrayList<>();
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    private void initViews() {
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        back= (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setCurrentItem(0);

        listTitle.add("在线情况");
        listTitle.add("聊天室");
        listTitle.add("测验");
        listTitle.add("测验结果");
        listTitle.add("远程教案");

        fragmentList.add(new OnlineFragment());
        fragmentList.add(new ChatFragment());
        fragmentList.add(new TestFragment());
        fragmentList.add(new TestResultFragment());
        fragmentList.add(new TeachPlanFragment());

        fragmentAdapter=new MyFragmentAdapter(getSupportFragmentManager(),fragmentList,listTitle);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragmentAdapter);

    }



}
