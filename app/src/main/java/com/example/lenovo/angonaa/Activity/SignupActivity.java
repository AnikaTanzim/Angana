package com.example.lenovo.angonaa.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.angonaa.HelperClass.Contact;
import com.example.lenovo.angonaa.R;

import com.example.lenovo.angonaa.Database.DatabaseHandler;

public class SignupActivity extends AppCompatActivity {

    ActionBar actionBar;
    Button create;
    DatabaseHandler db1;
    EditText nameEditText,numberEditText,passwordEditText;
    Button createButton;
    String nameString,numberString,passwordString;
    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    static int counter = 0;
    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BF7FBF")));


        db1 = new DatabaseHandler(this);
        nameEditText = (EditText) findViewById(R.id.name_edit);
        numberEditText = (EditText) findViewById(R.id.number_edit);
        passwordEditText = (EditText) findViewById(R.id.password_edit);
        createButton = (Button) findViewById(R.id.create_button);
        //for signing up and checking
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameString = nameEditText.getText().toString();
                numberString = numberEditText.getText().toString();
                passwordString = passwordEditText.getText().toString();
                if(nameString.equals("")||numberString.equals("")||passwordString.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"সবগুলো ঘর পূরণ করুন",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db1.addContact(new Contact(nameString,numberString,passwordString));
                    Toast.makeText(getApplicationContext(),"রেজিস্ট্রেশান সম্পন্ন হয়েছে",Toast.LENGTH_LONG).show();
                    Intent main = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(main);

                    //For two way authentication

                    /*Boolean chknum = db1.chknum(numberString);
                    if (chknum == true)
                    {

                        //Boolean insert  = db.insert(nameString,numberString,passwordString);
                        db1.addContact(new Contact(nameString,numberString,passwordString));
                        Toast.makeText(getApplicationContext(),"রেজিস্ট্রেশান সম্পন্ন হয়েছে",Toast.LENGTH_LONG).show();
                        if (insert == true)
                        {
                            Toast.makeText(getApplicationContext(),"রেজিস্ট্রেশান সম্পন্ন হয়েছে",Toast.LENGTH_LONG).show();
                            Intent main = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(main);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext()," রেজিস্ট্রেশান হয় নি",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"নাম্বারটি পূর্বেই রেজিস্টার্ড",Toast.LENGTH_LONG).show();
                    }*/


                }
            }
        });

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