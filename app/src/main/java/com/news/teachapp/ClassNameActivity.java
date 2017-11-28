package com.news.teachapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ClassNameAdapter;
import entity.ClassNameInfos;

public class ClassNameActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private Button back;
    private TextView sure;
    private ClassNameAdapter adapter;
    private List<ClassNameInfos> infos;
    private ArrayList<String> classNames;
    private HashMap<Integer,Boolean> isSelected;
    private static final String TAG = "ClassNameActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_name);
        infos=new ArrayList<>();
        initDatas();
        initViews();

    }

    private void initDatas() {

        ClassNameInfos classes=new ClassNameInfos(ClassNameInfos.TYPE_CHECK,"高三甲");
        infos.add(classes);
        ClassNameInfos classes1=new ClassNameInfos(ClassNameInfos.TYPE_CHECK,"高三丙");
        infos.add(classes1);
        ClassNameInfos classes2=new ClassNameInfos(ClassNameInfos.TYPE_NOCHECK,"高三丙2");
        infos.add(classes2);
    }
    private void initViews() {
        back= (Button) findViewById(R.id.btn_back);
        sure= (TextView) findViewById(R.id.tv_sure);
        listView= (ListView) findViewById(R.id.listView);
        isSelected=new HashMap<Integer, Boolean>();
        adapter=new ClassNameAdapter(this,infos,isSelected);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_sure:
                Intent intent=new Intent(this,CreateTestActivity.class);
                classNames=new ArrayList<>();
                if (infos.size()>0){
                    Log.d(TAG, "onClick: "+infos.size());
                    isSelected=adapter.getIsSelected();
                    for (int i = 0; i <isSelected.size() ; i++) {
                           // Toast.makeText(this, ""+isSelected.get(i), Toast.LENGTH_SHORT).show();
                        if (isSelected.get(i).equals(true)){
                           String name=infos.get(i).getClassName();
                            isSelected.put(i,false);
                            classNames.add(name);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
                intent.putExtra("aaa",classNames);
                startActivity(intent);
                break;
        }
    }
}
