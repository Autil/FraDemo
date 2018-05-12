package com.ll.frademo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.ll.frademo.MainActivity;
import com.ll.frademo.R;
import com.ll.frademo.db.RideDataService;
import com.ll.frademo.db.SportDataService;
import com.ll.frademo.entity.Locate;
import com.ll.frademo.entity.SportData;
import com.ll.frademo.event.MyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by l4396 on 2018/4/11.
 */

public class BackService extends Service {
    MyEvent myEvent = new MyEvent();
    private EventBus eventBus;
    @Subscribe
    public void onEvent(MyEvent eventData){
        module = eventData.getModule();

    }

    PowerManager.WakeLock wakeLock = null;
    private final static String TAG = BackService.class.getSimpleName();
    private final static int FOREGROUND_ID = 1000;

    private Date startTime;
    private Date endTime;
    String module;

    private boolean firstloc = true;//第一次定位成功
    private float thisdistance = 0;//此次距离
    private float speed = 0;//GPS返回的速度信息
    private float distance = 0;//运动距离,米
    private float usetime = 0;//用时
    private float ey = 0;

    private LatLng latLng;//一次定位返回的经纬度
    List<LatLng> list = new ArrayList<LatLng>();//保存的经纬度list

    private Locate locate;//定位数据
    List<Locate> locates = new ArrayList<Locate>();//保存Locate
    private boolean quit;
    RideDataService rideDataService;
    SportDataService sportDataService;


    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock(){
        if (null == wakeLock){
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE,"PostlocationService");
            if (null != wakeLock){
                wakeLock.acquire();
            }

        }
    }

    //释放设备电源锁
    private void releaseWakeLock(){
        if (null != wakeLock){
            wakeLock.release();
            wakeLock = null;
        }
    }

    public AMapLocationClient mapLocationClient = null;
    public AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null){
                if (aMapLocation.getErrorCode() == 0){
                    speed = aMapLocation.getSpeed();
                    latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());

                    if (firstloc){
                        distance = 0;
                        firstloc = false;
                        list.add(latLng);

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (!quit){
                                    usetime = (float) (usetime + 0.5);
                                }
                            }
                        },500,500);
                    }else {
                        //计算与上次经纬度的距离，单位米
                        thisdistance = AMapUtils.calculateLineDistance(list.get(list.size() - 1), latLng);


                        distance = distance +thisdistance;//路程计算
                        //保存经纬度信息到list
                        list.add(latLng);
                    }
                    locate = new Locate(aMapLocation.getLatitude(),aMapLocation.getLongitude(),speed);
                    locates.add(locate);

                    myEvent.setModule(module);
                    myEvent.setDistance(distance);
                    myEvent.setList(list);
                    myEvent.setSpeed(speed);
                    eventBus.post(myEvent);
                }else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public AMapLocationClientOption mapLocationClientOption = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "BackService->onStartCommand");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //builder.setSmallIcon(R.mipmap.ico);
        builder.setContentTitle("骑行");
        builder.setContentText("正在记录您的运动数据");
        builder.setWhen(System.currentTimeMillis());
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(FOREGROUND_ID, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        endTime = new Date();
        if(!firstloc){//定位成功过
            rideDataService.saveObject(locates);//保存轨迹数据
            SportData sportData = new SportData(startTime,endTime,usetime,distance,module);//运动数据
            sportDataService.saveObject(sportData);//保存运动数据
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
        this.quit = true;
        mapLocationClient.onDestroy();//销毁定位客户端。
        stopForeground(true);

        //反注册EventBus
        eventBus.unregister(this);
        releaseWakeLock();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        acquireWakeLock();
        //注册EventBus
        eventBus = EventBus.getDefault();
        eventBus.register(this);


        //初始化定位
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mapLocationClient.setLocationListener(mapLocationListener);

        //初始化定位参数
        mapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mapLocationClientOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mapLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mapLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mapLocationClientOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mapLocationClientOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mapLocationClientOption);
        //启动定位
        mapLocationClient.startLocation();

        rideDataService = new RideDataService(this);
        sportDataService = new SportDataService(this);

        startTime = new Date();
    }
}
