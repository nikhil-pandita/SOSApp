package com.example.nikhil.sos_rebuild;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactPicker extends AppCompatActivity {
    public static final int PICK_CONTACT = 1;
    private Button btnContacts;
    Button Getloc;
    private TextView txtContacts1;
    private TextView txtContacts2;
    public static int NCount = 0;
    public static String[] A= new String[1000];
    public static DB result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        result=new DB(this);
        btnContacts = (Button) findViewById(R.id.btn_contacts);
        txtContacts1 = (TextView) findViewById(R.id.txt_contacts_name);
        txtContacts2 = (TextView) findViewById(R.id.textView);
        Getloc = (Button) findViewById(R.id.Loc);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_CONTACT);
                }
                NCount++;
            }
        });

        Getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactPicker.this, Main3Activity.class);
                startActivity(i);
            }
        });
        final Cursor cursor = result.getAllNumbers();
        cursor.moveToFirst();
        if(cursor.getCount()!=0) {
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {
                    String number = cursor.getString(cursor
                            .getColumnIndex(result.COL_NAME));

                    //sms.sendTextMessage(number, null, Main3Activity.uri, null, null);
                    System.out.println("SMS sent to"+number);
                    cursor.moveToNext();
                }
            }
        }
        System.out.println(Main3Activity.uri);

        if (!cursor.isClosed())
        {
            cursor.close();
        }

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String cNumber = null;
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1"));
                            //System.out.println("number is:" + cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        /*txtContacts1.setText(name );
                        txtContacts2.setText(cNumber);*/

                        if(txtContacts1 != null && txtContacts1.getText().toString().length()==0)
                            txtContacts2.setText(name);
                        else
                        if(txtContacts1 != null) txtContacts1.append("\n"+name);


                            if (txtContacts2 != null && txtContacts2.getText().toString().length() == 0)
                            {
                                txtContacts2.setText(cNumber);
                                if(result.insertNumber(cNumber)){
                                    Toast.makeText(this,"Number has been added",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(this,"Failed To add the Number",Toast.LENGTH_LONG).show();
                                }
                                /*A[NCount-1]=cNumber;
                                System.out.println(A[NCount-1]);*/
                            }
                            else if (txtContacts2 != null)

                            {

                                txtContacts2.append("\n" + cNumber);
                                if(result.insertNumber(cNumber)){
                                    Toast.makeText(this,"Number has been added",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(this,"Failed To add the Number",Toast.LENGTH_LONG).show();
                                }
                                /*A[NCount-1]=cNumber;
                                System.out.println(A[NCount-1]);*/

                            }
                    }
                }
                break;
        }
    }


    public static class DB extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "Phone.db";
        public static final int DATABASE_VERSION = 1;
        public static final String COL_NAME="Numbers";
        public static  final String TABLE_NAME="Contacts";
        public DB(Context context) {
            super(context, DATABASE_NAME , null, DATABASE_VERSION);
        }




        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                    COL_NAME +" TEXT )"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
        public boolean insertNumber(String number) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME, number);
            db.insert(TABLE_NAME, null, contentValues);
            return true;
        }
        public Cursor getAllNumbers() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
            return res;
        }

    }




}
