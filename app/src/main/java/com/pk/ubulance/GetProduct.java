package com.pk.ubulance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

public class GetProduct extends AppCompatActivity {

    String end_latitude;
    String end_longitude;
    String start_latitude;
    String start_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_product);

        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            start_latitude = Double.toString(getLocation.getLongitude());
            start_longitude = Double.toString(getLocation.getLatitude());
        } else {
            getLocation.showSettingsAlert();
        }

        Intent intent = this.getIntent();
        Double end_lat = intent.getDoubleExtra("end_latitude", 0.0);
        Double end_long = intent.getDoubleExtra("end_longitude", 0.0);
        if (end_lat != 0.0 || end_long != 0.0) {
            end_latitude = Double.toString(end_lat);
            end_longitude = Double.toString(end_long);
        }

        Api api = new Api();
        try {
            api.callUbulance(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        api.getProductId(this, "38.998725", "-76.866995", new ServerCallBack() {
//            @Override
//            public void onSuccess(Object productID) {
//                Log.d("gotproductback", ((String) productID));
//            }
//
//            @Override
//            public void onFailure(Boolean bool) {
//                if (bool) {
//                    Toast.makeText(getApplicationContext(), "There are some error with the server.\n please call 911", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        if (end_latitude != null && end_longitude != null) {
//
//            try {
//                api.callUber(this, "dee8691c-8b48-4637-b048-300eee72d58d", start_latitude, start_longitude, end_latitude, end_longitude, new ServerCallBack() {
//                    @Override
//                    public void onSuccess(Object result) throws JSONException {
//
//                        JSONObject jsonObject = ((JSONObject) result);
//                        JSONArray array = jsonObject.getJSONArray("prices");
//                        for (int i = 0; i < array.length(); i++) {
//                            Log.d("uberrr" , "Display Name: "+ array.getJSONObject(i).getString("display_name"));
//                            Log.d("uberrr" , "Duration: "+Math.round(array.getJSONObject(i).getInt("duration") / 60));
//                            Log.d("uberrr" , "Cost: " + array.getJSONObject(i).getString("estimate"));
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Boolean bool) {
//
//                    }
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        }
}


