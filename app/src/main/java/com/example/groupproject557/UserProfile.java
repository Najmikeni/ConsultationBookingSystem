package com.example.groupproject557;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupproject557.model.Appointment;
import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.AppointmentService;
import com.example.groupproject557.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //app user object from sharedpref
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        //from api instance
        userService = ApiUtils.getUserService();
        AppointmentService appointmentService = ApiUtils.getAppointmentService();

        userService.getUserbyID(user.getToken(),user.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //debugging...
                Log.d("MyApp:", "Response: " + response.raw().toString());

                //get object from body of response to user object
                User user = response.body();

                //references values to textview in activiyt layout
                TextView txtNameProfile = findViewById(R.id.txtLectName);
                TextView txtID = findViewById(R.id.txtLectID);
                TextView txtRole = findViewById(R.id.txtLectRole);
                TextView txtEmail = findViewById(R.id.txtLectEmail);
                TextView txtNumApp = findViewById(R.id.txtNumApp);

                //set the values of the textview
                txtNameProfile.setText(user.getName());
                txtID.setText(String.valueOf(user.getId()));
                txtRole.setText(user.getRole());
                txtEmail.setText(user.getEmail());


                txtNumApp.setText("0");

                if(user.getRole().equals("admin"))
                {
                    appointmentService.getAllAppointmentReqbyID(user.getToken(), user.getId()).enqueue(new Callback<List<Appointment>>() {
                        @Override
                        public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                            List<Appointment> app = response.body();
                            txtNumApp.setText(String.valueOf(app.size()));
                        }

                        @Override
                        public void onFailure(Call<List<Appointment>> call, Throwable t) {

                        }
                    });

                }
                else {
                    appointmentService.getAllAppointmentbyID(user.getToken(), user.getId()).enqueue(new Callback<List<Appointment>>() {
                        @Override
                        public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                            List<Appointment> app = response.body();
                            txtNumApp.setText(String.valueOf(app.size()));
                        }

                        @Override
                        public void onFailure(Call<List<Appointment>> call, Throwable t) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });


    }

}