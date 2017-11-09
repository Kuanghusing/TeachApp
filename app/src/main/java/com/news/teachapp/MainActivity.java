package com.news.teachapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentAdapter;
import fragment.ChatFragment;
import fragment.OnlineFragment;
import fragment.TeachPlanFragment;
import fragment.TestFragment;
import fragment.TestResultFragment;

import static fragment.ChatFragment.mDetector;

public class MainActivity extends AppCompatActivity {
/*    private Toolbar toolbar;
    private TextView online_detail;
    private TextView chat;
    private TextView test;
    private TextView test_result;
    private TextView teach_plan;
    private ImageView line_tab;*/
    private ViewPager viewPager;
    private List<Fragment> fragmentList=new ArrayList<>();
    private MyFragmentAdapter fragmentAdapter;
    private TabLayout tabLayout;
    private List<String> listTitle=new ArrayList<>();

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
       /* toolbar= (Toolbar) findViewById(R.id.toolbar);
        online_detail= (TextView) findViewById(R.id.tv_online_detail);
        chat= (TextView) findViewById(R.id.tv_chat);
        test= (TextView) findViewById(R.id.tv_test);
        test_result= (TextView) findViewById(R.id.tv_test_result);
        teach_plan= (TextView) findViewById(R.id.tv_teach_plan);
        line_tab= (ImageView) findViewById(R.id.line_tab);*/
        viewPager.setCurrentItem(0);
      /*  online_detail.setOnClickListener(this);
        chat.setOnClickListener(this);
        test.setOnClickListener(this);
        test_result.setOnClickListener(this);
        teach_plan.setOnClickListener(this);
*/

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
     /*   tabLayout.post(new Runnable() {
            @Override
            public void run() {
                Utils.setIndicator(tabLayout,0,0);
            }
        });*/
        viewPager.setAdapter(fragmentAdapter);
       // viewPager.setOnPageChangeListener(this);
    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_online_detail:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_chat:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tv_test:
                viewPager.setCurrentItem(2);
                break;
            case R.id.tv_test_result:
                viewPager.setCurrentItem(3);
                break;
            case R.id.tv_teach_plan:
                viewPager.setCurrentItem(4);
                break;
        }
    }*/

 /*   @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                online_detail.setTextColor(Color.BLACK);
                break;
            case 1:
                chat.setTextColor(Color.BLACK);
                break;
            case 2:
                test.setTextColor(Color.BLACK);
                break;
            case 3:
                test_result.setTextColor(Color.BLACK);
                break;
            case 4:
                teach_plan.setTextColor(Color.BLACK);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }*/

}
