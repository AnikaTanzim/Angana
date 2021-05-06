package com.example.lenovo.angonaa.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.angonaa.HelperClass.Contact;
import com.example.lenovo.angonaa.R;

import com.example.lenovo.angonaa.Database.DatabaseHandler;

import java.util.List;

public class EmergencyListActivity extends AppCompatActivity {

    ImageButton addButton;
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    ActionBar actionBar;
    PreferenceActivity2 pre_obj = new PreferenceActivity2();
    static int counter = 0;
    DatabaseHandler db1;
    ListView listView;
    String deletePosition;
    ArrayAdapter<String> arrayAdapter;
    MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_list);
        db1 = new DatabaseHandler(this);
        addButton = (ImageButton)findViewById(R.id.add);

        listView = (ListView) findViewById(R.id.list);
        //adding contact
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               pickContact();

            }
        });

        actionBar = getSupportActionBar();
        //Changing action bar colour
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BF7FBF")));

        List<String> myContactList=db1.getAllContact();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myContactList);
        mainActivity = new MainActivity();

        listView.setAdapter(arrayAdapter);
        //for deleting selected contact
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteEmergency();
                deletePosition = listView.getItemAtPosition(position).toString();
            }
        });

    }
    //for opening phone contacts
    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_CONTACT_REQUEST) {

            if (resultCode == RESULT_OK)
            {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                //int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String number = cursor.getString(column);
                //String name = cursor.getString(nameIndex);

                db1.addEmergency(new Contact(number));
                finish();
                Intent emergency = new Intent(getApplicationContext(),EmergencyListActivity.class);
                startActivity(emergency);

            }
        }


    }
    //to delete numbers from emergency list
    public void deleteEmergency()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(EmergencyListActivity.this);
        View mView3 = getLayoutInflater().inflate(R.layout.activity_delete_emergency,null);
        mBuilder.setView(mView3);
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                db1.deleteContact(deletePosition);
                finish();
                Intent emergency = new Intent(getApplicationContext(),EmergencyListActivity.class);
                startActivity(emergency);
            }
        });

        mBuilder.setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(950,800);

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

