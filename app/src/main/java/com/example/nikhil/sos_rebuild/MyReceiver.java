package com.example.nikhil.sos_rebuild;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by nikhil on 29/6/16.
 */
public class MyReceiver extends BroadcastReceiver {
    static int COUNT = 0;
    int flag=0;
    long t1,t2;
    Context cntx;
    Vibrator vibe;
    Long a,screenon;
    Main2Activity.Database db;
    public  SharedPreferences shrd;
    String msg;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.v("onReceive", "Power button is pressed.");

        shrd = PreferenceManager
                .getDefaultSharedPreferences(context);

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            COUNT++;
            if (COUNT == 1) {
                a = System.currentTimeMillis();
                screenon = a + 5000;
                Toast.makeText(cntx, "POWER BUTTON PRESSED BROO", Toast.LENGTH_LONG).show();
                System.out.println("COUNT IS ONE");
            }


            if (screenon >= System.currentTimeMillis()) {
                if (COUNT >= 3) {
                    t1 = System.currentTimeMillis();
                    t2 = t1 + 1800000;  //1800000  30 min logic ......
                    flag=1;
                    while (System.currentTimeMillis() <= t2) {

                        if (flag == 1) {
                            t1=System.currentTimeMillis()+10000;  //300000   5 min logic....
                            System.out.println("SOS SENT"+t1);


                            SmsManager sms = SmsManager.getDefault();

                    /*for(int i=0;i<ContactPicker.NCount;++i)
                    //System.out.println(ContactPicker.A[i]);*/

                            db = new Main2Activity.Database(context);
                            final Cursor cursor = db.getAllNumbers();
                            cursor.moveToFirst();
                            if (cursor != null && cursor.getCount() != 0) {
                                if (cursor.moveToFirst()) {

                                    while (cursor.isAfterLast() == false) {
                                        String number = cursor.getString(cursor
                                                .getColumnIndex(db.COLUMN_NUMBER));
                                        msg = shrd.getString("autoSave", "")+"  "+MyService.urli;
                                        //sms.sendTextMessage(number, null,msg , null, null);
                                        System.out.println("SMS sent to" + number);
                                        cursor.moveToNext();
                                    }
                                }
                            }
                            System.out.println(msg);

                            if (!cursor.isClosed()) {
                                cursor.close();
                            }
                            flag=0;
                    /*for(int i=0;i<ContactPicker.NCount;i++) {
                        sms.sendTextMessage(ContactPicker.A[i], null, Main3Activity.uri, null, null);
                    }

*/
                        }
                        if(System.currentTimeMillis()==t1)
                        {
                            flag=1;
                        }
                    }
                        COUNT = 0;

                }
                else
                {
                    System.out.println("Sorry, SOS not sent");

                }
            }
            else if(COUNT>=3)
            {
                COUNT = 0;
                System.out.println("LOOP Restarted");
            }
        }
    }

}