package com.ll.frademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ll.frademo.bean.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_age,et_height,et_weight;
    private EditText et_username,et_psw,et_psw_tiwce;
    private Button bt_register_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this,"c50c8a2125db58a711b783daba816417");
        et_username = findViewById(R.id.et_username1);
        et_psw = findViewById(R.id.et_password1);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        bt_register_ok = findViewById(R.id.bt_sign);
        et_psw_tiwce = findViewById(R.id.et_psw_tiwce);
        bt_register_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = et_username.getEditableText().toString();
        String psw = et_psw.getEditableText().toString();
        String age = et_age.getEditableText().toString();
        String height = et_height.getEditableText().toString();
        String weight = et_weight.getEditableText().toString();
        String psw1 = et_psw_tiwce.getEditableText().toString();
        et_username.setError("");
        et_psw.setError("");
        et_age.setError("");
        et_height.setError("");
        et_weight.setError("");
        et_psw_tiwce.setError("");
        if (isValid(name,psw,psw1,age,height,weight))
            storeUsermsg(name,psw);

    }

    public void storeUsermsg(String name, String psw){
        int i = 0;
        MyUser bu = new MyUser();
        bu.setUsername(name);
        bu.setPassword(psw);
        bu.setAge(Integer.parseInt(et_age.getText().toString()));
        bu.setMheight(Float.parseFloat(et_height.getText().toString()));
        bu.setMweight(Float.parseFloat(et_weight.getText().toString()));
        bu.setMdistance(i);
        bu.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null){
                    Toast.makeText(RegisterActivity.this,"注册成功",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                }else {
                    Toast.makeText(RegisterActivity.this,e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean isValid(String name,String psw,String psw1,String age,String height,String weight){
        if (name.equals("")){
            et_username.setError("用户名不能为空");
            return false;
        }else if (!name.matches("^[a-zA-Z0-9]+$")){
            et_username.setError("用户名只能位字母和数字");
            return false;
        }else if (psw.equals("")){
            et_psw.setError("密码不能为空");
            return false;
        }else if (!psw.matches("^[a-zA-Z0-9]+$")){
            et_username.setError("密码只能位字母和数字");
            return false;
        }else if (age.equals("")){
            et_age.setError("年龄不能为空");
            return false;
        }else if (height.equals("")){
            et_height.setError("身高不能为空");
            return false;
        }else if (weight.equals("")){
            et_weight.setError("体重不能为空");
            return false;
        }else if (!psw.equals(psw1)){
            et_psw_tiwce.setError("二次输入密码错误！请重新输入！");
            Toast.makeText(this,"二次密码输入错误，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
