package com.example.nikhil.sos_rebuild;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final int PICK_CONTACT = 1;
    public static Database maindb;

    List<String> name1 = new ArrayList<String>();
    List<String> phno1 = new ArrayList<String>();
    public static ListView lv;
    MyAdapter ma;
    Button add, delete;

    StringBuilder checkedcontacts = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        maindb=new Database(this);

        getAllContacts();

        lv = (ListView) findViewById(R.id.lv);
        ma = new MyAdapter();
        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);
        // adding
        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_CONTACT);
                }
            }
        });
            delete.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v) {
                    System.out.println(".............."+ma.mCheckStates.size());
                    for(int i = 0; i < name1.size(); i++)

                    {
                        if(ma.mCheckStates.get(i)==true) {
                            ma.toggle(i);
                            String nm = name1.get(i).toString();
                            String ph = phno1.get(i).toString();
                            if (maindb.deleteNumber(nm, ph)) {
                                Toast.makeText(Main2Activity.this, "Data has been removed", Toast.LENGTH_LONG).show();
                                getAllContacts();
                                System.out.println("Hi, Deletion Confirmed");
                                lv.setAdapter(ma);
                            } else {
                               Toast.makeText(Main2Activity.this, "Failed To remove the Data", Toast.LENGTH_LONG).show();
                            }
                        }

                        else
                        {
                            System.out.println("Not Checked......"+name1.get(i).toString());
                        }


                    }
                }
            });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        ma.toggle(arg2);
    }

    public void getAllContacts() {
        phno1.clear();
        name1.clear();
        final Cursor cursor = maindb.getAllNumbers();
         cursor.moveToFirst();
        if(cursor!=null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndex(maindb.COLUMN_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(maindb.COLUMN_NUMBER));
                    System.out.println(".................." + phoneNumber);
                    if (!name1.contains(name) || !phno1.contains(phoneNumber)) {
                        name1.add(name);
                        phno1.add(phoneNumber);
                    }
                    cursor.moveToNext();
                }

            }
        }

        if (!cursor.isClosed())
        {
            cursor.close();
        }
    }

    class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
        private SparseBooleanArray mCheckStates;
        LayoutInflater mInflater;
        TextView tv1, tv;
        CheckBox cb;

        MyAdapter() {
            mCheckStates = new SparseBooleanArray(name1.size());
            mInflater = (LayoutInflater) Main2Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return name1.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
            if (convertView == null)
                vi = mInflater.inflate(R.layout.row, null);
            tv = (TextView) vi.findViewById(R.id.name);
            tv1 = (TextView) vi.findViewById(R.id.number);
            cb = (CheckBox) vi.findViewById(R.id.checkBox);
            tv.setText("Name :" + name1.get(position));
            tv1.setText("Phone No :" + phno1.get(position));
            cb.setTag(position);
            cb.setChecked(mCheckStates.get(position, false));
            cb.setOnCheckedChangeListener(this);

            return vi;
        }

        public boolean isChecked(int position) {
            return mCheckStates.get(position, false);
        }

        public void setChecked(int position, boolean isChecked) {
            mCheckStates.put(position, isChecked);
            System.out.println("hello...........");
            notifyDataSetChanged();
        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub

            mCheckStates.put((Integer) buttonView.getTag(), isChecked);
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
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (maindb.insertNumber(name,cNumber)) {
                            Toast.makeText(this, "Data has been added", Toast.LENGTH_LONG).show();
                            getAllContacts();
                            lv.setAdapter(ma);
                        } else {
                            Toast.makeText(this, "Data Already Exists", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                break;
        }

    }



    public static class Database extends SQLiteOpenHelper {
        public static final String DATABASE_NN = "Contacts.db";
        public static final int DATABASE_VERSION = 1;
        public static final String COLUMN_NAME="Names";
        public static final String COLUMN_NUMBER="Numbers";
        public static  final String TABLE_NN="Contact";
        public Database(Context context) {
            super(context, DATABASE_NN , null, DATABASE_VERSION);
        }




        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NN + "(" +
                    COLUMN_NAME +" TEXT, " + COLUMN_NUMBER +" TEXT )"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NN);
            onCreate(db);
        }
        public boolean insertNumber(String name,String number) {
            if(!check(name,number)) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME, name);
                contentValues.put(COLUMN_NUMBER, number);
                db.insert(TABLE_NN, null, contentValues);
                return true;
            }
            else
            {
                return false;
            }
        }
        public Cursor getAllNumbers() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NN, null );
            return res;
        }
        public boolean deleteNumber(String name,String number){
            SQLiteDatabase db = getWritableDatabase();
            System.out.println("DELETE FROM "+TABLE_NN+" WHERE "+COLUMN_NAME+"="+"'"+name+"'"+" AND "+COLUMN_NUMBER+"="+"'"+number+"'");
            db.execSQL("DELETE FROM "+TABLE_NN+" WHERE "+COLUMN_NAME+"="+"'"+name+"'"+" AND "+COLUMN_NUMBER+"="+"'"+number+"'");
            return true;
        }
        public boolean check(String name,String number){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NN+" WHERE "+COLUMN_NAME+"="+"'"+name+"'"+" AND "+COLUMN_NUMBER+"="+"'"+number+"'", null );
             if(res.getCount()>0)
                 return true;
            else
                 return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
