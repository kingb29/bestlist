package com.example.bestlist.ui.login;

import android.app.Activity;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.RequestFuture;
import com.example.bestlist.LocalListings;
import com.example.bestlist.R;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.bestlist.data.Result;
import com.example.bestlist.data.model.LoggedInUser;
import com.example.bestlist.ui.login.LoginViewModel;
import com.example.bestlist.ui.login.LoginViewModelFactory;

import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity implements VolleyCallback{

    private LoginViewModel loginViewModel;
    private Map<String,String> mapParams;
    private Map<String,String> mapParams2;
    private Boolean returnBool;
    private String returnSalt = "null";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final CheckBox registerBox = findViewById(R.id.checkBox);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(true);
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));

                }

            }
        });

        registerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    loginButton.setText("Register");
                }
                else{
                    loginButton.setText("Log in");
                }

            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                try {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {


                    }

                }catch(Exception e){

                }
                return false;
            } // end of method

        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(registerBox.isChecked()) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    try {
                        registerNewUser(usernameEditText.getText().toString(), // attempts to register a new user to the database
                                passwordEditText.getText().toString());
                        loadingProgressBar.setVisibility(View.GONE);
                    }catch(Exception e){

                    }
                }else{
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    try {
                        validateLogin(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }catch(Exception e){

                    }
                }
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = "Welcome " + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registerNewUser(final String username, final String password) throws Exception{ // registers a new user to the database (returns true if success/false if not)

        System.out.println("Attempting to Register a new User: " + username + " with a password of " + password);
        Random random = new Random();

        byte[] salt = new byte[16];
        random.nextBytes(salt); // create a random salt for the user

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder(); // allow us to hash the password

        String hashPass = enc.encodeToString(hash); // store the hashed+salted Password
        String finalSalt = salt.toString(); // store the salt
        System.out.println("Check #1");

        RequestQueue queue = Volley.newRequestQueue(this); // open a new request
        String url = "http://54.172.67.81/register.php"; // url of login authentication

        mapParams = new HashMap<>(); // declare the map to be passed into the php file and pass in the parameters
        mapParams.put("Username", username);
        mapParams.put("HashPass", hashPass);
        mapParams.put("Salt", finalSalt);
        System.out.println("Check #2");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Check #3 the response is: " + response);
                if(response.equals("Success")){
                    System.out.println("Check #4");
                    // login user
                    System.out.println("Logging user in...");
                    loginViewModel.login(username,password);
                    // take to new activity
                    System.out.println("Taking to main page...");
                    startActivity(new Intent(LoginActivity.this, LocalListings.class));
                }else if(response.equals("Username already taken!")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Username is already taken!",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Check #7" + error.toString());
            }
        }){
            @Override
            public Map<String, String> getParams(){
                return mapParams;
            }
        };
        System.out.println("Check #5");
        queue.add(stringRequest);
        System.out.println("Check #6 " + returnBool);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean loginCheck(String username, String password) throws Exception{ // Checks to see if the user exists in the database, returns true if yes, false if not

        RequestQueue queue = Volley.newRequestQueue(this); // open a new request
        String url = "http://54.172.67.81/getsalt.php"; // url of login authentication
        System.out.println("Getting the user's salt...");
        mapParams = new HashMap<>(); // declare the map to be passed into the php file and pass in the parameters
        mapParams.put("Username", username);

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, future, future){
            @Override
            public Map<String, String> getParams(){
                return mapParams;
            }
        };
        System.out.println("check 4" + returnSalt);
        queue.add(stringRequest);
        System.out.println("check 5 " + returnSalt);
        try {
            returnSalt = future.get(30, TimeUnit.SECONDS);
        }catch(InterruptedException e){
            System.out.println("ERROR " + e.toString());
        }catch(Exception e){
            System.out.println("ERROR " + e.toString());
        }
        System.out.println("check 6 " + returnSalt);


        //dddddddddddddddddddddddddddddddddddddddddddddddddddd
        System.out.println("Check 8 : " + returnSalt);
        String saltResponse = returnSalt;
        byte[] salt = saltResponse.getBytes();


        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder(); // allow us to hash the password

        String hashPass = enc.encodeToString(hash); // store the hashed+salted Password
        String finalSalt = salt.toString(); // store the salt

       // RequestQueue queue = Volley.newRequestQueue(this); // open a new request
        //String url = "http://54.172.67.81/login.php"; // url of login authentication

        mapParams = new HashMap<>(); // declare the map to be passed into the php file and pass in the parameters
        mapParams.put("Username", username);
        mapParams.put("HashPass", hashPass);

        //RequestFuture<String> future = RequestFuture.newFuture();
        //StringRequest stringRequest = new StringRequest(Request.Method.POST, url, future, future);
        //queue.add(stringRequest);

        return returnBool;
    }

    public void validateLogin(String username, String password)throws Exception{ // gets the salt of the username from the database
        RequestQueue queue = Volley.newRequestQueue(this); // open a new request
        String url = "http://54.172.67.81/getsalt.php"; // url of login authentication
        System.out.println("Getting the user's salt...");
        mapParams = new HashMap<>(); // declare the map to be passed into the php file and pass in the parameters
        mapParams.put("Username", username);

        final String userName = username;
        final String passWord = password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("") && response != null) {
                        attemptToLogIn(response, userName, passWord);
                    }else{
                        System.out.println("Username doesn't exist in db.");
                        Toast toast = Toast.makeText(getApplicationContext(),"Invalid Login Details!",Toast.LENGTH_SHORT);
                        toast.show();
                        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                    }
                }catch(Exception e){
                    System.out.println("Could not Log in");
                }
                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR " + error.toString());
            }
        }){
            @Override
            public Map<String, String> getParams(){
                return mapParams;
            }
        };
        queue.add(stringRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void attemptToLogIn(String salt, String username, String password) throws Exception{
        final String userName = username;
        final String passWord = password;
        RequestQueue queue = Volley.newRequestQueue(this); // open a new request
        String url = "http://54.172.67.81/login.php"; // url of login authentication

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder(); // allow us to hash the password

        String hashPass = enc.encodeToString(hash); // store the hashed+salted Password
        mapParams = new HashMap<>(); // declare the map to be passed into the php file and pass in the parameters
        mapParams.put("Username", username);
        mapParams.put("HashPass", hashPass);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("SUCCESSFUL LOGIN");
                    System.out.println("Logging user in...");
                    loginViewModel.login(userName,passWord);
                    // take to new activity
                    System.out.println("Taking to main page...");
                    startActivity(new Intent(LoginActivity.this, LocalListings.class));
                }catch(Exception e){
                    System.out.println("Could not Log in");
                    Toast toast = Toast.makeText(getApplicationContext(),"Invalid Login Details!",Toast.LENGTH_SHORT);
                    toast.show();
                    //startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR " + error.toString());
            }
        }){
            @Override
            public Map<String, String> getParams(){
                return mapParams;
            }
        };
        queue.add(stringRequest);



    }

    @Override
    public void requestFinished(String response) {


    }
}
