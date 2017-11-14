package com.news.teachapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.RecylerViewAdapter;
import entity.TestNameInfos;
import utils.RecyclerViewItemDecoration;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private TextView create;
    private RecyclerView recylerView;
    private RecylerViewAdapter adapter;
    private List<TestNameInfos> testInfos=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initDatas();
        initViews();


    }

    private void initDatas() {
        TestNameInfos info=new TestNameInfos(R.mipmap.ic_launcher,"数学测验室","lqn","2017/11/14","9:14:25");
        testInfos.add(info);
        TestNameInfos info1=new TestNameInfos(R.mipmap.ic_launcher,"数学测验室2","tian","2017/10/14","9:14:25");
        testInfos.add(info1);
    }

    private void initViews() {
        back= (Button) findViewById(R.id.btn_back);
        create= (TextView) findViewById(R.id.tv_create);
        recylerView= (RecyclerView) findViewById(R.id.recylerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recylerView.setLayoutManager(layoutManager);
       /*为recyclerView添加分割线*/
        recylerView.addItemDecoration(new RecyclerViewItemDecoration());
        adapter=new RecylerViewAdapter(testInfos);
        recylerView.setAdapter(adapter);
        create.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case R.id.btn_back:
               Toast.makeText(this, "你点击了返回键", Toast.LENGTH_SHORT).show();
            finish();
            break;
            case R.id.tv_create:
                Intent intent=new Intent(this,CreateTestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
