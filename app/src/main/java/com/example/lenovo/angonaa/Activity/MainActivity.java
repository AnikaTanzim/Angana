package com.example.lenovo.angonaa.Activity;

//import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.support.v7.app.ActionBar;
import android.widget.EditText;
import android.widget.Toast;
import com.example.lenovo.angonaa.R;
import com.example.lenovo.angonaa.Service.ShakeSensorService;

import com.example.lenovo.angonaa.Database.DatabaseHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener {

    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    public Button tipsButton, emergencyButton, alarmButton, gpsButton;
    ActionBar actionBar;
    static int counter = 0;
    DatabaseHandler db1;
    EditText password_editText;
    private LocationManager locationManager;
    private String provider;
    public int lat, lng;
    public String adrss;

    private FusedLocationProviderClient mFusedLocationClient;


    public void init() {

        //goes to tipsButtonActivity
        tipsButton = (Button) findViewById(R.id.tips);
        tipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tps = new Intent(MainActivity.this, TipsActivityActivity.class);
                startActivity(tps);
            }
        });

        //goes to emergencyButtonActivity
        emergencyButton = (Button) findViewById(R.id.emergency_list);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eme = new Intent(MainActivity.this, EmergencyListActivity.class);
                startActivity(eme);

            }
        });

        //handles map button
        gpsButton = (Button) findViewById(R.id.gps);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 23.8103, 90.4125);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });


    }

    protected void onResume() {
        super.onResume();
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.police_siren2);
        alarmButton = (Button) findViewById(R.id.siren);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startService(new Intent(getApplicationContext(), ShakeSensorService.class));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BF7FBF")));

        db1 = new DatabaseHandler(this);
        db1.gpsAdd();
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            double lastlat = location.getLatitude();
                            double lastlong = location.getLongitude();
                            Toast.makeText(getApplicationContext(), "The lat:" + lastlat + "The long:" + lastlong + "got it", Toast.LENGTH_SHORT).show();
                            getAddress(getApplicationContext(),lastlat,lastlong);

                        } else {
                            Toast.makeText(getApplicationContext(), "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
        Intent title = new Intent(getApplicationContext(), TitleActivity.class);
        startActivity(title);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.stop_exit) {
            //opens dialogbox for stop&exit
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            View mView2 = getLayoutInflater().inflate(R.layout.activity_stopandexit, null);
            mBuilder.setView(mView2);

            password_editText = (EditText) mView2.findViewById(R.id.s2);
            mBuilder.setCancelable(true);
            mBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //For checking correct password to stop
                    String s = password_editText.getText().toString();
                    if (s.equals("")) {
                        Toast.makeText(getApplicationContext(), "পাসওয়ার্ড ফিল্ডটি খালি", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean chkpass = db1.chkpass(s);
                        if (chkpass == true) {
                            Toast.makeText(getApplicationContext(), "সঠিক পাসওয়ার্ড দিন", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), " পাসওয়ার্ড সঠিক", Toast.LENGTH_SHORT).show();
                            System.exit(0);
                            finish();
                        }
                    }


                }
            });
            AlertDialog dialog = mBuilder.create();
            dialog.show();
            dialog.getWindow().setLayout(950, 900);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            // Handle the settings action
            Intent set = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(set);
        } else if (id == R.id.preferences) {
            //Handle preference action
            Intent pref = new Intent(MainActivity.this, PreferenceActivity2.class);
            startActivity(pref);

        } else if (id == R.id.about_us) {
            //Handle about us action
            Intent inf = new Intent(MainActivity.this, About_usActivity.class);
            startActivity(inf);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Location accessing
    @Override
    public void onLocationChanged(android.location.Location location) {
        lat = (int) (location.getLatitude());
        lng = (int) (location.getLongitude());
        adrss = db1.chkgps(String.valueOf(lng), String.valueOf(lat));
        // Toast.makeText(getApplicationContext(),adrss,Toast.LENGTH_SHORT).show();

    }

    //Method for multiple messaging
    public void sendSMS() {
        try {
            List<String> myContactList = db1.getAllContact();
            String[] numbers = myContactList.toArray(new String[0]);
            for (String s : numbers) {
                //send sms
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(String.valueOf(s), null, "Help! This is an emergency at : " + adrss, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Message NOT sent!", Toast.LENGTH_SHORT).show();
        }

    }

    //volume button
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            ++counter;
            if (counter == 3 && pre_obj.volume_flag == 1) {
                final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.police_siren2);
                if (pre_obj.siren_flag == 1) {
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "সাইরেন শুরু হবে", Toast.LENGTH_LONG).show();
                    counter = 0;
                    sendSMS();

                } else {
                    Toast.makeText(getApplicationContext(), "সাইরেন এর প্রেফারেন্স ঠিক করুন ", Toast.LENGTH_LONG).show();
                    counter = 0;

                }

            } else if (counter == 3 && pre_obj.volume_flag == 0) {
                Toast.makeText(getApplicationContext(), "ভলুউম এর প্রেফারেন্স ঠিক করুন ", Toast.LENGTH_LONG).show();
                counter = 0;

            }

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                String fullAddress = address+ ", "+city+", "+state+", "+country+", "+postalCode+", "+knownName+" ";
                Toast.makeText(this, "Adress is:  "+fullAddress+" ", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}



