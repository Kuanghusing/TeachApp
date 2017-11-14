package com.news.teachapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateTestActivity extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private Button create;
    private ImageView permissionName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        initViews();
    }

    private void initViews() {
        back= (Button) findViewById(R.id.btn_back);
        create= (Button) findViewById(R.id.btn_create);
        permissionName= (ImageView) findViewById(R.id.iv_next_permission);
        back.setOnClickListener(this);
        create.setOnClickListener(this);
        permissionName.setOnClickListener(this);
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
