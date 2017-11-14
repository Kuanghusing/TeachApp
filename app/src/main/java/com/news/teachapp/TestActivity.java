package com.news.teachapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.GridViewAdapter;
import adapter.MyPagerAdapter;
import fragment.TitlesFragment;
import utils.VideoPlayerManager;

public class TestActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    GridView gridView;
    LinearLayout ll;
    LinearLayout ll_result;
    Button btn_back;
    TabLayout tabLayout;
    ViewPager viewPager;
    private List<String> titles=new ArrayList<>();
    private List<Fragment> fragmentList=new ArrayList<>();
    private MyPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();
        ll.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        gridView.setAdapter(new GridViewAdapter(this));
        initDatas();
    }
    private void initViews() {
        gridView= (GridView) findViewById(R.id.gridView);
        ll= (LinearLayout) findViewById(R.id.ll);
        btn_back= (Button) findViewById(R.id.btn_back);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        ll_result= (LinearLayout) findViewById(R.id.ll_result);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
    }
    private void initDatas() {
        fragmentList.add(new TitlesFragment());
        for (int i = 1; i < 11; i++) {
            titles.add("第"+i+"道题");
        }
        adapter=new MyPagerAdapter(this,titles);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll:
                if(ll_result.isShown()){
                    ll_result.setVisibility(View.GONE);
                }else {
                    ll_result.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.getInstance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.getInstance().onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
