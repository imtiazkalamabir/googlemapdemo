package com.debzi.googlemapdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    ActionBar actionBar;

    Button register_button;

    EditText fullName, mobileNumber, emailAddress, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Register");

        fullName = (EditText)findViewById(R.id.fullname);

        mobileNumber = (EditText)findViewById(R.id.mobile_number);

        emailAddress = (EditText)findViewById(R.id.email_address);

        password = (EditText)findViewById(R.id.user_password);

        register_button = (Button) findViewById(R.id.register_button);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fullName.getText().toString().equals("")||mobileNumber.getText().toString().equals("")||emailAddress.getText().toString().equals("")||password.getText().toString().equals("")){

                    Toast.makeText(RegisterActivity.this, "Please fill up the form correctly", Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.register_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
        }



        return super.onOptionsItemSelected(item);
    }
}
