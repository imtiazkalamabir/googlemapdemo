package com.debzi.googlemapdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class AppointmentActivity extends AppCompatActivity {


   // List<Facility> facilityList;
    Button sendAppointBtn,dateSelectBtn;
    EditText patientName,patientPhone;
    String selectedDate;
    TextView tvDate;

    String facility_id = "";

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            facility_id = extras.getString("FACILITY_ID");
            //The key argument here must match that used in the other activity
        }

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Ask for Appointment ");

        Toast.makeText(AppointmentActivity.this, "Loading Complete!!", Toast.LENGTH_SHORT).show();



        sendAppointBtn= (Button) findViewById(R.id.sendAppointmentBtn);
        dateSelectBtn= (Button) findViewById(R.id.apoointDateBtn);
        patientName= (EditText) findViewById(R.id.patientNameEt);
        patientPhone= (EditText) findViewById(R.id.patientPhoneEt);
        tvDate = (TextView)findViewById(R.id.tv_date);

        final String facilityId=getIntent().getStringExtra("FACILITY_ID");

        //http://debalina.bin404.com/set_appoint.php
        //fac_id
        //patient_name
        //phonenumber
        //appDate//2016-11-15

        dateSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDialog();
            }
        });
        sendAppointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dateSelectBtn==null){
                    Toast.makeText(AppointmentActivity.this,"Please select Date",Toast.LENGTH_LONG).show();
                    return;
                }
                sendAppointment(facilityId,patientName.getText().toString(),patientPhone.getText().toString(),selectedDate);
            }
        });

        //facilitySpinner= (Spinner) findViewById(R.id.facilityListSpinner);
        //pullAllFacility();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_facility:
                Intent ii = new Intent(AppointmentActivity.this, FacilityActivity.class);
                ii.putExtra("FACILITY_ID", facility_id);
                startActivity(ii);
                finish();
                break;

            case R.id.action_specialists:
                Intent i = new Intent(AppointmentActivity.this, SpecialistActivity.class);
                i.putExtra("FACILITY_ID", facility_id);
                startActivity(i);
                finish();
                break;

            case R.id.action_list_services:
                Intent a = new Intent(AppointmentActivity.this, ListofServicesActivity.class);
                a.putExtra("FACILITY_ID", facility_id);
                startActivity(a);
                finish();
                break;

            case R.id.action_cost_services:
                Intent b = new Intent(AppointmentActivity.this, CostofServicesActivity.class);
                b.putExtra("FACILITY_ID", facility_id);
                startActivity(b);
                finish();
                break;

            case R.id.action_provision_poor:
                Intent c = new Intent(AppointmentActivity.this, ProvisionActivity.class);
                c.putExtra("FACILITY_ID", facility_id);
                startActivity(c);
                finish();
                break;

            case R.id.action_note:
                Intent d = new Intent(AppointmentActivity.this, NoteActivity.class);
                d.putExtra("FACILITY_ID", facility_id);
                startActivity(d);
                finish();
                break;
            case R.id.action_appointment:
                Toast.makeText(this, "Ask for appointment", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendAppointment(String facilityId, String patientName, String phone, String selectedDate) {

        RequestParams params=new RequestParams();
        params.add("fac_id",facilityId);
        params.add("patient_name",patientName);
        params.add("phonenumber",phone);
        params.add("appDate",selectedDate);
        params.add("fac_id",facilityId);

        AsyncHttpClient httpClient=new AsyncHttpClient();
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Sending Appointment. Please wait...");
        httpClient.post("http://debalina.bin404.com/set_appoint.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(AppointmentActivity.this,new String(responseBody) ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(AppointmentActivity.this,"Error "+statusCode+" "+new String(responseBody) ,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePickDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate=year+"-"+monthOfYear+"-"+dayOfMonth;
                        tvDate.setText(dayOfMonth+"/"+monthOfYear+"/"+year);

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(),"Select Date");
    }

//
//    public void pullAllFacility(){
//        AsyncHttpClient httpClient=new AsyncHttpClient();
//        final ProgressDialog dialog=new ProgressDialog(this);
//        dialog.setIndeterminate(true);
//        dialog.setMessage("Loading. Please wait...");
//        httpClient.get("http://debalina.bin404.com/facility.php",null,new JsonHttpResponseHandler(){
//
//
//            @Override
//            public void onStart() {
//                dialog.show();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray facilityArray) {
//                super.onSuccess(statusCode, headers, facilityArray);
//                facilityList=new ArrayList<Facility>();
//                for (int i=0;i<facilityArray.length();i++){
//                    try {
//                        JSONObject facilityObj=facilityArray.getJSONObject(i);
//                        Facility facility=new Facility(facilityObj.getString("facility_id"),facilityObj.getString("name"));
//                        facilityList.add(facility);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                facilitySpinner.setAdapter(new ArrayAdapter<Facility>(
//                        AppointmentActivity.this,android.R.layout.simple_dropdown_item_1line,facilityList));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                Log.v("deb","server err "+responseString);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                dialog.dismiss();
//            }
//        });
//
//    }

//
//    class Facility{
//        private String facility_id;
//        private String name;
//
//        public Facility(String facility_id, String name) {
//            this.facility_id = facility_id;
//            this.name = name;
//        }
//
//        public String getFacility_id() {
//            return facility_id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        @Override
//        public String toString() {
//            return name;
//        }
//    }

}
