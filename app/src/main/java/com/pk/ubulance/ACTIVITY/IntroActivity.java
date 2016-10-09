package com.pk.ubulance.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pk.ubulance.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimaryDark)
                        .buttonsColor(R.color.colorAccent)
                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION})
                        .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET})
                        .image(R.drawable.ic_ubulance_intro)
                        .title("One touch ride to the hospital")
                        .description("In an emergency, an ambulance may take upwards of 20 minutes to arrive, but there is probably an Uber right around the corner...")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
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

//        Thread loading = new Thread() {
//            public void run() {
//                try {
//                    sleep(SPLASH_DISPLAY_LENGTH);
//                    Intent main = new Intent(IntroActivity.this, MainActivity.class);
//                    startActivity(main);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    finish();
//                }
//            }
//        };
//
//        loading.start();
    }
}
