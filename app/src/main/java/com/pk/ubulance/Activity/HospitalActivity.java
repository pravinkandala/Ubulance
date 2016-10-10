package com.pk.ubulance.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.pk.ubulance.Model.Place;
import com.pk.ubulance.R;
import com.pk.ubulance.Service.GetLocation;
import com.pk.ubulance.Service.PlacesService;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;


public class HospitalActivity extends AppCompatActivity {

    double mLongitude;
    double mLatitude;
    private SmoothProgressBar mProgressBar;
    CoordinatorLayout mCoordinatorLayout;
    MaterialListView mMaterialListView;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mToolbar.setTitle("Hospitals around you...");

        getSupportActionBar().setTitle("Ubulance");

        mMaterialListView = ((MaterialListView) findViewById(R.id.material_listview));
        mMaterialListView.setItemAnimator(new SlideInDownAnimator());
        mMaterialListView.getItemAnimator().setAddDuration(300);
        mMaterialListView.getItemAnimator().setRemoveDuration(300);

        mCoordinatorLayout = ((CoordinatorLayout) findViewById(R.id.activity_nearest_hospital));
        mProgressBar = (SmoothProgressBar) findViewById(R.id.progressBar);


        //get current location
        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            mLongitude = getLocation.getLongitude();
            mLatitude = getLocation.getLatitude();
        } else {
            getLocation.showSettingsAlert();
        }

        mProgressBar.setVisibility(View.VISIBLE);
        int[] color = {Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN};
        mProgressBar.setSmoothProgressDrawableColors(color);
        mProgressBar.progressiveStart();


        if (isNetworkAvailable()) {
//            mMaterialListView.setVisibility(View.VISIBLE);
            new GetPlaces(this, mMaterialListView).execute();
        } else {
//            mMaterialListView.setVisibility(View.INVISIBLE);
            Snackbar.make(mCoordinatorLayout, "No Network. Emergency ? - Dial 911.", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class GetPlaces extends AsyncTask<Void, Void, List<Place>> {

        private Context mContext;
        private MaterialListView listView;

        public GetPlaces(Context mContext, MaterialListView listView) {
            // TODO Auto-generated constructor stub
            this.mContext = mContext;
            this.listView = listView;
        }


        @Override
        protected void onPostExecute(List<Place> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            List<Card> cards = new ArrayList<>();
//            ImageView imageView = new ImageView(mContext);
            for (int i = 0; i < result.size(); i++) {
//                Glide.with(mContext).load(result.get(i).getIcon()).into(imageView);


                cards.add(new Card.Builder(mContext)
                        .setTag("HOSPITAL_NEAR_YOU")
                        .withProvider(new CardProvider())
                        .setTitle(result.get(i).getName())
                        .setDescription(result.get(i).getVicinity())
                        .setLayout(R.layout.material_small_image_card)
                        .setDrawable(R.drawable.ic_hospital_logo)
                        .endConfig()
                        .build());

            }

            listView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

                @Override
                public void onItemClick(Card card, int position) {
                    Intent intent = new Intent(mContext, DisplayDriverActivity.class);
                    Log.d("Ubulance", "Selected Hospital, Lat: " + result.get(position).getLatitude() + ", Log: " + result.get(position).getLongitude());

                    intent.putExtra("end_latitude", result.get(position).getLatitude());
                    intent.putExtra("end_longitude", result.get(position).getLongitude());
                    intent.putExtra("vicinity", result.get(position).getVicinity());

                    Log.d("Hospital", "Selected Hospital, Vicinity: " + result.get(position).getVicinity());

                    startActivity(intent);

                }

                @Override
                public void onItemLongClick(Card card, int position) {
                    Log.d("LONG_CLICK", card.getTag().toString());
                }
            });


            listView.getAdapter().addAll(cards);

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
