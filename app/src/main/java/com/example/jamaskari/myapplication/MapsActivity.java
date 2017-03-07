package com.example.jamaskari.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {

    private GoogleMap mMap;
    private boolean traceTrigger=false;
    private LatLng fromPosition;
    private  LatLng toLocation;
    private  String retreivedParameter= null;
    private String retreivedValue = null;
    private final int IDD_THREE_BUTTONS = 0;

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        marker.showInfoWindow();
       return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        try{
            mMap.setOnMarkerClickListener((OnMarkerClickListener) this);
        }catch(Exception e){}

        //wrap in async call should be here instead of this
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //wrap in async call should be here instead of this

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            retreivedParameter = extras.getString("parameter");
            retreivedValue = extras.getString("values");
        }

        View s= findViewById(R.id.searchBox);
        s.setVisibility(View.GONE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Spinner spinner = (Spinner) findViewById(R.id.main_menu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.main_menu_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                ((TextView) itemSelected).setText(null);

                switch (selectedItemPosition){
                    case 1:
                        findEO();
                        break;
                    case 2:
                        View s= findViewById(R.id.searchBox);
                        s.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        showHelp();
                        break;
                    case 4:
                        showAbout();
                        break;

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {            }
        });

        final Spinner spinnerA = (Spinner) findViewById(R.id.selector1);
        ArrayAdapter<CharSequence> adapterA = ArrayAdapter.createFromResource(this,
                R.array.selection_a, android.R.layout.simple_spinner_item);
        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerA.setAdapter(adapterA);
        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                ((TextView) itemSelected).setText(null);
                EditText txtBox =  (EditText)findViewById(R.id.editText);
                switch (selectedItemPosition){

                    case 0:

                        if (fromPosition==null)
                        {
                           // Toast.makeText(getApplicationContext(), "Пожалуйста включите геоданные", Toast.LENGTH_LONG).show();
                            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                            if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                    !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                builder.setTitle("Геоданные не включены");
                                builder.setMessage("Подтвердите включение передачи геоданных");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("Отмена",  new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                Dialog alertDialog = builder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }
                        }
                        else {
                            Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geo.getFromLocation(fromPosition.latitude, fromPosition.longitude, 1);
                                txtBox.setText(addresses.get(0).getThoroughfare() + ", " +addresses.get(0).getFeatureName()/*+ ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName()*/);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case 1:
                        if(txtBox.getText() != null)
                        {
                            try {
                                fromPosition = getLocationFromAddress(getApplicationContext(), txtBox.getText().toString());
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(fromPosition).alpha(0.7f));
                            }
                            catch (Exception e)
                            {

                            }

                        }

                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {            }
        });

  /*  DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
    databaseAccess.open();
    List<String> listEO = databaseAccess.getEOnames();
        List<String> listEOaddresses = databaseAccess.getEOaddress();
    databaseAccess.close();

    String[] simpleArray = new String[listEO.size()];
    listEO.toArray(simpleArray);

        final String[] simpleArrayofAddesses = new String[listEOaddresses.size()];
        listEOaddresses.toArray(simpleArrayofAddesses);
*/

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> buildings = databaseAccess.getAllBuildingsFromDb();
        List<String> buildingsAddresses = databaseAccess.getAllBuildingsAddressesFromDb();
        databaseAccess.close();

        String[] simpleArray = new String[buildings.size()];
        buildings.toArray(simpleArray);

        final String[] simpleArrayofAddesses = new String[buildingsAddresses.size()];
        buildingsAddresses.toArray(simpleArrayofAddesses);


    final Spinner spinnerB = (Spinner) findViewById(R.id.selector2);
    ArrayAdapter<String> adapterB = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, simpleArray);
    adapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerB.setAdapter(adapterB);

        spinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                EditText txtBox2 =  (EditText)findViewById(R.id.editText2);
                ((TextView) itemSelected).setText(null);
                txtBox2.setText(simpleArrayofAddesses[selectedItemPosition]);

            }
            public void onNothingSelected(AdapterView<?> parent) {            }
        });

    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();

       mMap.clear();
        View s= findViewById(R.id.searchBox);
        if(s.getVisibility()==View.VISIBLE) s.setVisibility(View.GONE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng vrn = new LatLng(51.6842095, 39.1854093);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vrn));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vrn, 12.0f));
        mMap.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));

        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {

                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Выберите действие")
                        .setPositiveButton("Перейти на сайт вуза", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                 Uri uri = Uri.parse("http://"+marker.getSnippet().toString());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Показать список специальностей", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                showListSpecialities(marker.getSnippet().toString());
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });

        if(retreivedValue!=null) placeMarkersOnMap();
    }

    private void showListSpecialities(String s) {

        DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess1.open();
        int eoid = databaseAccess1.getIdByLink(s);
        List<String> listSpecialities = databaseAccess1.getSpecialitiesById(eoid);
        databaseAccess1.close();

        String listspec="";
        for(int i=0; i<listSpecialities.size(); i = i+3)
        {
            listspec+=listSpecialities.get(i+2);
            listspec+=" ";
            listspec+=listSpecialities.get(i+1);
            listspec+="\n";
        }
        
        new AlertDialog.Builder(MapsActivity.this)
                .setTitle("Список специальностей")
                .setMessage(listspec)
                .setCancelable(true)
                .show();
    }

    public void findEO()
    {
        Intent intent = new Intent(MapsActivity.this,Findeo_activity.class);
        startActivity(intent);
    }
    public void showAbout()
    {
        Intent intent = new Intent(MapsActivity.this,About_activity.class);
        startActivity(intent);
    }
    public void showHelp()
    {
        Intent intent = new Intent(MapsActivity.this,Help_activity.class);
        startActivity(intent);
    }


    public void setRouteToTarget(LatLng a, LatLng b) {
        //String query = new String("http://maps.googleapis.com/maps/api/directions/json?origin=Воронеж&destination=Москва&sensor=false");
        String query = new String("http://maps.googleapis.com/maps/api/directions/json?origin=");
        query += a.latitude;
        query += ",";
        query += a.longitude;
        query += "&destination=";
        query += b.latitude;
        query += ",";
        query += b.longitude;

        RestTemplate rTemplate = new RestTemplate();
        String returnedJson = rTemplate.getForObject(query, String.class);

        try {
            JSONObject jsonRootObject = new JSONObject(returnedJson);
            if (jsonRootObject != null) {
                JSONArray routesArray = jsonRootObject.optJSONArray("routes");
                JSONObject routes = routesArray.getJSONObject(0);
                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                String polylineForRendering = overviewPolylines.getString("points");
                List<LatLng> approxRoute = PolyUtil.decode(polylineForRendering);

                PolylineOptions drawRoute = new PolylineOptions();
                drawRoute.color(Color.BLUE);
                for (LatLng thisOne : approxRoute) {
                    drawRoute.add(thisOne);
                }
                Polyline renderRoute = mMap.addPolyline(drawRoute);
            }
        } catch (JSONException e) {
        }
    }

    public void placeMarkersOnMap()
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        if(retreivedParameter.equals("Название вуза"))
        {
            List<String> list = databaseAccess.getDataByNameEO(retreivedValue);
            LatLng locationTest1 = getLocationFromAddress(getApplicationContext(), list.get(3).toString());
            try {
                mMap.addMarker(new MarkerOptions().position(locationTest1)
                                                  .alpha(0.7f)
                                                  .title(list.get(2).toString()+"\n"+list.get(3).toString())
                                                .snippet(list.get(4).toString())
                                                   );
            }
            catch (Exception e) { }

        }
        if(retreivedParameter.equals("Название специальности"))
        {
            List<String> list = databaseAccess.getEObyspecs(retreivedValue);
            for (int i=0; i< list.size();i=i+2)
            {
                List<String> list1 = databaseAccess.extortEoByItsID(Integer.parseInt(list.get(i)));
                LatLng locationTest1 = getLocationFromAddress(getApplicationContext(), list1.get(3).toString());

                try {
                    mMap.addMarker(new MarkerOptions().position(locationTest1)
                            .alpha(0.7f)
                            .title(list1.get(2).toString()+"\n"+list1.get(3).toString())
                            .snippet(list1.get(4).toString())
                    );
                }
                catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }

        }
        if(retreivedParameter.equals("Шифр специальности"))
        {

            List<String> list = databaseAccess.getEObyspecscodes(retreivedValue);
            for (int i=0; i< list.size();i=i+2)
            {
                List<String> list1 = databaseAccess.extortEoByItsID(Integer.parseInt(list.get(i)));
                LatLng locationTest1 = getLocationFromAddress(getApplicationContext(), list1.get(3).toString());

                try {
                    mMap.addMarker(new MarkerOptions().position(locationTest1)
                            .alpha(0.7f)
                            .title(list1.get(2).toString()+"\n"+list1.get(3).toString())
                            .snippet(list1.get(4).toString())
                    );
                }
                catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }

        }
        databaseAccess.close();
    }

    public void onTraceClick(View view)     {
        try {
            LatLng initialLocation = new LatLng(51.6842095, 39.1854093);
            final Marker myLocation = mMap.addMarker(new MarkerOptions().position(initialLocation).alpha(0.7f));
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {

                    LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                    fromPosition = currentPosition;
                    myLocation.setPosition(currentPosition);

                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && !traceTrigger) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation.getPosition()));

                traceTrigger=true;
            }
            else
            {
                locationManager.removeUpdates(locationListener );
                locationListener= null;
                locationManager=null;
                mMap.clear();
                traceTrigger=false;
            }

        }
        catch(Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
}

    public void drawRouteClick(View view)
    {
        EditText txtBox2 =  (EditText)findViewById(R.id.editText2);
        toLocation = getLocationFromAddress(getApplicationContext(), txtBox2.getText().toString());
        EditText txtBox1 =  (EditText)findViewById(R.id.editText);
        if(txtBox1.getText()!=null && txtBox2.getText()!=null) {
            mMap.addMarker(new MarkerOptions().position(toLocation).alpha(0.7f));
            Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geo.getFromLocation(fromPosition.latitude, fromPosition.longitude, 1);
                txtBox2.setText(addresses.get(0).getThoroughfare() + ", " + addresses.get(0).getFeatureName()/*+ ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName()*/);
            } catch (Exception e) {            }
            setRouteToTarget(fromPosition, toLocation);
            View s = findViewById(R.id.searchBox);
            s.setVisibility(View.GONE);
        }

    }
}
