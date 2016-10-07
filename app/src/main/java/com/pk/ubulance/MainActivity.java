package com.pk.ubulance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Context context;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mainActivity = this;
    }

    public void getCost(View view) {
        Api api  = new Api(context,"38.998725","-76.866995","38.992915","-76.873751",mainActivity);
        api.uberCost();

        Intent intent = new Intent(this,NearestHospital.class);
        startActivity(intent);
    }
}
