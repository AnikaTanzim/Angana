package com.example.lenovo.angonaa.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.angonaa.R;

public class DisclaimerActivity extends AppCompatActivity {
    public Button nextButton;
    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    static int counter = 0;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nextButton = (Button) findViewById(R.id.go);
        //for going to signup activity
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = new Intent(DisclaimerActivity.this, MainActivity.class);
                startActivity(sign);
            }
        });
        mainActivity = new MainActivity();
    }
    //Volume button
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
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
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }


}
