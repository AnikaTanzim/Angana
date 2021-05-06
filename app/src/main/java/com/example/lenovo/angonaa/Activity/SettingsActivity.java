package com.example.lenovo.angonaa.Activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.angonaa.R;

import com.example.lenovo.angonaa.Database.DatabaseHandler;

public class SettingsActivity extends AppCompatActivity {

    ActionBar actionBar;
    public int settings_flag = 0;
    public  static  String newname;
    public  static String pass;
    public  static  String new_number;
    public  static  String new_password;
    static int counter = 0;
    DatabaseHandler db1;
    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BF7FBF")));

        db1 = new DatabaseHandler(this);

    }
    //Method for showing edit text window
    public void popUpEditText() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if(settings_flag == 1)
        {
            alert.setTitle("নতুন নাম");
            alert.setMessage("নতুন নাম লিখুন ");
        }
        if(settings_flag == 2)
        {
            alert.setTitle("নতুন নাম্বার");
            alert.setMessage("নতুন নাম্বার লিখুন");
        }
        if(settings_flag == 3)
        {
            alert.setTitle("নতুন পাসওয়ার্ড");
            alert.setMessage("নতুন পাসওয়ার্ড লিখুন");
        }
        if(settings_flag == 4)
        {
            alert.setTitle("নতুন মেসেজ");
            alert.setMessage("নতুন মেসেজ লিখুন");
        }

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(settings_flag == 1)
                {
                   newname = String.valueOf(input.getText());
                   popUpPassword();
                   Toast.makeText(getApplicationContext(),"Name: "+newname+" ",Toast.LENGTH_SHORT).show();
                }

                if(settings_flag == 2)
                {
                    new_number = String.valueOf(input.getText());
                    popUpPassword();
                    Toast.makeText(getApplicationContext(),"Name: "+new_number+" ",Toast.LENGTH_SHORT).show();
                }

                if(settings_flag == 3)
                {
                    new_password = String.valueOf(input.getText());
                    popUpPassword();
                    Toast.makeText(getApplicationContext(),"Name: "+new_password+" ",Toast.LENGTH_SHORT).show();
                }



            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();
            }
        });

        alert.show();

    }

    //Method for checking correct password and updating the database

    public void popUpPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Password");
        alert.setMessage("Old password");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                pass = String.valueOf(input.getText());

               if(db1.chkpass(pass) == false)
               {
                   if(settings_flag == 1) {

                       boolean nameupdate = db1.name_update(newname, pass);
                       if (nameupdate == true) {
                           Toast.makeText(getApplicationContext(), "updated SUCCESSFULLY", Toast.LENGTH_LONG).show();
                       } else {
                           Toast.makeText(getApplicationContext(), " NOT updated", Toast.LENGTH_LONG).show();
                       }
                   }
                   if(settings_flag == 2) {

                       boolean number_update = db1.number_update(new_number, pass);
                       if (number_update == true) {
                           Toast.makeText(getApplicationContext(), "updated SUCCESSFULLY", Toast.LENGTH_LONG).show();
                       } else {
                           Toast.makeText(getApplicationContext(), " NOT updated", Toast.LENGTH_LONG).show();
                       }
                   }
                   if(settings_flag == 3) {

                       boolean password_update = db1.password_update(new_password, pass);
                       if (password_update == true) {
                           Toast.makeText(getApplicationContext(), "updated SUCCESSFULLY", Toast.LENGTH_LONG).show();
                       } else {
                           Toast.makeText(getApplicationContext(), " NOT updated", Toast.LENGTH_LONG).show();
                       }
                   }

               }
               else
               {
                   Toast.makeText(getApplicationContext(),"Password incorrect",Toast.LENGTH_SHORT).show();
               }


            }
        });


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();
            }
        });

        alert.show();

    }

    public void NameClick(View view) {
        settings_flag = 1;
        popUpEditText();
    }
    public void NumberClick(View view) {
        settings_flag = 2;
        popUpEditText();
    }
    public void PasswordClick(View view) {
        settings_flag = 3;
        popUpEditText();
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
