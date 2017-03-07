package com.example.jamaskari.myapplication;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Color;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.ToggleButton;

        import com.google.android.gms.games.internal.api.StatsImpl;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;
        import com.google.maps.android.PolyUtil;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import org.json.JSONStringer;
        import org.springframework.web.client.RestTemplate;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Locale;
        import java.util.regex.Pattern;

        import android.os.StrictMode;

/**
 * Created by jamaskari on 03.06.2016.
 */
public class Findeo_activity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.findeo_form);

            final Spinner spinner1 = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.spinnerXarray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);


        /*
                <item>Название вуза</item>
        <item>Название специальности</item>
        <item>Шифр специальности</item>
         */
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

                switch (position) {
                    case 0:
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                            databaseAccess.open();
                            List<String> list = databaseAccess.getEOnames();
                            databaseAccess.close();
                            String[] arrayToSpinner = new String[list.size()];
                            list.toArray(arrayToSpinner);

                            final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Findeo_activity.this, android.R.layout.simple_spinner_item, arrayToSpinner);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(adapter2);
                            break;
                    case 1:
                        DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(getApplicationContext());
                        databaseAccess1.open();
                        List<String> list1 = databaseAccess1.getEOspecialities();
                        databaseAccess1.close();
                        String[] arrayToSpinner1 = new String[list1.size()];
                        list1.toArray(arrayToSpinner1);

                        final Spinner spinner21 = (Spinner) findViewById(R.id.spinner2);
                        ArrayAdapter<String> adapter21 = new ArrayAdapter<String>(Findeo_activity.this, android.R.layout.simple_spinner_item, arrayToSpinner1);
                        adapter21.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner21.setAdapter(adapter21);
                        break;
                    case 2:
                        DatabaseAccess databaseAccess2 = DatabaseAccess.getInstance(getApplicationContext());
                        databaseAccess2.open();
                        List<String> list2 = databaseAccess2.getEOspecialityCodes();
                        databaseAccess2.close();
                        String[] arrayToSpinner2 = new String[list2.size()];
                        list2.toArray(arrayToSpinner2);

                        final Spinner spinner22 = (Spinner) findViewById(R.id.spinner2);
                        ArrayAdapter<String> adapter22 = new ArrayAdapter<String>(Findeo_activity.this, android.R.layout.simple_spinner_item, arrayToSpinner2);
                        adapter22.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner22.setAdapter(adapter22);
                        break;
                    default:
                        break;
                }


            }
            @Override public void onNothingSelected(AdapterView<?> parentView) {  } }
        );


    }

    public void buttonBackClick(View view)
    {
        Intent intent = new Intent(Findeo_activity.this, MapsActivity.class);
        startActivity(intent);

    }

    public void buttonForwardClick(View view)
    {
        //here comes selection from DB and chech if field parameter and value are not empty

        Intent intent = new Intent(Findeo_activity.this, MapsActivity.class);

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        String parameter = spinner1.getSelectedItem().toString();
        String values = spinner2.getSelectedItem().toString();
        intent.putExtra("parameter", parameter);
        intent.putExtra("values",values);

        startActivityForResult(intent, 42);
        finish();
    }


}
