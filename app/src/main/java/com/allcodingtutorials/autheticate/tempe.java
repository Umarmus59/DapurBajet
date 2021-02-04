package com.allcodingtutorials.autheticate;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(tempe.this);
            builder.setCancelable(true);
            builder.setTitle("Instruction");
            builder.setMessage("To use the temperature sensor, place your phone near the food");
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Toast.makeText(tempe.this, "Goodluck Chef!", Toast.LENGTH_SHORT).show();

                }
            });
            builder.show();

        }else{
            textView.setText("Food temperature:\n Not Available");
            istempavailable = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(tempe.this);
            builder.setCancelable(true);
            builder.setTitle("Alert!");
            builder.setMessage("Temperature sensor is not available on this device");
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Toast.makeText(tempe.this, "Goodluck Chef!", Toast.LENGTH_SHORT).show();

                }
            });
            builder.show();

        }
    }

    @Override
    public void onSensorChanged(SensorEvent SensorEvent) {
        textView.setText("Food Temperature: \n"+SensorEvent.values[0]+"C" );
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