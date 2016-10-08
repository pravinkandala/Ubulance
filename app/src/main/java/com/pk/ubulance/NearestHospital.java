package com.pk.ubulance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


public class NearestHospital extends Activity {

    double longitude;
    double latitude;
    ListView hostpitalListView;
    private SmoothProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_hospital);

        progressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
        hostpitalListView = (ListView) findViewById(R.id.hostpitalList);


        //get current location
        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            longitude = getLocation.getLongitude();
            latitude = getLocation.getLatitude();
        } else {
            getLocation.showSettingsAlert();
        }

        new GetPlaces(this, hostpitalListView).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    class GetPlaces extends AsyncTask<Void, Void, List<Place>> {

        private Context context;
        private ListView listView;

        public GetPlaces(Context context, ListView listView) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listView = listView;
        }


        @Override
        protected void onPostExecute(List<Place> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            HospitalAdapter adapter = new HospitalAdapter(context,result);
            listView.setAdapter(adapter);
            int[] color = {Color.YELLOW,Color.RED,Color.BLUE,Color.GREEN};
            progressBar.setSmoothProgressDrawableColors(color);
            progressBar.progressiveStop();
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.progressiveStart();

        }

        @Override
        protected List<Place> doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesService service = new PlacesService(getString(R.string.googleToken));
            return service.findPlaces(latitude, longitude, "hospital");  // hospital for hospital
        }


    }


}
