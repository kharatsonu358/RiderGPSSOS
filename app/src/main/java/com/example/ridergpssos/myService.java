package com.example.ridergpssos;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.ridergpssos.App.CHANNEL_ID;


public class myService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    Float azimut,pitch,roll;
    float[] mGravity =null ;
    float[] mGeomagnetic=null;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private Sensor sensor;

    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;


    @Override
    public void onCreate() {
        Log.d("MyService", "onCreate");
        super.onCreate();
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
     public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("MyService", "onStartCommand");
        String input="Rider Mode ON";
        Intent notificationIntent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Sos Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_baseline_directions_bike_24)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification );


        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        /*NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(MNotIsChecked)
        {

            if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                Intent i = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(i);
            }
            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
        }
        else
            {
                mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }*/







        





        return START_NOT_STICKY;
    }
    @Nullable


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Toast.makeText(getApplicationContext(),"azimathazimu",Toast.LENGTH_SHORT).show();

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if ((mGravity != null) && (mGeomagnetic != null)) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0];
                pitch=orientation[1];
                roll=orientation[2];
                Toast.makeText(getApplicationContext(),"APR"+azimut+pitch+roll,Toast.LENGTH_SHORT).show();




            }
        }
    }



}
