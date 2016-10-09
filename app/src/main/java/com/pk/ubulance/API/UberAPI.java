package com.pk.ubulance.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pk.ubulance.Model.ServerCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;


public class UberAPI {


    public void fetchSynchronously(RequestQueue que, String url, int method, JSONObject params, final ServerCallBack callback) {

        String tag = "CALL_UBULANCE";
        Log.d(tag, "Url : " + url);
        Log.d(tag, "Method : " + method);
        if(params != null) Log.d(tag, "Params : " + params.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, "Response : " + response.toString());

                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            Log.d(tag, "JSONException : " + e.getMessage());
                            Log.e(tag, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(tag, "Error : " + error.getMessage());
                        VolleyLog.d(tag, "Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZXMiOlsicmVxdWVzdCJdLCJzdWIiOiI5ZjRmYjZmZS1kYjBkLTRjZTAtOGMzNC1mYmFhZTlhMzczZjYiLCJpc3MiOiJ1YmVyLXVzMSIsImp0aSI6Ijg5MWIzOGE4LTkzMjgtNDI5ZC1iOTNkLWE4ZTJlNjIzYTVmNSIsImV4cCI6MTQ3ODU0MDA4NywiaWF0IjoxNDc1OTQ4MDg3LCJ1YWN0IjoiR1dJb2xBS2VYQ1l4c2FuVVNhdHlORHFVZWpXc2pyIiwibmJmIjoxNDc1OTQ3OTk3LCJhdWQiOiJucFk5UVpkUWN3Y3JwNHVJYXp4dktZY25EbHZjbVhZeSJ9.jWrj8FkSo5nWNPBapXqLzPUMPgWDEwYn86xGQPQuozIPu1RNfE7jnRYLr9jEwpRmVeKuwI8d1eeG2Ci2EWLlC9IxjdQhFXjJgfFHzREv9BrNubVToUVOSOL_ARMeffoB6gB6mYQyDuZL7SulHT1tduBX4NIIuTSBVZ3dUvtMhPEvIZ1H2tOqRMAguSeLWyGl90l5Fd5hSntnPAab-GI6XyoF4OiVKk6fPpl8ejZEcsdTsmRbdhl5UP_Sq-G5il2V_UAvCa-JY-5y52Yy1htunTtnnqOTqHRJ335no36WnE2D3U1HNXXER86GAJZslOAaK3TSqtItNU7YNQ3Nv2jLRw");
                return headers;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.data.length == 0)
                    return Response.success(new JSONObject(), null);
                else
                    return super.parseNetworkResponse(response);
            }
        };

//        NetworkController.getInstance().setRequestContext(c).addToRequestQueue(jsonObjectRequest);
        que.add(jsonObjectRequest);
    }

    public void callUbulance(Context c, String start_latitude, String start_longitude, String end_latitude, String end_longitude, ServerCallBack callBack) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(c);
        final String postUrl = "https://sandbox-api.uber.com/v1/requests";
        final String getUrl = "https://sandbox-api.uber.com/v1/requests/";
        final String putUrl = "https://sandbox-api.uber.com/v1/sandbox/requests/";
        final String[] requestId = new String[1];

        JSONObject postJson = new JSONObject();
        postJson.put("product_id", "dee8691c-8b48-4637-b048-300eee72d58d");
        postJson.put("start_latitude", start_latitude);
        postJson.put("start_longitude", start_longitude);
        postJson.put("end_latitude", end_latitude);
        postJson.put("end_longitude", end_longitude);

        JSONObject putJson = new JSONObject();
        putJson.put("status", "accepted");

        ServerCallBack getDriverDetails = new ServerCallBack() {
            @Override
            public void onSuccess(Object result) throws JSONException {
                final JSONObject finalResult = (JSONObject) result;
                Log.d("CALL_UBULANCE", "[SUCCESS] Final Result : " + finalResult.toString());
                callBack.onSuccess(finalResult);
            }

            @Override
            public void onFailure(Boolean bool) {
                Log.d("CALL_UBULANCE", "[FAILED] getDriverDetails");
            }
        };

        ServerCallBack callGetReq = new ServerCallBack() {
            @Override
            public void onSuccess(Object result) throws JSONException {
                Log.d("CALL_UBULANCE", "Calling get request to get driver details");
                fetchSynchronously(queue, getUrl + requestId[0], GET, null, getDriverDetails);
            }

            @Override
            public void onFailure(Boolean bool) {
                Log.d("CALL_UBULANCE", "[FAILED] callGetReq");
            }
        };

        ServerCallBack getProductIdAndSendPutReq = new ServerCallBack() {
            @Override
            public void onSuccess(Object result) throws JSONException {
                final JSONObject postResponse = (JSONObject) result;
                requestId[0] = ((String) postResponse.get("request_id"));
                Log.d("CALL_UBULANCE", "Received Request Id : " + requestId[0]);
                Log.d("CALL_UBULANCE", "Now Send PUT Request to change the status");
                // send put request
                fetchSynchronously(queue, putUrl + requestId[0], PUT, putJson, callGetReq);
            }

            @Override
            public void onFailure(Boolean bool) {
                Log.d("CALL_UBULANCE", "[FAILED] getProductIdAndSendPutReq");
            }
        };

        fetchSynchronously(queue, postUrl, POST, postJson, getProductIdAndSendPutReq);
    }


}
