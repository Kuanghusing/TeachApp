package com.news.teachapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ClassNameAdapter;
import entity.ClassNameInfos;

public class ClassNameActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private Button back;
    private TextView sure;
    private ClassNameAdapter adapter;
    private List<ClassNameInfos> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_name);
        initViews();
        initDatas();

    }

    private void initDatas() {
        ClassNameInfos classes=new ClassNameInfos(ClassNameInfos.TYPE_CHECK,"高三甲");
        infos.add(classes);
        ClassNameInfos classes1=new ClassNameInfos(ClassNameInfos.TYPE_CHECK,"高三丙");
        infos.add(classes1);
        ClassNameInfos classes2=new ClassNameInfos(ClassNameInfos.TYPE_CHECK,"高三丙");
        infos.add(classes2);
    }

    private void initViews() {
        back= (Button) findViewById(R.id.btn_back);
        sure= (TextView) findViewById(R.id.tv_sure);
        listView= (ListView) findViewById(R.id.listView);
        infos=new ArrayList<>();
        adapter=new ClassNameAdapter(this,infos);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_sure:
                finish();
                break;
        }
    }
}
