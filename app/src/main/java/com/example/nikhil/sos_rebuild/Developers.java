package com.example.nikhil.sos_rebuild;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Developers extends ActionBarActivity {
    String link;
    private ImageView imageViewRound,imageViewRound1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        imageViewRound=(ImageView)findViewById(R.id.nikhil);
        imageViewRound1=(ImageView)findViewById(R.id.ranjan);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.kristen);

        imageViewRound.setImageBitmap(icon);
        imageViewRound1.setImageBitmap(icon);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    public void onFb1(View v){
        goToUrl("https://www.facebook.com/NikIaMo0");
    }
    public void onFb2(View v){
        goToUrl("https://www.facebook.com/profile.php?id=100005785230434");
    }

    public void onGPlus1(View v){
        goToUrl("https://plus.google.com/u/0/103890811309769017367/posts");

    }
    public void onGPlus2(View v){
        goToUrl("https://plus.google.com/u/0/+RanjanKumar01/posts");

    }

    public void onLinkedin1(View v){
        goToUrl("http://www.pornhub.com");

    }
    public void onLinkedin2(View v){
        goToUrl("https://in.linkedin.com/in/ranjan-kumar-538a66bb");

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
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

