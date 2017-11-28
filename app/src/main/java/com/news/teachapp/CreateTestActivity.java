package com.news.teachapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class CreateTestActivity extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private Button create;
    private ImageView permissionName;
    private Intent intent;
    EditText et_test_name;
    TextView tv_request_permission;
    private static final String TAG = "CreateTestActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        initViews();
        initDatas();
    }
    private void initViews() {
        back= (Button) findViewById(R.id.btn_back);
        create= (Button) findViewById(R.id.btn_create);
        tv_request_permission= (TextView) findViewById(R.id.tv_request_permission);
        permissionName= (ImageView) findViewById(R.id.iv_next_permission);
        et_test_name= (EditText) findViewById(R.id.et_test_name);
        et_test_name.setSelection(et_test_name.length());
        back.setOnClickListener(this);
        create.setOnClickListener(this);
        permissionName.setOnClickListener(this);
    }
    private void initDatas() {
        Intent intent=getIntent();
     /*   String testName=et_test_name.getText().toString().trim();
        if (TextUtils.isEmpty(testName)){
            Toast.makeText(this, "测验室名字不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            intent.putExtra("testName",testName);
        }*/
        ArrayList<String> list = intent.getStringArrayListExtra("aaa");
        String c = "";
        if(list != null){
            for (String s : list) {
                c += s+"  ";
                //Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
            }
            tv_request_permission.setText(c);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_create:
                Toast.makeText(this, "你创建了我", Toast.LENGTH_SHORT).show();
                intent=new Intent(this,CreateActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_next_permission:
                intent=new Intent(this,ClassNameActivity.class);
                startActivity(intent);
                break;
        }
    }
}
