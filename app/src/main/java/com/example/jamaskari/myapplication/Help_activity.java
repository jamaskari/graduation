package com.example.jamaskari.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jamaskari on 05.06.2016.
 */
public class Help_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_layout);
        TextView txtBox1 = (TextView) findViewById(R.id.textView1);
        try {
            txtBox1.setText(this.getResources().getString(R.string.help));
        } catch (Exception e) {
        }

    }
}