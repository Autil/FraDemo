package com.ll.frademo.fragements;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ll.frademo.R;
import com.ll.frademo.bean.MyUser;
import com.ll.frademo.event.MyEvent;
import com.ll.frademo.map.map;
import com.ll.frademo.service.BackService;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by l4396 on 2018/3/21.
 */

public class RunFragment extends Fragment {
    FragmentActivity mainActivity;
    private Button map_bt;
    private EventBus eventBus;
    private MyEvent myEvent = new MyEvent();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String module = "跑步模式";

    private TextView title,distance,speed,ey;
    private static boolean sport_status = false;

    private Chronometer chronometer;
    private Button start;
    private boolean quit = true;
    private FragmentActivity fragmentActivity;

    public static RunFragment instance(){
        RunFragment view = new RunFragment();
        return view;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.runfragment,null);

        eventBus = EventBus.getDefault();
        eventBus.register(this);

        mainActivity = (FragmentActivity)getActivity();
        Bmob.initialize(mainActivity,"c50c8a2125db58a711b783daba816417");
        final MyUser bu = BmobUser.getCurrentUser(MyUser.class);

        sharedPreferences = mainActivity.getSharedPreferences("FraDemo",mainActivity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        title = view.findViewById(R.id.tv_sport_module);

        final Intent intent = new Intent(mainActivity,BackService.class);

        start = view.findViewById(R.id.bt_start);
        chronometer = view.findViewById(R.id.show_time);
        speed = view.findViewById(R.id.tv_show_speed);
        distance = view.findViewById(R.id.tv_show_distance);
        ey = view.findViewById(R.id.tv_show_ec);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sport_status = !sport_status;
                if (start.getText().equals("开始")){
                    start.setText(R.string.sport_stop);
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    mainActivity.startService(intent);
                    quit = false;
                }else {
                    start.setText(R.string.sport_start);
                    chronometer.stop();
                    mainActivity.stopService(intent);
                    MyUser bu = BmobUser.getCurrentUser(MyUser.class);
                    bu.setMdistance((int)myEvent.getDistance());
                    bu.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.i("更新距离成功",e.getMessage());
                            }
                        }
                    });
                    quit = true;
                }
            }
        });

        map_bt = view.findViewById(R.id.bt_map);
        map_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(mainActivity,map.class);
                if (sport_status){//运动状态的话要携带描线数据
                    Bundle data = new Bundle();
                    data.putSerializable("list", (Serializable) myEvent.getList());
                    intent1.putExtras(data);
                }
                startActivity(intent1);
            }
        });

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x1122){
                    speed.setText("当前速度:\n"+String.format("%.2f",myEvent.getSpeed())+"m/s");
                    distance.setText("当前里程:\n"+String.format("%.2f",myEvent.getDistance())+"m");
                    ey.setText("耗能："+String.format("%.2f",myEvent.getDistance()*bu.getMweight()*0.001)+"千卡");
                    myEvent.setModule(module);
                    eventBus.post(myEvent);
                }
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message m = new Message();
                m.what = 0x1122;
                if (!quit){
                    handler.sendMessage(m);
                }
            }
        },0,1000);

        final ImageButton modu = view.findViewById(R.id.imag_sport_module);

        DroppyMenuPopup.Builder builder = new DroppyMenuPopup.Builder(mainActivity,modu);

        builder.addMenuItem(new DroppyMenuItem("跑步",R.drawable.run)).addSeparator().addMenuItem(new DroppyMenuItem("开车",R.drawable.car));

        builder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                switch (id){
                    case 0://跑步模式
                        modu.setImageResource(R.drawable.run);
                        title.setText("跑步模式");
                        module = "跑步模式";
                        editor.putInt("module",id);
                        editor.commit();
                        break;
                    case 1:
                        modu.setImageResource(R.drawable.car);
                        title.setText("开车模式");
                        module = "开车模式";
                        editor.putInt("module",id);
                        editor.commit();
                        break;
                }
                myEvent.setModule(module);
            }
        });
        DroppyMenuPopup droppyMenuPopup = builder.build();

        switch (sharedPreferences.getInt("module",0)){
            case 0:
                modu.setImageResource(R.drawable.run);
                title.setText("跑步模式");
                module = "跑步模式";
                break;
            case 1:
                modu.setImageResource(R.drawable.car);
                title.setText("开车模式");
                module = "开车模式";
                break;
        }
        myEvent.setModule(module);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("RunFragment   onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("RunFragment   onStart");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.quit = true;

        //反注册EventBus
        eventBus.unregister(this);

    }

    @Subscribe
    public void onEvent(MyEvent eventData){
        myEvent = eventData;
    }
}
