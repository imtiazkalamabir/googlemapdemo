package com.debzi.googlemapdemo;

import android.Manifest;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.app.Dialog;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private TextView tvInfo, tvInfoHeader, tvSomeInfo;
    private Button moreInfo;
    private String jsonName;
    private String locationId[];
    private String addressArray[];
    private String nameArray[];
    private String newLocationId[];
    private String facilityId[];
    private String newNameArray[];
    private String newaddressArray[];
    private String entranceArray[];
    String s = "";
    String fac_id = "";

    TextView tv_search;
    LinearLayout floatingSearchBar;

    String[] countries;
    private ArrayList<String> mCountries;

    String [] address;
    private ArrayList<String> mAddress;

    Marker markerArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);




        floatingSearchBar = (LinearLayout)findViewById(R.id.floating_search);
        tv_search = (TextView)findViewById(R.id.tv_search);



        floatingSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFloatingBarSearch();
            }
        });

//        ColorDrawable newColor = new ColorDrawable(getResources().getColor(android.R.color.black));//your color from res
//        newColor.setAlpha(50);
//        actionBar.setBackgroundDrawable(newColor);
//        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled( true );
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        loadPositions();
        loadFacility();

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(MapActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                String name = "";
                String loc = marker.getTitle();

                for(int i=0; i<newLocationId.length; i++){



                    if(loc.equals(newLocationId[i])){


                        name = newNameArray[i];

                    }


                }

                TextView title = new TextView(MapActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(name);



//                TextView snippet = new TextView(MapActivity.this);
//                snippet.setTextColor(Color.GRAY);
//                snippet.setText(marker.getTitle());

                info.addView(title);
//                info.addView(snippet);

                return info;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                final String markersnip = marker.getSnippet();
//              Toast.makeText(MapActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();


                final Dialog d = new Dialog(MapActivity.this);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                d.setContentView(R.layout.info_dialog);
                tvInfo = (TextView)d.findViewById(R.id.info);
                tvInfoHeader= (TextView)d.findViewById(R.id.tv_info_title);
                tvSomeInfo = (TextView)d.findViewById(R.id.tv_someinfo);
                moreInfo = (Button)d.findViewById(R.id.more_info);

                fac_id = "";

                int index = 0;

                for (int i=0; i<locationId.length; i++){
                    String loc_id = marker.getTitle().toString();

                    if(loc_id.equals(locationId[i])){

                        index = i;
                    }

                }


                for(int i=0; i<newLocationId.length; i++){

                    String loc_id = marker.getTitle().toString();

                    if(loc_id.equals(newLocationId[i])){

                        tvInfoHeader.setText(newNameArray[i]);
                        tvSomeInfo.setText(addressArray[index]);


                    }


                }


                for(int i=0; i<newLocationId.length; i++){

                    String loc_id = marker.getTitle();

                    if(loc_id.equals(newLocationId[i])){


                        s = facilityId[i];

                    }


                }

                moreInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(MapActivity.this, FacilityActivity.class);
                        i.putExtra("FACILITY_ID", s);
                        startActivity(i);


//                        Toast.makeText(MapActivity.this, markersnip , Toast.LENGTH_SHORT).show();
                    }
                });





                d.show();

                return false;
            }
        });




    }

    public void loadFloatingBarSearch() {


        ArrayList<String> countryStored = SharedPreference.loadList(MapActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES);
        ArrayList<String> addressStored = SharedPreference.loadAddressList(MapActivity.this, Utils.PREFS_ADDRESS, Utils.KEY_ADDRESS);

        View view = MapActivity.this.getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        final ImageView imgToolMic = (ImageView) view.findViewById(R.id.img_tool_mic);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);

        Utils.setListViewHeightBasedOnChildren(listSearch);

        edtToolSearch.setHint("Search Facility...");

        if(!( tv_search.getText().toString().equals("Search Facility... ") )){

            edtToolSearch.setText(tv_search.getText().toString());
        }

        if (!edtToolSearch.getText().toString().equals("")) {
            imgToolMic.setImageDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_close_grey600_24dp));
        }

        final Dialog toolbarSearchDialog = new Dialog(MapActivity.this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(false);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();

        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        countryStored = (countryStored != null && countryStored.size() > 0) ? countryStored : new ArrayList<String>();
        addressStored = (addressStored != null && addressStored.size() > 0) ? addressStored : new ArrayList<String>();
        final SearchAdapter searchAdapter = new SearchAdapter(MapActivity.this, countryStored, addressStored, false);

        listSearch.setVisibility(View.VISIBLE);
        listSearch.setAdapter(searchAdapter);


        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                try {

                TextView nameView = (TextView) view.findViewById(R.id.txt_country);
                TextView addressView = (TextView) view.findViewById(R.id.txt_details);

                String country = nameView.getText().toString();
                String address = addressView.getText().toString();
                SharedPreference.addList(MapActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES, country);
                SharedPreference.addAddressList(MapActivity.this, Utils.PREFS_ADDRESS, Utils.KEY_ADDRESS, address);
                edtToolSearch.setText(country);
                listSearch.setVisibility(View.GONE);
                int idx = 0;
                for(int i =0; i<newaddressArray.length;i++){

                    if(address.equals(newaddressArray[i])){
                        idx = i;
                        break;
                    }
                }
                toolbarSearchDialog.dismiss();
                tv_search.setText(country);

                int newIdx = 0;

                for(int i =0; i<locationId.length; i++){

                    if(newLocationId[idx].equals(locationId[i])){
                        newIdx = i;
                        break;
                    }

                }


                Marker mark = markerArray[newIdx];
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mark.getPosition(), 18));
                mark.showInfoWindow();




                Toast.makeText(MapActivity.this, country+","+address , Toast.LENGTH_SHORT).show();

                }catch (NullPointerException e){

                    Toast.makeText(MapActivity.this, "Loading data. Please wait a moment", Toast.LENGTH_SHORT).show();
                }


            }
        });
        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                try{

                    mCountries = new ArrayList<String>(Arrays.asList(countries));
                    mAddress = new ArrayList<String>(Arrays.asList(address));
                    listSearch.setVisibility(View.VISIBLE);
                    searchAdapter.updateList(mCountries, mAddress, true);
                }catch (NullPointerException e){

                    Toast.makeText(MapActivity.this, "Loading data. Please wait a moment", Toast.LENGTH_SHORT).show();
                }






            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                try{
                ArrayList<String> filterList = new ArrayList<String>();
                ArrayList<String> filterAddressList = new ArrayList<String>();

                if (!edtToolSearch.getText().toString().equals("")) {
                imgToolMic.setImageDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_close_grey600_24dp));
                }

                boolean isNodata = false;
                if (s.length() > 0) {
                    for (int i = 0; i < mCountries.size(); i++) {


                        if (mCountries.get(i).toLowerCase().startsWith(s.toString().trim().toLowerCase())) {

                            filterList.add(mCountries.get(i));
                            filterAddressList.add(mAddress.get(i));

                            listSearch.setVisibility(View.VISIBLE);
                            searchAdapter.updateList(filterList,filterAddressList, true);
                            isNodata = true;
                        }
                    }
                    if (!isNodata) {
                        listSearch.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("No data found");
                    }
                } else {
                    listSearch.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }
                }catch (NullPointerException e){

                    Toast.makeText(MapActivity.this, "Loading data. Please wait a moment", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!edtToolSearch.getText().toString().equals("")) {
                    imgToolMic.setImageDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_close_grey600_24dp));
                }
                else {
                    imgToolMic.setImageDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_microphone_grey600_24dp));
                }

            }
        });



        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtToolSearch.getText().toString().equals("")){
                    tv_search.setText("Search Facility... ");
                }
                toolbarSearchDialog.dismiss();

            }
        });

        imgToolMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtToolSearch.setText("");
                imgToolMic.setImageDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_microphone_grey600_24dp));

            }
        });


    }







    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }



    private void loadPositions() {
        Log.v("deb","load position");
         AsyncHttpClient httpClient=new AsyncHttpClient();
         httpClient.get("http://debalina.bin404.com/location.php",null,new JsonHttpResponseHandler(){

             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONArray positionArray) {
                 super.onSuccess(statusCode, headers, positionArray);
                 Log.v("deb","server suc - "+positionArray.toString());
                 locationId = new String [positionArray.length()];
                 addressArray = new String [positionArray.length()];
                 nameArray = new String[positionArray.length()];
                 markerArray = new Marker[positionArray.length()];
                 try {
                     //JSONArray positionArray=new JSONArray(getResources().getString(R.string.positions));
                     Log.v("deb","json array");
                     for (int i=0;i<positionArray.length();i++){
                         JSONObject positionObj=positionArray.getJSONObject(i);
                         Log.v("deb","json obj "+positionObj.toString());
                         entranceArray = positionObj.getString("entrance").split(",");
                         int idx = 0;
                         for(int m= 0; m<entranceArray.length; m++){

                             if(entranceArray[m].equals("1")){

                                 idx = m;
                                 break;

                             }

                         }



                         if(idx>0) {
                             if (idx == 1) {
                                 idx = idx + 1;
                             } else if (idx == 2) {
                                 idx = idx + 2;
                             } else if (idx ==3){
                                 idx = idx + 3;
                             }
                         }



                         String gpsData[]=positionObj.getString("gps").split(",");
                         locationId[i]=positionObj.getString("location_id");
                         addressArray[i]="House:"+positionObj.getString("house")+", "+"Road:"+positionObj.getString("road")+","+positionObj.getString("area")+", "+positionObj.getString("ward")+", Dhaka - "+positionObj.getString("postal_code");
                         nameArray[i] = positionObj.getString("name");

                             LatLng latLng = new LatLng(Double.parseDouble(gpsData[idx]), Double.parseDouble(gpsData[idx+1]));
                             MarkerOptions markerOptions = new MarkerOptions();
                             markerOptions.position(latLng);
                             markerOptions.title(positionObj.getString("location_id"));
//                             markerOptions.snippet(positionObj.getString("locationd_id"));
                             markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                             mCurrLocationMarker = mMap.addMarker(markerOptions);
                             markerArray[i]=mCurrLocationMarker;
//                             mCurrLocationMarker.setTag(positionObj.getString("locationd_id"));
//                             jsonName = positionObj.getString("name");




                     }
                 } catch (JSONException e) {
                     Log.v("deb","error "+e.getMessage());
                     e.printStackTrace();
                     System.out.println("json error-"+e.getMessage());
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 super.onFailure(statusCode, headers, responseString, throwable);
                 Log.v("deb","server err "+responseString);
             }
         });


    }



    private void loadFacility() {
        Log.v("deb","load position");
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.get("http://debalina.bin404.com/facility.php",null,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray positionArray) {
                super.onSuccess(statusCode, headers, positionArray);
                Log.v("deb","server suc - "+positionArray.toString());
                newLocationId = new String [positionArray.length()];
                facilityId = new String [positionArray.length()];
                newNameArray = new String [positionArray.length()];
                newaddressArray = new String [positionArray.length()];
                try {
                    //JSONArray positionArray=new JSONArray(getResources().getString(R.string.positions));
                    Log.v("deb","json array");
                    for (int i=0;i<positionArray.length();i++){
                        JSONObject positionObj=positionArray.getJSONObject(i);
                        Log.v("deb","json obj "+positionObj.toString());
                        newLocationId[i]=positionObj.getString("location_id");
                        facilityId[i] = positionObj.getString("facility_id");
                        newNameArray[i] = positionObj.getString("name");
                        newaddressArray[i]="House:"+positionObj.getString("house")+", "+"Road:"+positionObj.getString("road")+","+positionObj.getString("area")+", "+positionObj.getString("ward")+", Dhaka - "+positionObj.getString("postal_code");


                    }


                    countries = newNameArray;
                    address = newaddressArray;




                } catch (JSONException e) {
                    Log.v("deb","error "+e.getMessage());
                    e.printStackTrace();
                    System.out.println("json error-"+e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v("deb","server err "+responseString);
            }
        });


    }



