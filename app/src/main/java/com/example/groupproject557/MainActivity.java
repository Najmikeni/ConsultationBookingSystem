package com.example.groupproject557;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        TextView wlcmTxt = findViewById(R.id.wlcmTxt);

        wlcmTxt.setText("Welcome " + user.getName());

        // assign action to logout button
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // clear the shared preferences
                SharedPrefManager.getInstance(getApplicationContext()).logout();

                // display message
                Toast.makeText(getApplicationContext(),
                        "You have successfully logged out.",
                        Toast.LENGTH_LONG).show();

                // forward to LoginActivity
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

        // assign action to Lecturer List button
        Button btnLecturerList = findViewById(R.id.btnLecturerList);
        btnLecturerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // forward user to LecturerListActivity
                Intent intent = new Intent(getApplicationContext(),LectListActivity.class);
                startActivity(intent);

            }
        });

        // assign action to Appointment List button
        Button btnAppointmentList = findViewById(R.id.btnAppointmentList);
        btnAppointmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // forward user to AppointmentListActivity
                Intent intent = new Intent(getApplicationContext(),AppointmentStudentActivity.class);
                startActivity(intent);

            }
        });
        //button your profile
        Button btnCreateRequest = findViewById(R.id.btnCreateRequest);
        btnCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UserProfile.class);
                startActivity(intent);
            }
        });
    }


}


