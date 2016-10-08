package com.pk.ubulance;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PUT;

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

//    public Api(Context context, String start_lat, String start_long, String end_lat, String end_long, MainActivity mainActivity) {
//
//        this.context = context;
//        this.start_latitude = start_lat;
//        this.start_longitude = start_long;
//        this.end_latitude = end_lat;
//        this.end_longitude = end_long;
//        this.mainActivity = mainActivity;
//    }

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


    public void fetchSynchronously(Context c, String url, int method, JSONObject params, final ServerCallBack callback) {

        String tag = "pk43";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, response.toString());

                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            Log.e(tag, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(tag, "Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZXMiOlsicmVxdWVzdCJdLCJzdWIiOiI5ZjRmYjZmZS1kYjBkLTRjZTAtOGMzNC1mYmFhZTlhMzczZjYiLCJpc3MiOiJ1YmVyLXVzMSIsImp0aSI6Ijg5MWIzOGE4LTkzMjgtNDI5ZC1iOTNkLWE4ZTJlNjIzYTVmNSIsImV4cCI6MTQ3ODU0MDA4NywiaWF0IjoxNDc1OTQ4MDg3LCJ1YWN0IjoiR1dJb2xBS2VYQ1l4c2FuVVNhdHlORHFVZWpXc2pyIiwibmJmIjoxNDc1OTQ3OTk3LCJhdWQiOiJucFk5UVpkUWN3Y3JwNHVJYXp4dktZY25EbHZjbVhZeSJ9.jWrj8FkSo5nWNPBapXqLzPUMPgWDEwYn86xGQPQuozIPu1RNfE7jnRYLr9jEwpRmVeKuwI8d1eeG2Ci2EWLlC9IxjdQhFXjJgfFHzREv9BrNubVToUVOSOL_ARMeffoB6gB6mYQyDuZL7SulHT1tduBX4NIIuTSBVZ3dUvtMhPEvIZ1H2tOqRMAguSeLWyGl90l5Fd5hSntnPAab-GI6XyoF4OiVKk6fPpl8ejZEcsdTsmRbdhl5UP_Sq-G5il2V_UAvCa-JY-5y52Yy1htunTtnnqOTqHRJ335no36WnE2D3U1HNXXER86GAJZslOAaK3TSqtItNU7YNQ3Nv2jLRw");
                return headers;
            }
        };

        NetworkController.getInstance().setRequestContext(c).addToRequestQueue(jsonObjectRequest);
    }

    public void callUbulance(Context c) throws JSONException {
        final String postUrl = "https://sandbox-api.uber.com/v1/requests";
        final String getUrl = "https://sandbox-api.uber.com/v1/requests/";
        final String putUrl = "https://sandbox-api.uber.com/v1/sandbox/requests/";
        final String[] requestId = new String[1];

        JSONObject postJson = new JSONObject();
        postJson.put("product_id", "dee8691c-8b48-4637-b048-300eee72d58d");
        postJson.put("start_latitude", "38.998725");
        postJson.put("start_longitude", "-76.866995");
        postJson.put("end_latitude", "38.99332899999999");
        postJson.put("end_longitude", "-76.873783");

        JSONObject putJson = new JSONObject();
        putJson.put("status", "accepted");

        fetchSynchronously(c, postUrl, Request.Method.POST, postJson, new ServerCallBack() {
            @Override
            public void onSuccess(Object result) throws JSONException {
                final JSONObject json = (JSONObject) result;
                requestId[0] = ((String) json.get("request_id"));
                Log.d("abhishek", "on post success, reqid:" + requestId[0]);
            }

            @Override
            public void onFailure(Boolean bool) {

            }
        });

        Log.d("abhishek","before put"+requestId[0]);

        fetchSynchronously(c, putUrl + requestId[0], PUT, putJson, new ServerCallBack() {
            @Override
            public void onSuccess(Object result) throws JSONException {
                // do some logging her
                Log.d("abhishek", "on put success");
            }

            @Override
            public void onFailure(Boolean bool) {
                Log.d("abhishek", "on put failed");
            }
        });

        fetchSynchronously(c, getUrl + requestId[0], GET, null, new ServerCallBack() {
            @Override
            public void onSuccess(Object result) throws JSONException {
                // do logging
                JSONObject json = ((JSONObject) result);
                Log.d("driverdetails", json.toString());
            }

            @Override
            public void onFailure(Boolean bool) {
                Log.d("driver", "failed");
            }
        });
    }

    public void getProductId(Context context, String latitude, String longitude, final ServerCallBack callback) {
        //Get request for uber api


        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "https://api.uber.com/v1/products?latitude=" + latitude + "&longitude=" + longitude;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            callback.onSuccess(parseUberProductResponse(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        callback.onFailure(true);
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

    public String getFirst_productID() {
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
                Log.d("uberrr", "Display Name: " + array.getJSONObject(i).getString("display_name"));
                Log.d("uberrr", "Duration: " + Math.round(array.getJSONObject(i).getInt("duration") / 60));
                Log.d("uberrr", "Cost: " + array.getJSONObject(i).getString("estimate"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
