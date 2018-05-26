package com.ll.frademo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ll.frademo.R;
import com.ll.frademo.db.RideDataService;
import com.ll.frademo.db.SportDataService;
import com.ll.frademo.entity.SportData;
import com.ll.frademo.event.RefreshEvent;
import com.ll.frademo.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DataActivity extends Activity {
    private EventBus eventBus;
    @Subscribe
    public void onEvent(RefreshEvent eventData) {

    }
    private ArrayList<SportData> sportDatas;
    private SportDataService sportDataService;
    private RideDataService rideDataService;

    private Map<Integer,Integer> dataMap = new HashMap<Integer,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        eventBus = EventBus.getDefault();
        eventBus.register(this);

        sportDataService = new SportDataService(this);
        rideDataService = new RideDataService(this);

        try {
            sportDatas = sportDataService.getObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for (int i = 0; i< sportDatas.size(); i++){
            dataMap.put(i,sportDatas.get(i).getNum());
            Map<String,Object> listItem = new HashMap<String,Object>();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ft.setTimeZone(TimeZone.getTimeZone("GMT+8"));

            String start = ft.format(sportDatas.get(i).getStartTime());
            String end = ft.format(sportDatas.get(i).getEndTime());
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String useTime = formatter.format((long)(sportDatas.get(i).getUseTime()*1000));

            listItem.put("distance", String.format("%.2f", sportDatas.get(i).getDistance()));
            listItem.put("module", sportDatas.get(i).getModule());
            listItem.put("startTime",start);
            listItem.put("endTime",end);
            listItem.put("useTime",useTime);

            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.simple_item
        ,new String[]{"distance","module","startTime","endTime","useTime"},new int[]{R.id.distance,
        R.id.module,R.id.startTime,R.id.endTime,R.id.useTime});

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Tools.dip2px(getApplicationContext(),90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        SwipeMenuListView listView = findViewById(R.id.data_listview);
        listView.setMenuCreator(creator);
        listView.setAdapter(simpleAdapter);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //delete action
                int num = dataMap.get(position);
                sportDataService.deletData(num);
                rideDataService.deletData(num);
                eventBus.post(new RefreshEvent());

                // false : close the menu; true : not close the menu
                //自己调到自己的activity
                Intent intent = new Intent(DataActivity.this, DataActivity.class);
                startActivity(intent);
                //close this activity
                finish();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DataActivity.this, ShowDataActivity.class);
                Bundle data = new Bundle();
                int num = dataMap.get(position);
                data.putSerializable("position", num);//传递数据位置
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }
}
