package com.example.lenovo.angonaa.Activity;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.lenovo.angonaa.R;

public class DeleteEmergencyActivity extends AppCompatActivity {
    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    static int counter = 0;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_emergency);
        mainActivity = new MainActivity();
    }

    //Volume button
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


}
