package com.example.jamaskari.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by jamaskari on 05.06.2016.
 */
public class About_activity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        TextView txtBox1 = (TextView) findViewById(R.id.textView4);
        try {
            txtBox1.setText(this.getResources().getString(R.string.about));
        } catch (Exception e) {
        }
    }
}
