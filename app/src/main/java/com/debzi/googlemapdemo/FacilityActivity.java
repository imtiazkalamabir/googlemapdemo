package com.debzi.googlemapdemo;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FacilityActivity extends AppCompatActivity {


//    String location_id = "1375853984463";

    String facility_id = "";

    String locationId[];

    String facilityId[];

    String nameArray[], cityCorporationArray[], houseArray[], roadArray[], areaArray[], wardArray[], postalCodeArray[], contactPhoneArray[], contactEmailArray[],
            managedByArray[], mngSubArray[], mngSubsubArray[], natureArray[], natureSubtypeArray[], servicePatternArray[];

    TextView tv_name, tv_dhakaNorth, tv_dhakaSouth;

    TextView tv_house, tv_road, tv_area, tv_ward, tv_postal_code;

    TextView tv_phone, tv_email;

    LinearLayout ll_manage;

    TextView tv_public, tv_private, tv_ppp;

    LinearLayout ll_public, ll_private, ll_non_profit;

    TextView public_only, public_auto, for_profit, non_profit, ngo, other_org;

    LinearLayout ll_nature, ll_satellite;

    TextView tv_static, tv_satellite, fixed, mobile;

    TextView tv_weekly, tv_monthly;

    Spinner opSpinner;


    RelativeLayout parent_layout;

    ProgressBar pb;

    ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            facility_id = extras.getString("FACILITY_ID");
            //The key argument here must match that used in the other activity
        }

        parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);

        pb=(ProgressBar) findViewById(R.id.progressBar1);


        parent_layout.setVisibility(View.INVISIBLE);

        tv_name = (TextView)findViewById(R.id.name);

        tv_dhakaNorth = (TextView) findViewById(R.id.dhaka_north);

        tv_dhakaSouth = (TextView) findViewById(R.id.dhaka_south);

        tv_house = (TextView)findViewById(R.id.house);

        tv_road = (TextView)findViewById(R.id.road);

        tv_area = (TextView)findViewById(R.id.area);

        tv_ward = (TextView)findViewById(R.id.ward);

        tv_postal_code = (TextView)findViewById(R.id.postal_code);

        tv_phone = (TextView)findViewById(R.id.phone);

        tv_email = (TextView)findViewById(R.id.email);

        ll_manage = (LinearLayout) findViewById(R.id.ll_manage);

        tv_public = (TextView)findViewById(R.id.tv_public);

        tv_private = (TextView)findViewById(R.id.tv_private);

        tv_ppp = (TextView)findViewById(R.id.tv_ppp);

        public_only = (TextView)findViewById(R.id.public_only);

        public_auto = (TextView)findViewById(R.id.public_auto);



        ll_public = (LinearLayout)findViewById(R.id.ll_public);
        ll_private = (LinearLayout)findViewById(R.id.ll_private);
        ll_non_profit = (LinearLayout)findViewById(R.id.ll_non_profit);


        for_profit = (TextView) findViewById(R.id.for_profit);
        non_profit = (TextView) findViewById(R.id.non_profit);
        ngo = (TextView) findViewById(R.id.ngo);
        other_org = (TextView) findViewById(R.id.other_org);

        ll_nature = (LinearLayout) findViewById(R.id.ll_nature);
        ll_satellite = (LinearLayout) findViewById(R.id.ll_satellite);

        tv_static = (TextView) findViewById(R.id.tv_static);
        tv_satellite = (TextView) findViewById(R.id.tv_satellite);
        fixed = (TextView) findViewById(R.id.fixed);
        mobile = (TextView) findViewById(R.id.mobile);

        tv_weekly = (TextView) findViewById(R.id.tv_weekly);
        tv_monthly = (TextView) findViewById(R.id.tv_monthly);

        opSpinner = (Spinner)findViewById(R.id.op_Spinner);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Facility Info");


        loadFacilityInfo();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_appointment:
                Intent in = new Intent(FacilityActivity.this, AppointmentActivity.class);
                in.putExtra("FACILITY_ID", facility_id);
                startActivity(in);
                finish();
                break;

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_facility:
                Toast.makeText(FacilityActivity.this, "Facility Info", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_specialists:
                Intent i = new Intent(FacilityActivity.this, SpecialistActivity.class);
                i.putExtra("FACILITY_ID", facility_id);
                startActivity(i);
                finish();
                break;

            case R.id.action_list_services:
                Intent a = new Intent(FacilityActivity.this, ListofServicesActivity.class);
                a.putExtra("FACILITY_ID", facility_id);
                startActivity(a);
                finish();
                break;

            case R.id.action_cost_services:
                Intent b = new Intent(FacilityActivity.this, CostofServicesActivity.class);
                b.putExtra("FACILITY_ID", facility_id);
                startActivity(b);
                finish();
                break;

            case R.id.action_provision_poor:
                Intent c = new Intent(FacilityActivity.this, ProvisionActivity.class);
                c.putExtra("FACILITY_ID", facility_id);
                startActivity(c);
                finish();
                break;

            case R.id.action_note:
                Intent d = new Intent(FacilityActivity.this, NoteActivity.class);
                d.putExtra("FACILITY_ID", facility_id);
                startActivity(d);
                finish();
                break;

        }



        return super.onOptionsItemSelected(item);
    }

    private void loadFacilityInfo() {
        Log.v("deb","load position");
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.get("http://debalina.bin404.com/facility.php",null,new JsonHttpResponseHandler(){


            @Override
            public void onStart() {

                pb.setVisibility(View.VISIBLE);
                pb.setMax(100);



                // called before request is started
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {

                super.onProgress(bytesWritten, totalSize);

                int prog = (Math.round((bytesWritten/totalSize)*100));

                pb.setProgress(prog);


            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray facilityArray) {
                super.onSuccess(statusCode, headers, facilityArray);
                Log.v("deb","server suc - "+facilityArray.toString());
                facilityId = new String [facilityArray.length()];
                locationId = new String [facilityArray.length()];

                nameArray = new String [facilityArray.length()];
                cityCorporationArray = new String [facilityArray.length()];
                houseArray = new String [facilityArray.length()];
                roadArray = new String [facilityArray.length()];
                areaArray = new String [facilityArray.length()];
                wardArray = new String [facilityArray.length()];
                postalCodeArray = new String [facilityArray.length()];
                contactPhoneArray = new String [facilityArray.length()];
                contactEmailArray = new String [facilityArray.length()];
                managedByArray = new String [facilityArray.length()];
                mngSubArray = new String [facilityArray.length()];
                mngSubsubArray = new String [facilityArray.length()];
                natureArray = new String [facilityArray.length()];
                natureSubtypeArray = new String [facilityArray.length()];
                servicePatternArray = new String [facilityArray.length()];


                try {

                    Log.v("deb","json array");
                    for (int i=0;i<facilityArray.length();i++){
                        JSONObject facilityObj=facilityArray.getJSONObject(i);
                        Log.v("deb","json obj "+facilityObj.toString());
                        locationId[i]=facilityObj.getString("location_id");
                        facilityId[i] = facilityObj.getString("facility_id");
                        nameArray[i] = facilityObj.getString("name");
                        cityCorporationArray[i] = facilityObj.getString("citycorporation");
                        houseArray[i] = facilityObj.getString("house");
                        areaArray[i] = facilityObj.getString("area");
                        wardArray[i] = facilityObj.getString("ward");
                        postalCodeArray[i] = facilityObj.getString("postal_code");
                        contactPhoneArray[i] = facilityObj.getString("contact_phone");
                        contactEmailArray[i] = facilityObj.getString("contact_email");
                        managedByArray[i] = facilityObj.getString("managed_by");
                        mngSubArray[i] = facilityObj.getString("managed_by_subtype");
                        mngSubsubArray[i] = facilityObj.getString("managed_by_subsubtype");
                        natureArray[i] = facilityObj.getString("nature");
                        natureSubtypeArray[i] = facilityObj.getString("nature_subtype_2");
                        servicePatternArray[i] = facilityObj.getString("service_pattern");



                    }


                    inflateNameSection(facilityId,nameArray,cityCorporationArray);

                    inflateAddressSection(facilityId,houseArray,roadArray,areaArray,wardArray,postalCodeArray);

                    inflateContactSection(facilityId,contactPhoneArray,contactEmailArray);

                    inflateManageSection(facilityId,managedByArray,mngSubArray,mngSubsubArray);

                    inflateNatureSection(facilityId,natureArray,natureSubtypeArray);

                    inflateServicePatternSection(facilityId,servicePatternArray);

                    inflateOpSpinner();
                    opSpinner.setSelection(0);

                    opSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            opSpinner.setSelection(0);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            opSpinner.setSelection(0);
                        }
                    });



                    pb.setVisibility(View.GONE);
                    parent_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(FacilityActivity.this, "Loading Complete!!", Toast.LENGTH_SHORT).show();



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



    public void inflateNameSection(String a[], String b[], String c[]){


        for(int i=0; i<a.length; i++){

            if(facility_id.equals(a[i])){

                tv_name.setText(b[i]);

                if(c[i].equals("91")){

                    tv_dhakaNorth.setBackgroundColor(Color.parseColor("#b4b4b4"));

                }

            }

        }


    }

    public void inflateAddressSection(String a[], String b[], String c[], String d[], String e[],String f[]){


        for(int i=0; i<a.length; i++){

            if(facility_id.equals(a[i])){

                tv_house.setText(b[i]);
                tv_road.setText(c[i]);
                tv_area.setText(d[i]);
                tv_ward.setText(e[i]);
                tv_postal_code.setText(f[i]);

            }

        }

    }

    public void inflateContactSection(String a[], String b[], String c[]){


        for(int i=0; i<a.length; i++){

            if(facility_id.equals(a[i])){

                tv_phone.setText(b[i]);
                tv_email.setText(c[i]);


            }



        }




    }

    public void inflateManageSection(String a[], String b[], String c[], String d[]){


        for(int i=0; i<a.length; i++){

            if(facility_id.equals(a[i])){

                if(b[i].equals("1")){

                    tv_public.setBackgroundColor(Color.parseColor("#b4b4b4"));

                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
                    ll_manage.setWeightSum(3f);
                    ll_manage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
                    ll_private.setVisibility(View.GONE);
                    ll_non_profit.setVisibility(View.GONE);



                    if(c[i].equals("11")){
                        public_only.setBackgroundColor(Color.parseColor("#b4b4b4"));

                    }

                    else if(c[i].equals("12")){

                        public_auto.setBackgroundColor(Color.parseColor("#b4b4b4"));

                    }


                }

                else if(b[i].equals("2")){

                    tv_private.setBackgroundColor(Color.parseColor("#b4b4b4"));
                    ll_public.setVisibility(View.GONE);

                    if(c[i].equals("21")){

                        for_profit.setBackgroundColor(Color.parseColor("#b4b4b4"));
                        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
                        ll_manage.setWeightSum(3f);
                        ll_manage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));


                    }

                    else if(c[i].equals("22")){

                        non_profit.setBackgroundColor(Color.parseColor("#b4b4b4"));
                        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, getResources().getDisplayMetrics());
                        ll_manage.setWeightSum(4f);
                        ll_manage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));


                        if(d[i].equals("221")){


                            ngo.setBackgroundColor(Color.parseColor("#b4b4b4"));


                        }

                        else if(d[i].equals("222")){

                            other_org.setBackgroundColor(Color.parseColor("#b4b4b4"));

                        }



                    }



                }

                else if(b[i].equals("3")){

                    tv_ppp.setBackgroundColor(Color.parseColor("#b4b4b4"));

                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    ll_manage.setWeightSum(2f);
                    ll_manage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
                    ll_public.setVisibility(View.GONE);
                    ll_private.setVisibility(View.GONE);
                    ll_non_profit.setVisibility(View.GONE);



                }




            }



        }



    }

    public void inflateNatureSection(String a[], String b[], String c[]){


        for(int i=0; i<a.length; i++){

            if(facility_id.equals(a[i])){


                if(b[i].equals("1")){

                    tv_static.setBackgroundColor(Color.parseColor("#b4b4b4"));

                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    ll_nature.setWeightSum(2f);
                    ll_nature.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
                    ll_satellite.setVisibility(View.GONE);


                }

                else if(b[i].equals("2")){

                    tv_satellite.setBackgroundColor(Color.parseColor("#b4b4b4"));

                    if(c[i].equals("21")){

                        fixed.setBackgroundColor(Color.parseColor("#b4b4b4"));


                    }

                    else if(c[i].equals("22")){

                        mobile.setBackgroundColor(Color.parseColor("#b4b4b4"));

                    }



                }






            }



        }


    }

    public void inflateServicePatternSection(String a[], String b[]){


        for(int i=0; i<a.length; i++){

            if(facility_id.equals(a[i])){


                if(b[i].equals("1")){

                    tv_weekly.setBackgroundColor(Color.parseColor("#b4b4b4"));

                }

                else if(b[i].equals("2")){

                    tv_monthly.setBackgroundColor(Color.parseColor("#b4b4b4"));
                }




            }



        }


    }


    private void inflateOpSpinner(){


//        final int valueA = a;
//        final int valueB = b;

        List<String> listSize = new ArrayList<String>();
        listSize.add("Operational Status");
        listSize.add("Operational");
        listSize.add("Closed");
        listSize.add("Lincenced");
        listSize.add("Pending Licensing");
        listSize.add("License Suspended");
        listSize.add("License Cancelled");
        listSize.add("Not Licensed");
        listSize.add("Pending Registration");
        listSize.add("Not Registered");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSize){


            @Override
            public boolean isEnabled(int position){



                return false;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
//                if(position==valueA) {
//                    // Set the disable item text color
//                    tv.setTextColor(Color.BLACK);
//                    tv.setBackgroundColor(Color.GRAY);
//                }
//                else if(position ==valueB){
//
//                    tv.setTextColor(Color.BLACK);
//                    tv.setBackgroundColor(Color.GRAY);
//                }
//
//                else if(position ==0){
//
//                    tv.setTextColor(Color.GRAY);
//                    tv.setBackgroundColor(Color.TRANSPARENT);
//                }
//
//                else {
//                    tv.setTextColor(Color.GRAY);
//                }
                return view;
            }





        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opSpinner.setAdapter(dataAdapter);


    }





}
