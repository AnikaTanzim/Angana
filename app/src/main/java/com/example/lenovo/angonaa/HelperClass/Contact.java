package com.example.lenovo.angonaa.HelperClass;

/**
 * Created by Lenovo on 7/23/2018.
 */

public class Contact {
    int _id;
    String _name;
    String _phoneNumber;
    String _password;
    String _emergencyNo;

    public Contact()
    {

    }
    public Contact(int Id,String Name ,String ContactNumber,String Password)
    {

        this._id=Id;
        this._name=Name;
        this._phoneNumber=ContactNumber;
        this._password = Password;


    }

    public Contact(String Name,String ContactNumber,String Password)
    {
        this._name=Name;
        this._phoneNumber=ContactNumber;
        this._password = Password;

    }

    public Contact(String emergency_number)
    {
        this._emergencyNo = emergency_number;
    }



    public int getId()
    {
        return this._id;
    }
    public void setId(int Id)

    {
        this._id=Id;
    }

    public String getName()
    {
        return this._name;
    }
    public void setName(String Name)
    {
        this._name=Name;
    }

    public String getContactNumber()
    {
        return this._phoneNumber;
    }
    public void setContactNumber(String ContactNumber)
    {
        this._phoneNumber=ContactNumber;
    }
    public String getPassword()
    {
        return this._password;
    }
    public void setPassword(String Password)
    {
        this._password=Password;
    }
    public String get_emergencyNo()
    {
        return this._emergencyNo;
    }
    public void set_emergencyNo(String EmergencyNumber)
    {
        this._emergencyNo=EmergencyNumber;
    }






}

