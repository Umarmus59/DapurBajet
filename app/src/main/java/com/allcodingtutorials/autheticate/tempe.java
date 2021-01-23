package com.allcodingtutorials.autheticate;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class tempe extends AppCompatActivity implements SensorEventListener {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor tempsensor;
    private Boolean istempavailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempe);
        textView = findViewById(R.id.tempView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)!=null){
            tempsensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            istempavailable = true;
        }else{
            textView.setText("Temperature sensor is not available on this device");
            istempavailable = false;

        }
    }

    @Override
    public void onSensorChanged(SensorEvent SensorEvent) {
        textView.setText(SensorEvent.values[0]+"C" );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(istempavailable){
            sensorManager.registerListener(this,tempsensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(istempavailable){
            sensorManager.unregisterListener(this);
        }
    }
}