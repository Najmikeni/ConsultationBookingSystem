package com.example.groupproject557;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRegisterActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtName;
    private EditText edtPassword;
    private EditText edtrole;
    private Button btnBack;
    private Button btnRegister;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        context = this;

        //get input references
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        edtrole = findViewById(R.id.edtRole);
        edtrole.setEnabled(false);
        btnBack = findViewById(R.id.btnBack);

        // set onClick action to btnLogin
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        /**
         * Called when Register button is clicked
         */
        // set onClick action to btnRegister
        /*btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get values in form
                String email = edtEmail.getText().toString();
                String username = edtUsername.getText().toString();
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String role = "user";
                String lease = "2023-01-31 14:24:14";
                String secret = "206b2dbe-ecc9-490b-b81b-83767288bc5e";
                String token = "";

                //encrypt password from string to md5
                String encryptPassword = md5(password);

                // create a Register object
                //User u = new User(0, email, username, name, encryptPassword, token,lease, role, 1,secret);
                // get user info from SharedPreferences
                //User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

                // send request to add new book to the REST API
                /*UserService userService = ApiUtils.getUserService();
                Call<User> call = userService.registerUser(user.getToken(), u);

                // execute
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        // for debug purpose
                        Log.d("MyApp:", "Response: " + response.raw().toString());


                        // account register successfully?
                        User registerAccount = response.body();
                        if (registerAccount != null) {
                            // display message
                            Toast.makeText(context,
                                    registerAccount.getUsername() + " added successfully.",
                                    Toast.LENGTH_LONG).show();

                            // end this activity and forward user to loginActivity
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // for debug purpose
                        Log.d("MyApp:", "Error: " + t.getCause().getMessage());
                    }
                });
            }
        });
    }

    //change string to md5 method
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }*/
    }
}