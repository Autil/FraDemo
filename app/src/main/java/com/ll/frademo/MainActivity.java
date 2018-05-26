package com.ll.frademo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ll.frademo.event.RefreshEvent;
import com.ll.frademo.fragements.RankFragment;
import com.ll.frademo.fragements.RunFragment;
import com.ll.frademo.fragements.UserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EventBus eventBus;
    @Subscribe
    public void onEvent(RefreshEvent eventData){

    }
    private FragmentManager manager;
    private String[] tags = new String[]{"RunFragment","RankFragment","UserFragment"};
    private List<Fragment> fragments = new ArrayList<>();
    Fragment mRunFragment,mRankFragment,mUserFragment;
    private Fragment mCurrent;
    private RadioButton rs,rr,ru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this,"c50c8a2125db58a711b783daba816417");
        eventBus = EventBus.getDefault();
        eventBus.register(this);

        initView();
        init();

    }
    private void init(){
        manager = getFragmentManager();
        mRunFragment = new RunFragment();
        mRankFragment = new RankFragment();
        mUserFragment = new UserFragment();
        fragments.add(0,mRunFragment);
        fragments.add(1,mRankFragment);
        fragments.add(2,mUserFragment);
        mCurrent = mRunFragment;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.contont_layout,mCurrent);
        ft.commitAllowingStateLoss();

    }
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        stateCheck(outState);
    }

    public void initView(){
        rs = findViewById(R.id.sport);
        rr = findViewById(R.id.rank);
        ru = findViewById(R.id.user);

        rs.setOnClickListener(this);
        rr.setOnClickListener(this);
        ru.setOnClickListener(this);
    }

    public void switchFragment(Fragment from, Fragment to, int position){
        if (mCurrent != to){
            mCurrent = to;
            FragmentTransaction transaction = manager.beginTransaction();
            if (!to.isAdded()){
                transaction.hide(from).add(R.id.contont_layout,to,tags[position]).
                        commitAllowingStateLoss();
            }else {
                transaction.hide(from).show(to).commitAllowingStateLoss();
            }
        }
    }

    private void stateCheck(Bundle savedInstanceState){
        if (savedInstanceState == null){
            manager = getFragmentManager();
            manager = getFragmentManager();
            FragmentTransaction fts = manager.beginTransaction();
            RunFragment rf = new RunFragment();
            mCurrent = rf;
            fts.add(R.id.contont_layout,rf);
            fts.commit();
        }else {
            RunFragment runf = (RunFragment) getFragmentManager().findFragmentByTag(tags[0]);
            RankFragment rankf = (RankFragment) getFragmentManager().findFragmentByTag(tags[1]);
            UserFragment userf = (UserFragment) getFragmentManager().findFragmentByTag(tags[2]);
            getFragmentManager().beginTransaction().show(runf).hide(rankf).hide(userf).
                    commitAllowingStateLoss();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sport:
                switchFragment(mCurrent,fragments.get(0),0);
                break;
            case R.id.rank:
                switchFragment(mCurrent,fragments.get(1),1);
                break;
            case R.id.user:
                switchFragment(mCurrent,fragments.get(2),2);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder isExit = new AlertDialog.Builder(this);
            isExit.setTitle("退出可能使记录中断");
            isExit.setMessage("确定要退出么？");
            isExit.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            });
            isExit.setNegativeButton("后台", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            isExit.show();
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
            System.out.println("home");
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }
}
