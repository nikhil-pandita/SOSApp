package com.example.nikhil.sos_rebuild;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Demo extends AppCompatActivity {

    TextView guide;
    Main2Activity.Database dabba;
    public SharedPreferences shr;
    ImageView imgview;
    String msgs;
    Cursor crs =null;
    int flg=0;
            String s="Messaage has been sent ......";
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        dabba=new Main2Activity.Database(Demo.this);
        crs = dabba.getAllNumbers();
        shr = PreferenceManager
                .getDefaultSharedPreferences(this);
        guide=(TextView)findViewById(R.id.guide);
        imgview=(ImageView)findViewById(R.id.strt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void onStrtcmd(View v) {
        if(crs==null || crs.getCount()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(Demo.this);
            builder.setTitle("No Contact Selected");
            builder.setMessage("Please select contact to send SMS to");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Demo.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Demo.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            Dialog alertDialog = builder.create();
            alertDialog.show();
        }
        else {
            Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
            new SendSms().execute();
            guide.setText(s);
        }

    }
    public void onStpcmd(View v){

        finish();
    }

    private Runnable sendSms = new Runnable() {
        public void run() {
            System.out.println("inside run");

            crs.moveToFirst();
            if (crs != null && crs.getCount() != 0) {
                if (crs.moveToFirst()) {

                    while (crs.isAfterLast() == false) {
                        String number = crs.getString(crs
                                .getColumnIndex(dabba.COLUMN_NUMBER));
                        msgs = shr.getString("autoSave", "")+"  "+MyService.urli;
                        //sms.sendTextMessage(number, null,msg , null, null);
                        System.out.println("SMS sent to" + number);
                        crs.moveToNext();
                    }
                }
            }
            System.out.println(msgs);

            if (!crs.isClosed()) {
                crs.close();
            }

        }
    };

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

    private class SendSms extends AsyncTask<String, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Demo.this, null,
                    getResources().getText(R.string.sendingsms), true);

        }

        @Override
        protected Void doInBackground(String... params) {
            System.out.println("inside run");
            while(MyService.urli==null){

            }
            dabba=new Main2Activity.Database(Demo.this);
            final Cursor crs = dabba.getAllNumbers();
            crs.moveToFirst();
            if (crs != null && crs.getCount() != 0) {
                if (crs.moveToFirst()) {

                    while (crs.isAfterLast() == false) {
                        String number = crs.getString(crs
                                .getColumnIndex(dabba.COLUMN_NUMBER));
                        msgs = shr.getString("autoSave", "")+"  "+MyService.urli;
                        //sms.sendTextMessage(number, null,msg , null, null);
                        System.out.println("SMS sent to" + number);
                        crs.moveToNext();
                    }
                }
            }
            /*else{
                System.out.println("IS IT WORKING>???");
                 flg=1;
            }*/
            System.out.println(msgs);

            if (!crs.isClosed()) {
                crs.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            progressDialog.dismiss();
            /*if(flg==1){
                AlertDialog.Builder builder = new AlertDialog.Builder(Demo.this);
                builder.setTitle("No Contact Selected");
                builder.setMessage("Please select contact to send SMS to");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Show location settings when the user acknowledges the alert dialog
                        Intent intent = new Intent(Demo.this, Main2Activity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Show location settings when the user acknowledges the alert dialog
                        Intent intent = new Intent(Demo.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                Dialog alertDialog = builder.create();
                alertDialog.show();
                flg=0;
            }*/
            //else {
                Toast.makeText(Demo.this, "Sms Sent", Toast.LENGTH_LONG).show();
            //}

    }
        }
}
