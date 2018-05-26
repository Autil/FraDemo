package com.ll.frademo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ll.frademo.R;
import com.ll.frademo.bean.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditInfo extends AppCompatActivity implements View.OnClickListener{
    private Button bt_back_et,bt_eidt;
    private EditText et_age,et_height,et_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        Bmob.initialize(this,"c50c8a2125db58a711b783daba816417");
        bt_back_et = findViewById(R.id.bt_back_et);
        bt_eidt = findViewById(R.id.bt_dit);
        et_age = findViewById(R.id.et_userinfo_age);
        et_height = findViewById(R.id.et_userinfo_height);
        et_weight = findViewById(R.id.et_userinfo_weight);
        bt_eidt.setOnClickListener(this);
        bt_back_et.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back_et:
                EditInfo.this.finish();
                break;
            case R.id.bt_dit:
                MyUser bu = BmobUser.getCurrentUser(MyUser.class);
                bu.setAge(Integer.valueOf(et_age.getEditableText().toString()));
                bu.setMheight(Float.valueOf(et_height.getEditableText().toString()));
                bu.setMweight(Float.valueOf(et_weight.getEditableText().toString()));
                bu.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(EditInfo.this,"信息修改成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditInfo.this,UserInfoActivity.class);
                            startActivity(intent);
                            EditInfo.this.finish();
                        }
                    }
                });
                break;
        }

    }
}
