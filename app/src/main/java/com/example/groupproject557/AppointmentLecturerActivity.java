package com.example.groupproject557;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.groupproject557.adapter.AppointmentAdapter;
import com.example.groupproject557.model.Appointment;
import com.example.groupproject557.model.DeleteRequest;
import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.AppointmentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentLecturerActivity extends AppCompatActivity {

    AppointmentService appointmentService;
    Context context;
    RecyclerView AppReq;
    AppointmentAdapter adapter;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_lecturer);
        context = this;

        AppReq = findViewById(R.id.AppReq);
        registerForContextMenu(AppReq);

        updateListView();
    }

    private void updateListView() {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get book service instance
        appointmentService = ApiUtils.getAppointmentService();

        // execute the call. send the user token when sending the query
        appointmentService.getAllAppointmentReqbyID(user.getToken() , user.getId()).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // token is not valid/expired
                if (response.code() == 401) {
                    displayAlert("Session Invalid");
                }

                // Get list of appointment object from response
                List<Appointment> appointments = response.body();
                //if the lect dont have an appointment
                if(appointments == null){
                    displayToast("No appointment available");
                    Intent intent = new Intent(context, LecturerMainActivity.class);
                    startActivity(intent);

                }
                // initialize adapter
                adapter = new AppointmentAdapter(context, appointments);

                // set adapter to the RecyclerView
                AppReq.setAdapter(adapter);

                // set layout to recycler view
                AppReq.setLayoutManager(new LinearLayoutManager(context));

                // add separator between item in the list
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(AppReq.getContext(),
                        DividerItemDecoration.VERTICAL);
                AppReq.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show();
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Appointment selectedAppointment = adapter.getSelectedItem();
        System.out.println(selectedAppointment.getId());
        Log.d("MyApp", "selected "+ selectedAppointment.toString());
        Intent intent;
        switch (item.getItemId()) {
            case R.id.detail:
                doDetail(selectedAppointment);
                break;
            case R.id.approve:
                approveAppointment(selectedAppointment);
                // refresh user to AppointmentListActivity
                intent = new Intent(getApplicationContext(),AppointmentLecturerActivity.class);
                startActivity(intent);
                break;
            case R.id.decline:
                declineAppointment(selectedAppointment);
                // refresh user to AppointmentListActivity
                intent = new Intent(getApplicationContext(),AppointmentLecturerActivity.class);
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void doDetail(Appointment selectedAppointment){
        Log.d("MyApp:", "viewing appointment details "+ selectedAppointment.toString());
        Intent intent = new Intent(context, AppointmentDetailActivity.class);
        intent.putExtra("id", selectedAppointment.getId());
        startActivity(intent);
    }

    private void approveAppointment(Appointment selectedAppointment){
        Log.d("MyApp:", "approve Appointment"+ selectedAppointment.toString());
        status = "Approve";
        selectedAppointment.setStatus(status);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // send request to update appointment status to REST API
        AppointmentService appointmentService= ApiUtils.getAppointmentService();
        Call<Appointment> call = appointmentService.updateAppointment(user.getToken(), selectedAppointment);
        Context context = this;
        // execute
        call.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {

                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // invalid session?
                if (response.code() == 401)
                    displayAlert("Invalid session. Please re-login");

                // book updated successfully?
                Appointment updatedStatus = response.body();
                if (updatedStatus != null) {
                    // display message
                    Toast.makeText(context,
                            "Appointment status updated successfully.",
                            Toast.LENGTH_LONG).show();

                } else {
                    displayAlert("Update Appointment Status failed.");
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
            }
        });
    }
    private void declineAppointment(Appointment selectedAppointment){
        Log.d("MyApp:", "decline Appointment"+ selectedAppointment.toString());
        status = "Decline";
        selectedAppointment.setStatus(status);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // send request to update appointment status to REST API
        AppointmentService appointmentService= ApiUtils.getAppointmentService();
        Call<Appointment> call = appointmentService.updateAppointment(user.getToken(), selectedAppointment);
        Context context = this;
        // execute
        call.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {

                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // invalid session?
                if (response.code() == 401)
                    displayAlert("Invalid session. Please re-login");

                // book updated successfully?
                Appointment updatedStatus = response.body();
                if (updatedStatus != null) {
                    // display message
                    Toast.makeText(context,
                            "Appointment status updated successfully.",
                            Toast.LENGTH_LONG).show();

                } else {
                    displayAlert("Update Appointment Status failed.");
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
            }
        });
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}