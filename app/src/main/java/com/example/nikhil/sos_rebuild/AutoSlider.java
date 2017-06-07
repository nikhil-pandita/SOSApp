package com.example.nikhil.sos_rebuild;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.nikhil.sos_rebuild.R;


import java.util.Timer;
import java.util.TimerTask;

public class AutoSlider extends AppCompatActivity {

        public int currentimageindex=0;
        TimerTask task;
        ImageView slidingimage;

        int[] IMAGE_IDS = {R.drawable.one, R.drawable.two, R.drawable.three,
                R.drawable.four};

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auto_slider);
            slidingimage = (ImageView)findViewById(R.id.imgview);
            final Handler mHandler = new Handler();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            // Create runnable for posting
            final Runnable mUpdateResults = new Runnable() {
                public void run() {

                    AnimateandSlideShow();

                }
            };

            int delay = 1000; // delay for 1 sec.

            int period = 3000; // repeat every 4 sec.

            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {

                    mHandler.post(mUpdateResults);

                }

            }, delay, period);

        }

        public void onClick(View v) {

            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }

      @NonNull
        private void AnimateandSlideShow() {

            slidingimage.setImageResource(IMAGE_IDS[currentimageindex%IMAGE_IDS.length]);

            currentimageindex++;


          Animation rotateimage = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
          slidingimage.startAnimation(rotateimage);
         /* Animation rotateimage1 = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
          slidingimage.startAnimation(rotateimage1);*/


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


