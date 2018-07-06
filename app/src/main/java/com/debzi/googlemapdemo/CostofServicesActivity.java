package com.debzi.googlemapdemo;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class CostofServicesActivity extends AppCompatActivity {

    String facility_id = "";

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costof_services);
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
        actionBar.setTitle("Cost of Services");

        Toast.makeText(CostofServicesActivity.this, "Loading Complete!!", Toast.LENGTH_SHORT).show();
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
                Intent ii = new Intent(CostofServicesActivity.this, FacilityActivity.class);
                ii.putExtra("FACILITY_ID", facility_id);
                startActivity(ii);
                finish();
                break;

            case R.id.action_specialists:
                Intent i = new Intent(CostofServicesActivity.this, SpecialistActivity.class);
                i.putExtra("FACILITY_ID", facility_id);
                startActivity(i);
                finish();
                break;

            case R.id.action_list_services:
                Intent a = new Intent(CostofServicesActivity.this, ListofServicesActivity.class);
                a.putExtra("FACILITY_ID", facility_id);
                startActivity(a);
                finish();
                break;

            case R.id.action_cost_services:
                Toast.makeText(CostofServicesActivity.this, "Cost of Services", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_provision_poor:
                Intent c = new Intent(CostofServicesActivity.this, ProvisionActivity.class);
                c.putExtra("FACILITY_ID", facility_id);
                startActivity(c);
                finish();
                break;

            case R.id.action_note:
                Intent d = new Intent(CostofServicesActivity.this, NoteActivity.class);
                d.putExtra("FACILITY_ID", facility_id);
                startActivity(d);
                finish();
                break;
            case R.id.action_appointment:
                Intent e = new Intent(CostofServicesActivity.this, AppointmentActivity.class);
                e.putExtra("FACILITY_ID", facility_id);
                startActivity(e);
                finish();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

}
