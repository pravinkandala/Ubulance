package com.pk.ubulance.Activity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.pk.ubulance.Adapter.HospitalAdapter;
import com.pk.ubulance.Model.Place;
import com.pk.ubulance.R;
import com.pk.ubulance.Service.GetLocation;
import com.pk.ubulance.Service.PlacesService;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


public class HospitalActivity extends AppCompatActivity {

    double mLongitude;
    double mLatitude;
    ListView mHospitalListView;
    private SmoothProgressBar mProgressBar;
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        getSupportActionBar().setTitle("Ubulance");
        getSupportActionBar().setSubtitle("Hospitals around");

        mProgressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
        mHospitalListView = (ListView) findViewById(R.id.hostpitalList);
        mCoordinatorLayout = ((CoordinatorLayout) findViewById(R.id.activity_nearest_hospital));


        //get current location
        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            mLongitude = getLocation.getLongitude();
            mLatitude = getLocation.getLatitude();
        } else {
            getLocation.showSettingsAlert();
        }

        mProgressBar.setVisibility(View.VISIBLE);
        int[] color = {Color.YELLOW,Color.RED,Color.BLUE,Color.GREEN};
        mProgressBar.setSmoothProgressDrawableColors(color);
        mProgressBar.progressiveStart();


        if(isNetworkAvailable()) {
            mHospitalListView.setVisibility(View.VISIBLE);
            new GetPlaces(this, mHospitalListView).execute();
        }else{
            mHospitalListView.setVisibility(View.INVISIBLE);
            Snackbar.make(mCoordinatorLayout,"No Network. Emergency ? - Dial 911.",Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class GetPlaces extends AsyncTask<Void, Void, List<Place>> {

        private Context mContext;
        private ListView listView;

        public GetPlaces(Context mContext, ListView listView) {
            // TODO Auto-generated constructor stub
            this.mContext = mContext;
            this.listView = listView;
        }


        @Override
        protected void onPostExecute(List<Place> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            HospitalAdapter adapter = new HospitalAdapter(mContext,result);
            listView.setAdapter(adapter);
            mProgressBar.progressiveStop();
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected List<Place> doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesService service = new PlacesService(getString(R.string.googleToken));
            return service.findPlaces(mLatitude, mLongitude, "hospital");  // hospital for hospital
        }


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
