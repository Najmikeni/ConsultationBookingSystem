package com.example.groupproject557;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerDetailActivity extends AppCompatActivity {
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_detail);

        // get id from prev activity by intent, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        userService = ApiUtils.getUserService();

        userService.getUserbyID(user.getToken(),id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());
                // get User object from the response
                User userLect = response.body();

                //references value
                TextView txtLectName = findViewById(R.id.txtLectName);
                TextView txtLectID = findViewById(R.id.txtLectID);
                TextView txtLectRole = findViewById(R.id.txtLectRole);
                TextView txtLectEmail = findViewById(R.id.txtLectEmail);

                //to set the values
                txtLectName.setText(userLect.getName());
                txtLectID.setText(String.valueOf(userLect.getId()));
                txtLectRole.setText(userLect.getRole());
                txtLectEmail.setText(userLect.getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

    }
}