//    @Override
//    public boolean onMarkerClick(Marker arg0) {
//        if (arg0.getSnippet() == null) {
//            mMap.moveCamera(CameraUpdateFactory.zoomIn());
//            return true;
//        }
//        arg0.showInfoWindow();
////        final DataClass data = myMapData.get(arg0);
////        final Dialog d = new Dialog(MapActivity.this);
////        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        d.setTitle("Info");
////        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
////        d.setContentView(R.layout.info_dialog);
////        tvInfo= (TextView)d.findViewById(R.id.tv_info);
////
////        String name= arg0.getTitle();
////        tvInfo.setText(name);
//
////        ivPhoto = (ImageView)d.findViewById(R.id.infocontent_iv_image);
////        AddImageOnWindow executeDownload = new AddImageOnWindow();
////        final LatLng l = arg0.getPosition();
////        executeDownload.execute(l);
////        TextView tvName = (TextView)d.findViewById(R.id.infocontent_tv_name);
////        tvName.setText(data.getPlaceName());
////
////        TextView tvType = (TextView)d.findViewById(R.id.infocontent_tv_type);
////        tvType.setText("("+data.getPlaceType()+")");
////
////        TextView tvDesc = (TextView)d.findViewById(R.id.infocontent_tv_desc);
////        tvDesc.setText(data.getPlaceDesc());
////
////        TextView tvAddr = (TextView)d.findViewById(R.id.infocontent_tv_addr);
////        tvAddr.setText(Html.fromHtml(data.getPlaceAddr()));
//
////        d.show();
//        return true;
//    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}