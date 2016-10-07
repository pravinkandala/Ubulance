package com.pk.ubulance;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class NearestHospital extends ListActivity {

    double longitude;
    double latitude;
    Activity activity;
    ListView hostpitalListView;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);

        activity = this;

        hostpitalListView = (ListView) findViewById(R.id.hostpitalList);




        GetLocation getLocation = new GetLocation(this);
        if (getLocation.canGetLocation()) {
            longitude = getLocation.getLongitude();
            latitude = getLocation.getLatitude();
            getLocation.showSettingsAlert();
        } else {

        }
        Log.d("current location", "Long:" + longitude + ", Lat:" + latitude);

        new GetPlaces(this, getListView()).execute();

    }




    class GetPlaces extends AsyncTask<Void, Void, List<Place>> {

        private ProgressDialog dialog;
        private Context context;
        public String[] placeName;
        //        public String[] imageUrl;
        private ListView listView;

        public GetPlaces(Context context, ListView listView) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listView = listView;
        }


        @TargetApi(Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(List<Place> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
//            result.forEach(p -> {
//                p.ge
//            });

//            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_nearest_hospital, result);

//            hostpitalListView.setAdapter(adapter);
//            listView.setAdapter(new ArrayAdapter<Place>(context, android.R.layout., result));
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected List<Place> doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesService service = new PlacesService(getString(R.string.googleToken));
            return service.findPlaces(latitude, longitude, "hospital");  // hospital for hospital
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);






    }
}
