package com.pk.ubulance.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.pk.ubulance.API.UberAPI;
import com.pk.ubulance.Model.ServerCallBack;
import com.pk.ubulance.R;
import com.pk.ubulance.Service.GetLocation;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DisplayDriverActivity extends AppCompatActivity {

    private String mEndLatitude;
    private String mEndLongitude;
    private String mStartLongitude;
    private String mStartLatitude;

    private Context mContext;
    private CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.driver_name_tv)
    TextView mDriverNameTV;
    @BindView(R.id.driver_rating_tv)
    TextView mDriverRatingTV;
    @BindView(R.id.status_tv)
    TextView mStatusTV;
    @BindView(R.id.eta_tv)
    TextView mEtaTV;
    @BindView(R.id.vehicle_make_tv)
    TextView mVehicleMakeTV;
    @BindView(R.id.vehicle_model_tv)
    TextView mVehicleModelTV;
    @BindView(R.id.vehicle_plate_tv)
    TextView mVehiclePlateTV;
    @BindView(R.id.hospital_address_tv)
    TextView mHostpitalAddressTV;
    @BindView(R.id.driver_phone_tv)
    TextView mDriverPhoneTV;
    @BindView(R.id.toggle_iv)
    ImageView mToggleIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_display_driver);
        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_driver_screen);

//        callIntent = new Intent(Intent.ACTION_CALL);
        mDriverNameTV = ((TextView) findViewById(R.id.driver_name_tv));
        mDriverPhoneTV = ((TextView) findViewById(R.id.driver_phone_tv));
        mDriverRatingTV = ((TextView) findViewById(R.id.driver_rating_tv));
        mStatusTV = ((TextView) findViewById(R.id.status_tv));
        mEtaTV = ((TextView) findViewById(R.id.eta_tv));
        mVehicleMakeTV = ((TextView) findViewById(R.id.vehicle_make_tv));
        mVehicleModelTV = ((TextView) findViewById(R.id.vehicle_model_tv));
        mVehiclePlateTV = ((TextView) findViewById(R.id.vehicle_plate_tv));
        mHostpitalAddressTV = (TextView) findViewById(R.id.hospital_address_tv);
        mToggleIV = ((ImageView) findViewById(R.id.toggle_iv));


        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            mStartLatitude = Double.toString(getLocation.getLatitude());
            mStartLongitude = Double.toString(getLocation.getLongitude());
        } else {
            getLocation.showSettingsAlert();
        }

        Intent intent = this.getIntent();
        Double mEndLatitude = intent.getDoubleExtra("mEndLatitude", 0.0);
        Double mEndLongitude = intent.getDoubleExtra("mEndLongitude", 0.0);
        mHostpitalAddressTV.setText(intent.getStringExtra("vicinity"));

        if (mEndLatitude != 0.0 || mEndLongitude != 0.0) {
            this.mEndLatitude = Double.toString(mEndLatitude);
            this.mEndLongitude = Double.toString(mEndLongitude);
        }

        if (isNetworkAvailable()) {
            UberAPI uberAPI = new UberAPI();
            try {
                uberAPI.callUbulance(this, mStartLatitude, mStartLongitude, this.mEndLatitude, this.mEndLongitude, new ServerCallBack() {
                    @Override
                    public void onSuccess(Object result) throws JSONException {

                        final JSONObject finalResult = (JSONObject) result;

                        String product_id = finalResult.getString("product_id");
                        Log.d("Ubulance", "[FINAL] Product Id: " + product_id);

                        String request_id = finalResult.getString("request_id");
                        Log.d("Ubulance", "[FINAL] Product Id: " + request_id);

                        mStatusTV.setText("Status: " + finalResult.getString("status"));


                        final JSONObject pickup = finalResult.getJSONObject("pickup");
                        mEtaTV.setText("ETA: " + pickup.getString("eta") + "min");

                        final JSONObject driver = finalResult.getJSONObject("driver");
                        mDriverNameTV.setText("Driver: " + driver.getString("name"));
                        mDriverPhoneTV.setVisibility(View.VISIBLE);
                        mDriverPhoneTV.setText("Call: " + driver.getString("phone_number"));

                        final JSONObject vehicle = finalResult.getJSONObject("vehicle");
                        displayDriverImage(mContext, driver.getString("picture_url"));

                        mToggleIV.setOnClickListener(new View.OnClickListener() {
                            Boolean toggle = true;

                            @Override
                            public void onClick(View view) {

                                try {
                                    if (toggle) {
                                        displayDriverImage(mContext, driver.getString("picture_url"));
                                        toggle = false;
                                    } else {
                                        Glide.with(mContext).load(vehicle.getString("picture_url")).into(mToggleIV);
                                        toggle = true;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });


                        mVehicleMakeTV.setText("Make: " + vehicle.getString("make"));
                        mVehicleModelTV.setText("Model: " + vehicle.getString("model"));
                        mVehiclePlateTV.setText("Licence Plate: " + vehicle.getString("license_plate"));
                    }

                    @Override
                    public void onFailure(Boolean bool) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Snackbar.make(mCoordinatorLayout, "No Internet. Please dial 911, if its emergency or try again later", Snackbar.LENGTH_LONG).show();
        }



    }

    public void displayDriverImage(Context mContext, String mUrl) {

        Glide.with(mContext).load(mUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(mToggleIV) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mToggleIV.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Snackbar.make(mCoordinatorLayout, "[Mode: Testing] - You cannot cancel this trip.", Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


