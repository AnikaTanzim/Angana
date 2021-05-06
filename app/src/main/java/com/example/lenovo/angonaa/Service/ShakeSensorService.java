package com.example.lenovo.angonaa.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.angonaa.Activity.EmergencyListActivity;
import com.example.lenovo.angonaa.Activity.MainActivity;
import com.example.lenovo.angonaa.Activity.PreferenceActivity2;
import com.example.lenovo.angonaa.HelperClass.ShakeDetector;

import com.example.lenovo.angonaa.Database.DatabaseHandler;
import com.example.lenovo.angonaa.R;

import java.util.List;

public class ShakeSensorService extends Service implements LocationListener {

    private final String TAG = this.getClass().getSimpleName();
    private SensorManager mSensorManager;
    private ShakeDetector mShakeDetector;
    PreferenceActivity2 pref_obj;
    MainActivity mainActivity_obj;
    DatabaseHandler db1;

    private LocationManager locationManager;
    private String provider;
    public int lat,lng;
    public String adrss;

    public ShakeSensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        pref_obj = new PreferenceActivity2();
        db1 = new DatabaseHandler(this);
        mainActivity_obj = new MainActivity();
        //get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)

            return;
        }
        android.location.Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Toast.makeText(getApplicationContext(), "Location not available", Toast.LENGTH_SHORT).show();

        }


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final Sensor mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                //Checking for sensitivity and shake count
                if ((count == 1 && pref_obj.progress_value == 1)||(count == 2 && pref_obj.progress_value == 2)
                        ||(count == 3 && pref_obj.progress_value == 3) || (count == 4 && pref_obj.progress_value == 4)
                        ||(count == 5 && pref_obj.progress_value == 5)||(count == 6 && pref_obj.progress_value == 6)
                        ||(count == 7 && pref_obj.progress_value == 7)||(count == 8 && pref_obj.progress_value == 8)
                        ||(count == 9 && pref_obj.progress_value == 9)||(count == 10 && pref_obj.progress_value == 10)) {
                    Log.d(TAG, "Shake Count:" + count);

                    if(pref_obj.shake_flag == 1)
                    {

                        Toast.makeText(getApplicationContext(),"সেকিং",Toast.LENGTH_LONG).show();
                        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.police_siren2);
                        mp.start();
                        sendSMS();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext()," Shake এর প্রেফারেন্স ঠিক করুন ",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mSensorManager.unregisterListener(mShakeDetector);
        super.onDestroy();
    }

    //method for multiple mesaaging
    public void sendSMS() {
        try{
            List<String> myContactList= db1.getAllContact();
            String[] numbers = myContactList.toArray(new String[0]);
            for(String s:numbers){
                //send sms
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(String.valueOf(s), null, "Help! This is an emergency at : "+adrss  , null, null);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Message NOT sent!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        lat = (int) (location.getLatitude());
        lng = (int) (location.getLongitude());
        adrss = db1.chkgps(String.valueOf(lng),String.valueOf(lat));
        Toast.makeText(getApplicationContext(),adrss,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}