package com.pk.ubulance.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.pk.ubulance.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SlideFragmentBuilder slideFragmentBuilder = new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimaryDark)
                .buttonsColor(R.color.background);
        if(Build.VERSION.SDK_INT >= 23)
            slideFragmentBuilder
                    .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION})
                    .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET});
        final SlideFragment slider = slideFragmentBuilder
                .image(R.drawable.ic_ubulance_intro)
                .title("One touch ride to the hospital")
                .description("Disclaimer: If it's real emergency, call 911 ")
                .build();
        addSlide(slider, new MessageButtonBehaviour(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Thread loading = new Thread() {
                                    public void run() {
                                        try {
                                            sleep(SPLASH_DISPLAY_LENGTH);
                                            Intent main = new Intent(IntroActivity.this, HospitalActivity.class);
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
                        }, "Book Uber")
        );
    }
}
