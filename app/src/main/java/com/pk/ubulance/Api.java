package com.pk.ubulance;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Pravin on 10/6/16.
 * Project: Ubulance
 */

public class Api {

    Context context;
    String start_latitude;
    String start_longitude;
    String end_latitude;
    String end_longitude;
    String first_productID;

    HashMap<String, Integer> uberDurations = new LinkedHashMap<String, Integer>();
    HashMap<String, String> uberEstimates = new LinkedHashMap<String, String>();

    MainActivity mainActivity;

    public Api(Context context, String start_lat, String start_long, String end_lat, String end_long, MainActivity mainActivity) {

        this.context = context;
        this.start_latitude = start_lat;
        this.start_longitude = start_long;
        this.end_latitude = end_lat;
        this.end_longitude = end_long;
        this.mainActivity = mainActivity;
    }

    public void uberCost() {
        //Get request for uber api
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "https://api.uber.com/v1/estimates/price?start_latitude=" + start_latitude + "&start_longitude=" + start_longitude + "&end_latitude=" + end_latitude + "&end_longitude=" + end_longitude;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // display response
                    Log.d("Response", response.toString());
                    parseStoreUberResponse(response);
                },
                error -> Log.d("Error.Response", error.toString())
        ) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token 31-LZaJskTZ94_it8e59TCOsZgbnRhZCGsBqIHvI");
                return headers;
            }
        };

        // add it to the RequestQueue
        queue.add(getRequest);

    }

    public void getProductId() {
        //Get request for uber api


        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "https://api.uber.com/v1/products?latitude="+start_latitude+"&longitude="+start_longitude;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        first_productID = parseUberProductResponse(response);
                        Log.d("getProd", first_productID);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token 31-LZaJskTZ94_it8e59TCOsZgbnRhZCGsBqIHvI");
                return headers;
            }
        };

        // add it to the RequestQueue
        queue.add(getRequest);


    }

    public String getFirst_productID(){
        return first_productID;
    }

    public String parseUberProductResponse(JSONObject response) {
        //parse and store JSON response from the Uber api
        try {
            JSONArray array = response.getJSONArray("products");
            return array.getJSONObject(0).getString("product_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }





    public void parseStoreUberResponse(JSONObject response) {
        //parse and store JSON response from the Uber api
        try {
            JSONArray array = response.getJSONArray("prices");
            for (int i = 0; i < array.length(); i++) {
                Log.d("uberrr" , "Display Name: "+ array.getJSONObject(i).getString("display_name"));
                Log.d("uberrr" , "Duration: "+Math.round(array.getJSONObject(i).getInt("duration") / 60));
                Log.d("uberrr" , "Cost: " + array.getJSONObject(i).getString("estimate"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
