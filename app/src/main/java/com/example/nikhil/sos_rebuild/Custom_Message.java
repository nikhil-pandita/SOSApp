package com.example.nikhil.sos_rebuild;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Custom_Message extends AppCompatActivity {

    EditText txt;
    public static SharedPreferences prefs;
    public static String msgs = "Help Me!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom__message);
        txt = (EditText) findViewById(R.id.msg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        txt.setText(prefs.getString("autoSave", ""));
    }

    public void onSave(View v) {
        prefs.edit().putString("autoSave", txt.getText().toString()).commit();
        msgs = (prefs.getString("autoSave", ""));
        System.out.println(msgs);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
