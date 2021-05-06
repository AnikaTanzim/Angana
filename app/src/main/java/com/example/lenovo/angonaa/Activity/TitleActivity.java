package com.example.lenovo.angonaa.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.angonaa.R;

public class TitleActivity extends AppCompatActivity {

    public static int click_flag = 0;
    public Button clickButton;
    ActionBar actionBar;
    public static int counter = 0;
    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        actionBar = getSupportActionBar();
        actionBar.hide();

        //For showing signup only after first install
        //this retrieve the sharedpreference element

        SharedPreferences myPref = this.getSharedPreferences(
                "prefName", Context.MODE_PRIVATE);
        //this retrieve the boolean "firstRun", if it doesn't exists, it places "true"
        boolean firstLaunch = myPref.getBoolean("firstLaunch", true);
        //so, if it's not the first run do stuffs
        if(!firstLaunch){
            //start the next activity
            click_flag = 1;
        }
        else
        {
            //else, if it's the first run, add the sharedPref
            myPref.edit().putBoolean("firstLaunch", false).commit();
            click_flag = 0;
        }

        clickButton = (Button) findViewById(R.id.click);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click_flag == 0)

                {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(TitleActivity.this);
                    View mView1 = getLayoutInflater().inflate(R.layout.activity_disclaimer,null);
                    mBuilder.setView(mView1);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    Button b = mView1.findViewById(R.id.go);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            click_flag = 1;
                            Intent sign = new Intent(TitleActivity.this,SignupActivity.class);
                            startActivity(sign);
                        }
                    });

                }
                if(click_flag == 1)
                {
                    Intent clk = new Intent(TitleActivity.this,MainActivity.class);
                    startActivity(clk);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

    //volume button
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            ++counter;
            if(counter == 3&& pre_obj.volume_flag==1){
                final MediaPlayer mp = MediaPlayer.create(this,R.raw.police_siren2);
                if(pre_obj.siren_flag==1)
                {
                    mp.start();
                    Toast.makeText(getApplicationContext(),"সাইরেন শুরু হবে",Toast.LENGTH_LONG).show();
                    mainActivity.sendSMS();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"সাইরেন এর প্রেফারেন্স ঠিক করুন ",Toast.LENGTH_LONG).show();
                    counter = 0;

                }
            }

            else if(counter == 3&& pre_obj.volume_flag==0){
                Toast.makeText(getApplicationContext(),"ভলুউম এর প্রেফারেন্স ঠিক করুন ",Toast.LENGTH_LONG).show();
                counter=0;
            }

            return true;
        }

        else {
            return super.onKeyDown(keyCode, event);
        }
    }
    public void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

}
