package com.debzi.googlemapdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button login_button, register;

    EditText userName, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        userName = (EditText)findViewById(R.id.username);

        password = (EditText)findViewById(R.id.password);

        userName.addTextChangedListener(new MyTextWatcher(userName));
        password.addTextChangedListener(new MyTextWatcher(password));

        login_button = (Button)findViewById(R.id.login_button);

        register = (Button)findViewById(R.id.signUp_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                submitForm();




            }
        });

    }


    private void submitForm() {


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }


        if(checkCredentials()){

            Toast.makeText(this, "Login Successful. Please wait a moment", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(LoginActivity.this, MapActivity.class);
            startActivity(i);
            finish();

        }else if(!checkCredentials()){

            Toast.makeText(this, "Your username and password doesnot match.", Toast.LENGTH_SHORT).show();
        }




    }


    private boolean checkCredentials(){

        if(userName.getText().toString().equals("anikavirgo@gmail.com")||password.getText().toString().equals("anika1234")){

            return true;
        }

        return false;
    }


    private boolean validateEmail() {
        String email = userName.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {

            if (getCurrentFocus() != null) {

//                tv_username.setBackgroundColor(Color.TRANSPARENT);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }

//            tv_username.setBackgroundColor(Color.TRANSPARENT);
            userName.setError("Please enter a valid username");


            requestFocus(userName);


            return false;
        } else {

            if (getCurrentFocus() != null) {


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }


            userName.setError(null);


        }

        return true;
    }


    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {

            if (getCurrentFocus() != null) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }

//            tv_password.setBackgroundColor(Color.TRANSPARENT);
            password.setError("Please enter a valid password");

            requestFocus(password);

            return false;
        } else {

            if (getCurrentFocus() != null) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }

            password.setError(null);

        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void requestFocus(View view) {

//        view.setBackgroundColor(Color.TRANSPARENT);

        if (view.requestFocus()) {


//            view.setBackgroundColor(Color.TRANSPARENT);

        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {
            }
        }
    }

}
