package com.example.administrator.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends Activity{
    MapView mMapView = null;
    private BaiduMap mBaidu;
    private Button basics;
    private Button satellite;

    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;

    private boolean isFalsIn = true;
    private double mLatitude;
    private double mLongitude;
    private Button my;
    //自定义图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private  float mCurrentX;//记录当前的位置
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initview();
        //定位的方法
        initLocat();
    }

    private void initLocat(){
        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09||");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        //初始化图标
        mIconLocation= BitmapDescriptorFactory.fromResource(R.drawable.arrow);
        myOrientationListener=new MyOrientationListener(this);
        myOrientationListener.setOnOrienLiser(new MyOrientationListener.OnOrienLiser(){
            @Override
            public void onoriernlister(float x){
                mCurrentX=x;
            }
        });
    }

    private void initview(){
        basics = (Button) findViewById(R.id.Basics);
        satellite = (Button) findViewById(R.id.satellite);
        my = (Button) findViewById(R.id.my);
        dianji();
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaidu = mMapView.getMap();
        //设置地图的间距
        MapStatusUpdate mau = MapStatusUpdateFactory.zoomBy(5.0f);
        mBaidu.setMapStatus(mau);
    }

    public void dianji(){
        basics.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mBaidu.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }
        });
        satellite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mBaidu.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
        });
        my.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dingwei();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //开启定位
        mBaidu.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted()){
            mLocationClient.start();
            //开启方向传感器
            myOrientationListener.start();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        mBaidu.setMyLocationEnabled(false);
        mLocationClient.stop();
        //关闭方向传感
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation){
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentX)
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            mBaidu.setMyLocationData(data);

            MyLocationConfiguration myLocationConfiguration=new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,mIconLocation);
                    mBaidu.setMyLocationConfigeration(myLocationConfiguration);
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
            if(isFalsIn){
                dingwei();
                isFalsIn = false;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i){
        }
    }

    //定位到我的位置
    private void dingwei(){
        LatLng latlng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
        mBaidu.animateMapStatus(msu);
    }
}