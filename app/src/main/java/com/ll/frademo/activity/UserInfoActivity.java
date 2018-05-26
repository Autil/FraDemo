package com.ll.frademo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ll.frademo.R;
import com.ll.frademo.bean.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt_back,bt_userinfo_edit;
    TextView tv_userinfo_age,tv_userinfo_height,tv_userinfo_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Bmob.initialize(this,"c50c8a2125db58a711b783daba816417");
        bt_back = findViewById(R.id.bt_back);
        bt_userinfo_edit = findViewById(R.id.bt_userinfo_edit);
        tv_userinfo_age = findViewById(R.id.tv_userinfo_age);
        tv_userinfo_height = findViewById(R.id.tv_userinfo_height);
        tv_userinfo_weight = findViewById(R.id.tv_userinfo_weight);
        bt_back.setOnClickListener(this);
        bt_userinfo_edit.setOnClickListener(this);

        getUsermsg();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back:
                UserInfoActivity.this.finish();
                break;
            case R.id.bt_userinfo_edit:
                Intent intent = new Intent(UserInfoActivity.this,EditInfo.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
                break;
        }

    }

    public void getUsermsg(){
        MyUser bu = BmobUser.getCurrentUser(MyUser.class);
        String name = bu.getUsername().toString();
        String age = bu.getAge().toString();
        String height = bu.getMheight().toString();
        String weight = bu.getMweight().toString();

        tv_userinfo_age.setText("年龄："+age);
        tv_userinfo_height.setText("身高："+height+"cm");
        tv_userinfo_weight.setText("体重："+weight+"kg");
    }
}
