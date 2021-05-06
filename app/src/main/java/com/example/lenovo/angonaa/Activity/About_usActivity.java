package com.example.lenovo.angonaa.Activity;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.lenovo.angonaa.Database.DatabaseHandler;
import com.example.lenovo.angonaa.R;

import java.util.List;

public class About_usActivity extends AppCompatActivity {

    ActionBar actionBar;
    static int counter = 0;
    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    MainActivity mainActivity;
    DatabaseHandler db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us2);

        actionBar = getSupportActionBar();
        actionBar.hide();

        db1 = new DatabaseHandler(this);
        mainActivity = new MainActivity();
    }

    //Use of Volume button
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            ++counter;
            if(counter == 3&& pre_obj.volume_flag==1){
                final MediaPlayer mp = MediaPlayer.create(this,R.raw.police_siren2);
                if(pre_obj.siren_flag==1)
                {
                    mp.start();
                    Toast.makeText(getApplicationContext(),"সাইরেন শুরু হবে",Toast.LENGTH_LONG).show();
                    counter = 0;
                    sendSMS();

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

    //For multiple messaging
    public void sendSMS( ) {
        try{
            List<String> myContactList= db1.getAllContact();
            String[] numbers = myContactList.toArray(new String[0]);
            for(String s:numbers){
                //send sms
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(String.valueOf(s), null, "Help! This is an emergency at : "+ mainActivity.adrss  , null, null);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Message NOT sent!",Toast.LENGTH_SHORT).show();
        }

    }

}
