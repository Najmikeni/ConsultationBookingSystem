package com.example.groupproject557;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.groupproject557.model.ErrorResponse;
import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.UserService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    TextView txtRegister;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // if the user is already logged in we will directly start
        // the main activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(this).getUser();
            finish();// stop this LoginActivity
            if(user.getRole().equalsIgnoreCase("user")){
                startActivity(new Intent(this, MainActivity.class));
            }
            else{
                startActivity(new Intent(this, LecturerMainActivity.class));
            }
            return;
        }

        // get references to form elements
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        //make register word clickable
        String text = "New user? Register here!";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            //when clicked and do this
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(LoginActivity.this, "Please register if you are new.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), NewRegisterActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED); //change word "here" color
                ds.setUnderlineText(false); //no underline text
            }
        };
        ss.setSpan(clickableSpan, 19, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtRegister.setText(ss);
        txtRegister.setMovementMethod(LinkMovementMethod.getInstance());

        // get UserService instance
        userService = ApiUtils.getUserService();

        // set onClick action to btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get username and password entered by user
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                // validate form, make sure it is not empty
                if (validateLogin(username, password)) {
                    // do login
                    doLogin(username, password);
                }
            }
        });
    }

    /**
     * Validate value of username and password entered. Client side validation.
     * @param username
     * @param password
     * @return
     */
    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            displayToast("Username is required");
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            displayToast("Password is required");
            return false;
        }
        return true;
    }

    /**
     * Call REST API to login
     * @param username
     * @param password
     */
    private void doLogin(String username, String password) {
        Call call;
        if(username.contains("@") == true) {
            call = userService.loginEmail(username, password);
        }
        else{
            call = userService.login(username, password);
        }
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {

                // received reply from REST API
                if (response.isSuccessful()) {
                    // parse response to POJO
                    User user = (User) response.body();
                    if (user.getToken() != null) {
                        // successful login. server replies a token value
                        displayToast("Login successful");
                        displayToast("Token: " + user.getToken());

                        // store value in Shared Preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        // forward user to MainActivity
                        finish();
                        System.out.println(user.getRole());
                        if(user.getRole().contentEquals("user")){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), LecturerMainActivity.class));
                        }
                    }
                }
                else if (response.errorBody() != null){
                    // parse response to POJO
                    String errorResp = null;
                    try {
                        errorResp = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ErrorResponse e = new Gson().fromJson( errorResp, ErrorResponse.class);
                    displayToast(e.getError().getMessage());
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                displayToast("Error connecting to server.");
                displayToast(t.getMessage());
            }

        });
    }

    /**
     * Display a Toast message
     * @param message
     */
    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}