package com.debzi.googlemapdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class NoteActivity extends AppCompatActivity {

    String location_id = "1375853984463";

    String facility_id = "";

    String locationId[];

    String facilityId[];

    String observNoteArray[], reasontypeArray[], reasonArray[], visitArray[];

    TextView tv_complete, tv_incomplete, tv_not_done, tv_reason, tv_visit, tv_ob_note;
    LinearLayout note_parent, ll_visit;
    ProgressBar pb2;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
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
        actionBar.setTitle("Note");

        tv_complete = (TextView)findViewById(R.id.complete);
        tv_incomplete = (TextView)findViewById(R.id.incomplete);
        tv_not_done = (TextView)findViewById(R.id.not_done);
        tv_reason = (TextView)findViewById(R.id.reason);
        tv_visit = (TextView)findViewById(R.id.visit);
        tv_ob_note = (TextView)findViewById(R.id.observation_note);

        note_parent = (LinearLayout)findViewById(R.id.note_parent);

        ll_visit = (LinearLayout)findViewById(R.id.ll_visit);

        pb2=(ProgressBar) findViewById(R.id.progressBar_note);

        note_parent.setVisibility(View.GONE);



        loadNoteInfo();




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
                Intent ii = new Intent(NoteActivity.this, FacilityActivity.class);
                ii.putExtra("FACILITY_ID", facility_id);
                startActivity(ii);
                finish();
                break;

            case R.id.action_specialists:
                Intent i = new Intent(NoteActivity.this, SpecialistActivity.class);
                i.putExtra("FACILITY_ID", facility_id);
                startActivity(i);
                finish();
                break;

            case R.id.action_list_services:
                Intent a = new Intent(NoteActivity.this, ListofServicesActivity.class);
                a.putExtra("FACILITY_ID", facility_id);
                startActivity(a);
                finish();
                break;

            case R.id.action_cost_services:
                Intent b = new Intent(NoteActivity.this, CostofServicesActivity.class);
                b.putExtra("FACILITY_ID", facility_id);
                startActivity(b);
                finish();
                break;

            case R.id.action_provision_poor:
                Intent c = new Intent(NoteActivity.this, ProvisionActivity.class);
                c.putExtra("FACILITY_ID", facility_id);
                startActivity(c);
                finish();
                break;

            case R.id.action_appointment:
                Intent e = new Intent(NoteActivity.this, AppointmentActivity.class);
                e.putExtra("FACILITY_ID", facility_id);
                startActivity(e);
                finish();
                break;

            case R.id.action_note:
                Toast.makeText(NoteActivity.this, "Note", Toast.LENGTH_SHORT).show();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    private void loadNoteInfo() {
        Log.v("deb","load position");
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.get("http://debalina.bin404.com/facility_note.php",null,new JsonHttpResponseHandler(){


            @Override
            public void onStart() {

                pb2.setVisibility(View.VISIBLE);
                pb2.setMax(100);



                // called before request is started
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {

                super.onProgress(bytesWritten, totalSize);

                int prog = (Math.round((bytesWritten/totalSize)*100));

                pb2.setProgress(prog);


            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray noteArray) {
                super.onSuccess(statusCode, headers, noteArray);
                Log.v("deb","server suc - "+noteArray.toString());
                facilityId = new String [noteArray.length()];
                observNoteArray = new String [noteArray.length()];
                reasontypeArray = new String [noteArray.length()];
                reasonArray = new String [noteArray.length()];
                visitArray = new String [noteArray.length()];





                try {

                    Log.v("deb","json array");
                    for (int i=0;i<noteArray.length();i++){
                        JSONObject noteObj=noteArray.getJSONObject(i);
                        Log.v("deb","json obj "+noteObj.toString());
                        facilityId[i] = noteObj.getString("facility_id");
                        observNoteArray[i] = noteObj.getString("observation_note");
                        reasontypeArray[i] = noteObj.getString("reason_type");
                        reasonArray[i] = noteObj.getString("reason_note");
                        visitArray[i] = noteObj.getString("reason_visits_no");




                    }



                    inflateNoteUI(facilityId, observNoteArray, reasontypeArray, reasonArray, visitArray);


                    pb2.setVisibility(View.GONE);
                    note_parent.setVisibility(View.VISIBLE);
                    Toast.makeText(NoteActivity.this, "Loading Complete!!", Toast.LENGTH_SHORT).show();



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


    public void inflateNoteUI(String a[], String b[], String c[], String d[], String e[]){


        for(int i=0; i<a.length; i++){

            if(facility_id.equals(a[i])){

                tv_ob_note.setText(b[i]);

                if(c[i].equals("1")){

                    tv_incomplete.setBackgroundColor(Color.parseColor("#b4b4b4"));
                    tv_reason.setText(d[i]);
                    tv_visit.setText(e[i]);


                }

                else if(c[i].equals("2")){

                    tv_not_done.setBackgroundColor(Color.parseColor("#b4b4b4"));
                    tv_reason.setText(d[i]);
                    tv_visit.setText(e[i]);


                }

                else if(c[i].equals("3")){

                    tv_complete.setBackgroundColor(Color.parseColor("#b4b4b4"));
                    tv_reason.setVisibility(View.GONE);
                    ll_visit.setVisibility(View.GONE);


                }


            }



        }





    }


}
