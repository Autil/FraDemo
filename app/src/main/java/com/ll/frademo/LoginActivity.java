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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt1,bt2;
    private EditText et_name,et_psw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this,"c50c8a2125db58a711b783daba816417");
        bt1 = findViewById(R.id.bt_login);
        bt2 = findViewById(R.id.bt_login_register);
        et_name = findViewById(R.id.et_username);
        et_psw = findViewById(R.id.et_password);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_login:
                et_name.setError("");
                et_name.setError("");
                Login();
                break;
            case R.id.bt_login_register:
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;
        }
    }
    public void Login(){
        String name = et_name.getEditableText().toString();
        String psw = et_psw.getEditableText().toString();
        if (name.equals("")){
            et_name.setError("用户名不能为空");
            return;
        }else if (psw.equals("")){
            et_psw.setError("密码不能为空");
            return;
        }

        MyUser bu = new MyUser();
        bu.setUsername(name);
        bu.setPassword(psw);
        bu.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null){
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }else {
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.
                            LENGTH_SHORT).show();
                }
            }
        });
    }
}
