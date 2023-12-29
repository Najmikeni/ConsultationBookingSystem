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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentStudentActivity extends AppCompatActivity {
    AppointmentService appointmentService;
    Context context;
    RecyclerView appList;
    AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_student);
        context = this;

        appList = findViewById(R.id.AppList);
        registerForContextMenu(appList);

        updateListView();

        // action handler for Add  floating button
        FloatingActionButton fab = findViewById(R.id.fabtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // forward user to NewAppointmentkActivity
                Intent intent = new Intent(context, NewAppointmentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateListView() {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get appointment service instance
        appointmentService = ApiUtils.getAppointmentService();

        // execute the call. send the user token when sending the query
        appointmentService.getAllAppointmentbyID(user.getToken() , user.getId()).enqueue(new Callback<List<Appointment>>() {
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
                //if the student dont have an appoinment
                if(appointments == null){
                    displayToast("No appointment available");
                    Intent intent = new Intent(context, NewAppointmentActivity.class);
                    startActivity(intent);

                    }

                // initialize adapter
                adapter = new AppointmentAdapter(context, appointments);

                // set adapter to the RecyclerView
                appList.setAdapter(adapter);

                // set layout to recycler view
                appList.setLayoutManager(new LinearLayoutManager(context));

                // add separator between item in the list
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(appList.getContext(),
                        DividerItemDecoration.VERTICAL);
                appList.addItemDecoration(dividerItemDecoration);
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
        inflater.inflate(R.menu.app_context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Appointment selectedAppointment = adapter.getSelectedItem();
        System.out.println(selectedAppointment.getId());
        Log.d("MyApp", "selected "+ selectedAppointment.toString());
        switch (item.getItemId()) {
            case R.id.menu_cancel://should match the id in the context menu file
                cancelAppointment(selectedAppointment);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Cancel appointment record. Called by contextual menu "menu_cancel"
     * @param selectedAppointment - appointment selected by user
     */
    private void cancelAppointment(Appointment selectedAppointment) {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // prepare REST API call
        AppointmentService appointmentService= ApiUtils.getAppointmentService();
        Call<DeleteRequest> call = appointmentService.cancelAppointment(user.getToken(), selectedAppointment.getId());

        //check status before canceling request
        if(selectedAppointment.getStatus().equalsIgnoreCase("New")) {
            // execute the call
            call.enqueue(new Callback<DeleteRequest>() {
                @Override
                public void onResponse(Call<DeleteRequest> call, Response<DeleteRequest> response) {
                    if (response.code() == 200) {
                        // 200 means OK
                        displayAlert("Appointment successfully canceled");
                        // update data in list view
                        updateListView();
                    } else {
                        displayAlert("Appointment can not be canceled");
                        Log.e("MyApp:", response.raw().toString());
                    }
                }

                @Override
                public void onFailure(Call<DeleteRequest> call, Throwable t) {
                    displayAlert("Error [" + t.getMessage() + "]");
                    Log.e("MyApp:", t.getMessage());
                }
            });
        }
        else{
            displayAlert("Appointment can not be canceled");
        }
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