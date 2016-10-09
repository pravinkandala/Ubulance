package com.pk.ubulance.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pk.ubulance.R;

public class IntroActivity extends Activity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Thread loading = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_DISPLAY_LENGTH);
                    Intent main = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(main);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };

        loading.start();
    }
}
