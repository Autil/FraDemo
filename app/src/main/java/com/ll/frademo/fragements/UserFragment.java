package com.ll.frademo.fragements;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ll.frademo.LoginActivity;
import com.ll.frademo.R;
import com.ll.frademo.activity.DataActivity;
import com.ll.frademo.activity.UserInfoActivity;
import com.ll.frademo.bean.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by l4396 on 2018/3/21.
 */

public class UserFragment extends Fragment implements View.OnClickListener{
    private TextView tv_usermsg,tv_sport_data,tv_username;
    private Button bt_login_out;
    FragmentActivity mainactivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_layout,null);

        mainactivity = (FragmentActivity) getActivity();
        Bmob.initialize(mainactivity,"c50c8a2125db58a711b783daba816417");
        tv_username = view.findViewById(R.id.tv_username);
        tv_usermsg = view.findViewById(R.id.tv_user_message);
        tv_sport_data = view.findViewById(R.id.tv_sport_data);
        bt_login_out = view.findViewById(R.id.bt_login_out);
        bt_login_out.setOnClickListener(this);
        tv_usermsg.setOnClickListener(this);
        tv_sport_data.setOnClickListener(this);
        getUsername();
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
                break;
            case R.id.tv_user_message:
                Intent intent1 = new Intent(mainactivity, UserInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_sport_data:
                Intent intent2 = new Intent(mainactivity, DataActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }

    }

    public void getUsername(){
        MyUser bu = BmobUser.getCurrentUser(MyUser.class);
        String name = bu.getUsername().toString();
        tv_username.setText(name);
    }
}
