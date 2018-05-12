package com.ll.frademo.fragements;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ll.frademo.LoginActivity;
import com.ll.frademo.MainActivity;
import com.ll.frademo.R;
import com.ll.frademo.bean.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by l4396 on 2018/3/21.
 */

public class UserFragment extends Fragment implements View.OnClickListener{
    private TextView tv_username,tv_age,tv_height,tv_weight;
    private Button bt_login_out;
    FragmentActivity mainactivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_layout,null);

        mainactivity = (FragmentActivity) getActivity();
        Bmob.initialize(mainactivity,"");
        tv_username = view.findViewById(R.id.tv_username);
        tv_age = view.findViewById(R.id.tv_age);
        tv_height = view.findViewById(R.id.tv_height);
        tv_weight = view.findViewById(R.id.tv_weight);
        bt_login_out = view.findViewById(R.id.bt_login_out);
        bt_login_out.setOnClickListener(this);
        getUsermsg();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login_out:
                BmobUser.logOut();

                Intent intent = new Intent(mainactivity, LoginActivity.class);
                startActivity(intent);
                mainactivity.finish();
        }

    }

    public void getUsermsg(){
        MyUser bu = BmobUser.getCurrentUser(MyUser.class);
        String name = bu.getUsername().toString();
        String age = bu.getAge().toString();
        String height = bu.getMheight().toString();
        String weight = bu.getMweight().toString();

        tv_username.setText(name);
        tv_age.setText("年龄："+age);
        tv_height.setText("身高："+height+"cm");
        tv_weight.setText("体重："+weight+"kg");
    }
}
