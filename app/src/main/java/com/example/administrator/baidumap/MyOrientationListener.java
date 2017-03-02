package com.example.administrator.baidumap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2017/3/2.
 */

public class MyOrientationListener implements SensorEventListener{
    private Sensor mSemsor;
    private SensorManager mSensorManger; //获得传感器管理器
    private Context mcontext;
    private float lastX;

    public MyOrientationListener(Context context){
        this.mcontext = context;
    }

    public void start(){
        mSensorManger = (SensorManager) mcontext.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManger != null){
            //获得方向传感器
            mSemsor = mSensorManger.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if(mSemsor != null){
            mSensorManger.registerListener(this, mSemsor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        mSensorManger.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION){
            float x = sensorEvent.values[SensorManager.DATA_X];
            if(Math.abs(x-lastX) > 1.0){
                if(onOrienLiser!=null){
                    onOrienLiser.onoriernlister(lastX);
                }
            }
            lastX=x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){
    }

    private OnOrienLiser onOrienLiser;

    public void setOnOrienLiser(OnOrienLiser onOrienLiser){
        this.onOrienLiser = onOrienLiser;
    }

    public interface OnOrienLiser{
        void onoriernlister(float x);
    }
}