package com.pk.ubulance;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class HospitalAdapter extends ArrayAdapter<Place> {


    public HospitalAdapter(Context context, List<Place> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Place place = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hostpital_list, parent, false);
        }
        // Lookup view for data population
        TextView placeName = (TextView) convertView.findViewById(R.id.placeNameTextView);
        TextView vicinity = (TextView) convertView.findViewById(R.id.vicinityTextView);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        // Populate the data into the template view using the data object
        placeName.setText(place.getName());
        vicinity.setText(place.getVicinity());
        Picasso.with(getContext()).load(place.getIcon()).into(icon);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: send this to uber call
                Log.d("prav:","Lat"+place.getLatitude()+", Log:"+place.getLongitude());
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
