package com.example.lenovo.angonaa.Activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.angonaa.R;

public class PreferenceActivity2 extends AppCompatActivity {

    public Switch siren_switch, volume_switch, shake_switch;
    public SeekBar sensivity_seekbar;
    public TextView sense_text;
    ActionBar actionBar;
    public static int volume_flag = 0;
    public static int siren_flag =0;
    public static int shake_flag =0;
    public  static int progress_value = 0;
    static int counter = 0;
    MainActivity mainActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference2);

        actionBar = getSupportActionBar();
        actionBar.hide();
        mainActivity = new MainActivity();

        siren_switch = (Switch) findViewById(R.id.playsiren);
        volume_switch= (Switch) findViewById(R.id.usevolumebutton);
        shake_switch = (Switch) findViewById(R.id.shakedetection);
        sense_text = (TextView) findViewById(R.id.sense);
        sensivity_seekbar = (SeekBar) findViewById(R.id.sensivity);

        boolean value = false;
        boolean value1 = false;
        boolean value2 = false;
        int value3 = 0;

        // default value if no value was found

        final SharedPreferences sharedPreferences = getSharedPreferences("isChecked", 0);
        final SharedPreferences sharedPreferences1 = getSharedPreferences("isChecked1", 0);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("isChecked2", 0);
        final SharedPreferences sharedPreferences3 = getSharedPreferences("isChanged", 0);

        value = sharedPreferences.getBoolean("isChecked", value);
        value1 = sharedPreferences1.getBoolean("isChecked1", value1);
        value2 = sharedPreferences2.getBoolean("isChecked2", value2);
        value3 = sharedPreferences3.getInt("isChanged",value3);

        // retrieve the value of your key
        volume_switch.setChecked(value);
        siren_switch.setChecked(value1);
        shake_switch.setChecked(value2);
        sensivity_seekbar.setProgress(value3);

        if(sharedPreferences.getBoolean("isChecked",true))
        {
            volume_flag = 1;
        }
        else
            volume_flag =0;
        if(sharedPreferences1.getBoolean("isChecked1",true))
        {
            siren_flag = 1;
        }
        else
            siren_flag =0;
        if(sharedPreferences2.getBoolean("isChecked2",true))
        {
            shake_flag = 1;
        }
        else
            shake_flag =0;
        progress_value = sharedPreferences3.getInt("isChanged",value3);

        //Setting onClickListener activities for all the options

        siren_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        siren_flag=1;
                        Toast.makeText(PreferenceActivity2.this,"সাইরেন অন",Toast.LENGTH_SHORT).show();
                        sharedPreferences1.edit().putBoolean("isChecked1", true).apply();

                    }
                    else
                    {
                        siren_flag=0;
                        Toast.makeText(PreferenceActivity2.this,"সাইরেন অফ",Toast.LENGTH_SHORT).show();
                        sharedPreferences1.edit().putBoolean("isChecked1",false).apply();

                    }
                    }
                    });

        volume_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    volume_flag=1;
                    Toast.makeText(PreferenceActivity2.this,"ভলিউম বাটন ৩ বার চাপুন",Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putBoolean("isChecked", true).apply();


                }
                else
                {
                    Toast.makeText(PreferenceActivity2.this,"অফ",Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putBoolean("isChecked", false).apply();
                    volume_flag=0;

                }
            }
        });

        shake_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    shake_flag = 1;
                    sharedPreferences2.edit().putBoolean("isChecked2", true).apply();
                    Toast.makeText(PreferenceActivity2.this, "অন", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    shake_flag = 0;
                    Toast.makeText(PreferenceActivity2.this, "অফ", Toast.LENGTH_SHORT).show();
                    sharedPreferences2.edit().putBoolean("isChecked2", false).apply();

                }
            }
            });

            sensivity_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                     sense_text.setText(progress + "/" + sensivity_seekbar.getMax());
                     progress_value = progress;
                     sharedPreferences3.edit().putInt("isChanged",progress).apply();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    progress_value = seekBar.getProgress();

                }
            });

        }
        //Volume button
        public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

                ++counter;
                if(counter == 3&& volume_flag==1){
                    final MediaPlayer mp = MediaPlayer.create(this,R.raw.police_siren2);
                    if(siren_flag==1)
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

                else if(counter == 3&& volume_flag==0){
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
