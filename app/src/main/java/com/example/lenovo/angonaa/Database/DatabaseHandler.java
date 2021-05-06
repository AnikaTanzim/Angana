package com.example.lenovo.angonaa.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo.angonaa.HelperClass.Contact;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME="contactsManager";
    private static final String TABLE_NAME="contact";
    private static final String KEY_ID="id";
    private static final String KEY_NAME="name";
    private static final String KEY_CONTACTNO="phoneNo";
    private static final String KEY_PASSWORD="password";
    private static final String EMERGENCY_NAME="emergencyName";
    private static final String EMERGENCY_NUMBER="emergencyNumber";
    private static final String KEY_ID1="id1";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //signuptable create
        String CREATE_CONTACT_TABLE="CREATE TABLE "+ TABLE_NAME +"("
                + KEY_ID +" INTEGER PRIMARY KEY,"
                + KEY_NAME +" TEXT,"
                + KEY_CONTACTNO +" TEXT,"
                + KEY_PASSWORD +" TEXT," +")";

        String sql= "CREATE TABLE CONTACT (ID INTEGER PRIMARY KEY," +
                " NAME TEXT" +
                ", PHONENO TEXT" +
                ", PASSWORD TEXT)";
        db.execSQL(sql);

        //emergency table create
        String CREATE_EMERGENCY_TABLE="CREATE TABLE "+ EMERGENCY_NAME +"("
                + KEY_ID1 +" INTEGER PRIMARY KEY,"
                + EMERGENCY_NUMBER+" TEXT," +")";
        String emergency = "CREATE TABLE EMERGENCYNAME (ID1 INTEGER PRIMARY KEY,EMERGENCYNUMBER TEXT)";
        db.execSQL(emergency);
        //gps Table create
        String gpsTable = "CREATE TABLE GPS(ID2 INTEGER PRIMARY KEY,LONGITUDE TEXT,LATITUDE TEXT,ADDRESS TEXT)";
        db.execSQL(gpsTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS CONTACT";
        db.execSQL(sql);

        String sql1 = "DROP TABLE IF EXISTS EMERGENCYNAME";
        db.execSQL(sql1);

        String sql2 = "DROP TABLE IF EXISTS GPS";
        db.execSQL(sql2);

        onCreate(db);

    }

    public void addContact(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO CONTACT(NAME,PHONENO)" +
                "VALUES('"+contact.getName()+"','"+contact.getContactNumber()+"','"+contact.getPassword()+"')";

        //String qry = "INSERT INTO CONTACT(NAME,PHONENO)VALUES('XYZ','016')";
        //db.execSQL(query);

        ContentValues value=new ContentValues();
        value.put(KEY_NAME, contact.getName());
        value.put(KEY_CONTACTNO,contact.getContactNumber());
        value.put(KEY_PASSWORD,contact.getPassword());

        db.insert(TABLE_NAME, null,value);
        db.close();
        /*long ins =db.insert(TABLE_NAME, null,value);
        db.close();
        if (ins == -1)
            return false;
        else return true;*/
    }

    //Method for adding emergency contacts to database

    public void addEmergency(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO EMERGENCYNAME(EMERGENCYNUMBER)" +
                "VALUES('"+contact.get_emergencyNo()+"')";

        //String qry = "INSERT INTO CONTACT(NAME,PHONENO)VALUES('XYZ','016')";
        //db.execSQL(query);

        ContentValues value=new ContentValues();

        value.put("EMERGENCYNUMBER",contact.get_emergencyNo());

        db.insert("EMERGENCYNAME", null,value);
        db.close();
    }

    //Code to get all the items from database (EMERGENCY LIST)

    public List<String> getAllContact()
    {
        List<String> mycontactList=new ArrayList<String>();

        String selectquery="SELECT * FROM EMERGENCYNAME";// where phoneno LIKE '017%'";

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery(selectquery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                String contact = new String(cursor.getString(1));
                mycontactList.add(contact);
            }while(cursor.moveToNext());
        }

        return mycontactList;
    }


    //Code for number and password checking (STOP&EXIT)
    public boolean chknum(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from contact where phoneNo=?", new String[]{number});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    public boolean chkpass(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from contact where password=?", new String[]{password});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    //COde for delete

    public void deleteContact(String number)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete("EMERGENCYNAME", "EMERGENCYNUMBER=?", new String[]{number});
        db.close();
    }

    //code for update

    public boolean name_update( String newname,String pass){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE contact SET name=? WHERE password=?", new String[]{newname,pass});
        return true;

    }

    public boolean number_update( String new_number,String pass){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE contact SET phoneNo=? WHERE password=?", new String[]{new_number,pass});
        return true;

    }

    public boolean password_update( String new_password,String pass){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE contact SET password=? WHERE password=?", new String[]{new_password,pass});
        return true;

    }

    //adding in gps table
    public  void gpsAdd()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("LONGITUDE","90");
        values.put("LATITUDE","23");
        values.put("ADDRESS","Ahsanullah University of Science and Technology");
        values.put("LONGITUDE","70");
        values.put("LATITUDE","10");
        values.put("ADDRESS","Baily Road");
        values.put("LONGITUDE","89");
        values.put("LATITUDE","20");
        values.put("ADDRESS","Bashabo");
        db.insert("GPS", null,values);

        db.close();

    }
    //Method for checking gps and returning location
    public String chkgps(String lat,String lng) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM GPS WHERE LONGITUDE =" +lng+ " AND LATITUDE = " +lat ;
        Cursor cursor = db.rawQuery(query, null);
        String myAddress = null;
        if(cursor.moveToFirst())
        {
            myAddress=cursor.getString(3);
        }

        return myAddress;

    }



}

