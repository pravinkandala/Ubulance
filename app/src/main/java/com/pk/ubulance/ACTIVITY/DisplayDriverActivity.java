package com.pk.ubulance.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.pk.ubulance.API.UberAPI;
import com.pk.ubulance.Model.ServerCallBack;
import com.pk.ubulance.R;
import com.pk.ubulance.Service.GetLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DisplayDriverActivity extends AppCompatActivity {

    private String mEndLatitude;
    private String mEndLongitude;
    private String mStartLongitude;
    private String mStartLatitude;

    private Context mContext;
    private CoordinatorLayout mCoordinatorLayout;

    private Boolean mWorkOnProcess = false;

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
    @BindView(R.id.user_address_tv)
    TextView mUserAddressTV;
    @BindView(R.id.hospital_address_tv)
    TextView mHospitalAddressTV;
    @BindView(R.id.driver_phone_tv)
    TextView mDriverPhoneTV;
    @BindView(R.id.toggle_iv)
    ImageView mToggleIV;
    @BindView(R.id.call_driver_rlayout)
    RelativeLayout mCallDriver;
    @BindView(R.id.show_hospital_location_rlayout)
    RelativeLayout mShowHospitalLocation;
    @BindView(R.id.show_user_location_rlayout)
    RelativeLayout mShowUserLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_driver);
        ButterKnife.bind(this);
        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_driver_screen);

//        callIntent = new Intent(Intent.ACTION_CALL);


        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            mStartLatitude = Double.toString(getLocation.getLatitude());
            mStartLongitude = Double.toString(getLocation.getLongitude());
            mUserAddressTV.setText(getLocationName(mContext, getLocation.getLatitude(), getLocation.getLongitude()));
            mShowUserLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String strUri = "http://maps.google.com/maps?q=loc:" + getLocation.getLatitude() + "," + getLocation.getLongitude() + " (" + "You are here" + ")";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                    startActivity(intent);
                }
            });

        } else {
            getLocation.showSettingsAlert();
        }

        Intent intent = this.getIntent();
        Double mEndLatitude = intent.getDoubleExtra("mEndLatitude", 0.0);
        Double mEndLongitude = intent.getDoubleExtra("mEndLongitude", 0.0);
        mHospitalAddressTV.setText(intent.getStringExtra("vicinity"));
        mShowHospitalLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "http://maps.google.com/maps?q=" + mHospitalAddressTV.getText().toString() + " (" + "Hospital" + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });


        if (mEndLatitude != 0.0 || mEndLongitude != 0.0) {
            this.mEndLatitude = Double.toString(mEndLatitude);
            this.mEndLongitude = Double.toString(mEndLongitude);

        }


        if (isNetworkAvailable()) {
            UberAPI uberAPI = new UberAPI();
            mWorkOnProcess = true;
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
                        mCallDriver.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                try {
                                    intent.setData(Uri.parse("tel:" + driver.getString("phone_number")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);
                            }
                        });

                        mDriverRatingTV.setText("Rating: " + driver.getString("rating"));

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

                        mWorkOnProcess = false;
                    }

                    @Override
                    public void onFailure(Boolean bool) {
                        mWorkOnProcess = false;
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Snackbar.make(mCoordinatorLayout, "No Internet. Please dial 911, if its emergency or try again later", Snackbar.LENGTH_LONG).show();
        }


    }

    public String getLocationName(Context mContext, Double latitude, Double longitude){
        Geocoder myLocation = new Geocoder(mContext, Locale.getDefault());
        List<Address> myList = null;
        try {
            myList = myLocation.getFromLocation(latitude,longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = (Address) myList.get(0);
        String addressStr = "";
        addressStr += address.getAddressLine(0) + ", ";
        addressStr += address.getAddressLine(1) + ", ";
        addressStr += address.getAddressLine(2);
        Log.d("Ubulance", "Your Location: "+addressStr);
        return addressStr;
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
        if(mWorkOnProcess){
            Snackbar.make(mCoordinatorLayout, "Loading components...", Snackbar.LENGTH_LONG).show();
        }else {
            super.onBackPressed();
            finish();
        }
    }
}


