package com.example.jamaskari.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by jamaskari on 05.06.2016.
 */
public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {

    LayoutInflater mInflater;



    public CustomWindowAdapter(LayoutInflater i){

        mInflater = i;

    }



    // This defines the contents within the info window based on the marker

    @Override

    public View getInfoContents(Marker marker) {

        // Getting view from the layout file

        View v = mInflater.inflate(R.layout.custominfowindow, null);

        // Populate fields

        TextView title = (TextView) v.findViewById(R.id.mainText);

        title.setText(marker.getTitle());



        TextView description = (TextView) v.findViewById(R.id.hyperLink);

        description.setText(marker.getSnippet());;

        // Return info window contents

        return v;

    }



    // This changes the frame of the info window; returning null uses the default frame.

    // This is just the border and arrow surrounding the contents specified above

    @Override

    public View getInfoWindow(Marker marker) {

        return null;

    }

}