package com.example.groupproject557;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupproject557.model.Appointment;
import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.AppointmentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetailActivity extends AppCompatActivity {
    AppointmentService appointmentService;
    private Button btnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        // get id from prev activity by intent, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        appointmentService = ApiUtils.getAppointmentService();
        appointmentService.getAppointment(user.getToken(),id).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());
                // get Appointment object from the response
                Appointment appointmentDetail = response.body();

                //references value
                TextView textDate = findViewById(R.id.textDate);
                TextView textTime = findViewById(R.id.textTime);
                TextView textName = findViewById(R.id.textName);
                TextView textStudentID = findViewById(R.id.textStudentID);
                TextView textReason = findViewById(R.id.textReason);
                TextView textStatus = findViewById(R.id.textStatus);

                //to set the values
                textDate.setText(appointmentDetail.getAppointmentDate());
                textTime.setText(appointmentDetail.getTime());
                textName.setText(appointmentDetail.getName());
                textStudentID.setText(String.valueOf(appointmentDetail.getStudent_id()));
                textReason.setText(appointmentDetail.getReason());
                textStatus.setText(appointmentDetail.getStatus());
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

        /*// set onClick action to btnLogin
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AppointmentLecturerActivity.class));
            }
        });*/
    }
}