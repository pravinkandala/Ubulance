package com.pk.ubulance.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pk.ubulance.API.UberAPI;
import com.pk.ubulance.Model.ServerCallBack;
import com.pk.ubulance.R;
import com.pk.ubulance.Service.GetLocation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayDriverActivity extends AppCompatActivity {

    String end_latitude;
    String end_longitude;
    String start_latitude;
    String start_longitude;
    TextView _driver_name;
    TextView _driver_phone;
    TextView _driver_rating;
    TextView _status;
    TextView _eta;
    TextView _vehicle_make;
    TextView _vehicle_model;
    TextView _vehicle_plate;
    TextView _hostpital_address;
    ImageView _driver_image, _vehicle_image;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_driver);
        context = this;

//        callIntent = new Intent(Intent.ACTION_CALL);
//        _driver_name = ((TextView) findViewById(R.id.driver_name_tv));
        _driver_phone = ((TextView) findViewById(R.id.driver_phone_tv));
//        _driver_rating = ((TextView) findViewById(R.id.driver_rating_tv));
//        _status = ((TextView) findViewById(R.id.status_tv));
//        _eta = ((TextView) findViewById(R.id.eta_tv));
        _vehicle_make = ((TextView) findViewById(R.id.vehicle_make_tv));
        _vehicle_model = ((TextView) findViewById(R.id.vehicle_model_tv));
        _vehicle_plate = ((TextView) findViewById(R.id.vehicle_plate_tv));
        _hostpital_address = (TextView) findViewById(R.id.hospital_address_tv);
        _driver_image = ((ImageView) findViewById(R.id.driver_image));
        _vehicle_image = ((ImageView) findViewById(R.id.vehicle_image));



        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            start_latitude = Double.toString(getLocation.getLatitude());
            start_longitude = Double.toString(getLocation.getLongitude());
        } else {
            getLocation.showSettingsAlert();
        }

        Intent intent = this.getIntent();
        Double end_lat = intent.getDoubleExtra("end_latitude", 0.0);
        Double end_long = intent.getDoubleExtra("end_longitude", 0.0);
        _hostpital_address.setText(intent.getStringExtra("vicinity"));

        if (end_lat != 0.0 || end_long != 0.0) {
            end_latitude = Double.toString(end_lat);
            end_longitude = Double.toString(end_long);
        }

        UberAPI uberAPI = new UberAPI();
        try {
            uberAPI.callUbulance(this, start_latitude, start_longitude, end_latitude, end_longitude, new ServerCallBack() {
                @Override
                public void onSuccess(Object result) throws JSONException {

                    /**
                     *
                     * {
                     "status": "accepted",
                     "product_id": "dee8691c-8b48-4637-b048-300eee72d58d",
                     "destination": {
                     "latitude": 38.993329,
                     "longitude": -76.873783
                     },
                     "driver": {
                     "phone_number": "(555)555-5555",
                     "rating": 4.9,
                     "picture_url": "https://d1a3f4spazzrp4.cloudfront.net/uberex-sandbox/images/driver.jpg",
                     "name": "John",
                     "sms_number": null
                     },
                     "pickup": {
                     "latitude": 38.998725,
                     "eta": 1,
                     "longitude": -76.866995
                     },
                     "request_id": "4d6d3a1f-0a5d-47c9-8f25-c53d69e45cb9",
                     "eta": 1,
                     "location": {
                     "latitude": 39.0004226109,
                     "bearing": -150,
                     "longitude": -76.8657338136
                     },
                     "vehicle": {
                     "make": "Toyota",
                     "picture_url": "https://d1a3f4spazzrp4.cloudfront.net/uberex-sandbox/images/prius.jpg",
                     "model": "Prius",
                     "license_plate": "UBER-PLATE"
                     },
                     "surge_multiplier": 1,
                     "shared": false
                     }
                     *
                     */

                    final JSONObject finalResult = (JSONObject) result;
                    String product_id = finalResult.getString("product_id");
//                    _status.setText("Status: " + finalResult.getString("status"));
                    String request_id = finalResult.getString("request_id");

                    final JSONObject pickup = finalResult.getJSONObject("pickup");
//                    _eta.setText("ETA: " + pickup.getString("eta") + "min");

                    final JSONObject driver = finalResult.getJSONObject("driver");
//                    _driver_name.setText("Driver: " + driver.getString("name"));
                    _driver_phone.setVisibility(View.VISIBLE);
                    _driver_phone.setText("Call: " + driver.getString("phone_number"));

                    Picasso.with(context).load(driver.getString("picture_url")).into(_driver_image);

                    final JSONObject vehicle = finalResult.getJSONObject("vehicle");
                    Picasso.with(context).load(vehicle.getString("picture_url")).into(_vehicle_image);
                    _vehicle_make.setText("Make: " + vehicle.getString("make"));
                    _vehicle_model.setText("Model: " + vehicle.getString("model"));
                    _vehicle_plate.setText("Licence Plate: " + vehicle.getString("license_plate"));
                }

                @Override
                public void onFailure(Boolean bool) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }
}


