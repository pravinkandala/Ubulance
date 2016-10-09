package com.pk.ubulance.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.dd.morphingbutton.MorphingButton;
import com.pk.ubulance.R;

public class MainActivity extends AppCompatActivity {

    Context context;
    MainActivity mainActivity;
    MorphingButton ubulance;
    MorphingButton.Params circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);
        ubulance = (MorphingButton) findViewById(R.id.uberLoginBtn);
        context = this;
        mainActivity = this;

        circle = MorphingButton.Params.create()
                .duration(600)
                .cornerRadius(256) // 56 dp
                .width(256) // 56 dp
                .height(256) // 56 dp
                .colorPressed(Color.WHITE) // pressed state color
                .icon(R.drawable.plus_icon); // icon

    }



    public void goUbulance(View view) {
//        UberAPI api  = new UberAPI(context,"38.998725","-76.866995","38.992915","-76.873751",mainActivity);
//        api.uberCost();

        ubulance.morph(circle);

        Thread loading = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Intent intent = new Intent(MainActivity.this, HospitalAcitivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    finish();
                }
            }
        };
        loading.start();


    }
}
