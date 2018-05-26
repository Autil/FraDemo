package com.ll.frademo.fragements;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ll.frademo.R;
import com.ll.frademo.bean.MyUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by l4396 on 2018/3/21.
 */

public class RankFragment extends Fragment implements View.OnClickListener{
    private TextView tv_name,tv_distance;
    private ListView listView;
    private Button bt_rank;
    private ImageView iv_rank;
    private SimpleAdapter rankAdpter;
    private List<Map<String,Object>> rankdata;
    FragmentActivity rankactivity;


    private int[] rankicon = {R.drawable.number_1,R.drawable.number_2,R.drawable.number_3,
            R.drawable.number_4,R.drawable.number_5};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.rank_fragment,null);
        rankactivity = (FragmentActivity) getActivity();
        Bmob.initialize(rankactivity,"c50c8a2125db58a711b783daba816417");

        tv_name = view.findViewById(R.id.tv_rank_name);
        tv_distance = view.findViewById(R.id.tv_rank_distance);
        bt_rank = view.findViewById(R.id.bt_rank);
        listView = view.findViewById(R.id.list_view);
        iv_rank = view.findViewById(R.id.iv_rank);
        bt_rank.setOnClickListener(this);
        return view;
    }
    private void show(String msg){
        Toast.makeText(rankactivity,msg,Toast.LENGTH_LONG).show();
    }

    public void Query(){
        BmobQuery<MyUser> bmobQuery = new BmobQuery<>();
        bmobQuery.addQueryKeys("mdistance,username");
        bmobQuery.setLimit(5).order("-mdistance");
        rankdata = new ArrayList<>();


        bmobQuery.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e == null){
                    show("刷新排行成功");
                    int i =0;
                    for (MyUser date : list){
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put("image",rankicon[i]);
                        i++;
                        map.put("name",date.getUsername());
                        map.put("distance",date.getMdistance());
                        rankdata.add(map);
                    }
                    String[] from = {"image","name","distance"};
                    int[] to = {R.id.iv_rank,R.id.tv_rank_name,R.id.tv_rank_distance};
                    rankAdpter = new SimpleAdapter(rankactivity,rankdata,R.layout.ranklist_item,from,to);
                    listView.setAdapter(rankAdpter);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_rank:
                Query();
                break;
        }
    }
}
