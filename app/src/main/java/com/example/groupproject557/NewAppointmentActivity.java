package com.example.groupproject557;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.example.groupproject557.adapter.LecturerAdapter;
import com.example.groupproject557.model.Appointment;
import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.AppointmentService;
import com.example.groupproject557.remote.UserService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentActivity extends AppCompatActivity {

    UserService userService;

    private EditText txtLecturerID;
    private EditText txtReason;
    private Spinner spinnerTime;
    private Spinner spinnerLect;
    private static TextView tvAppointmentDate; // static because need to be accessed by DatePickerFragment
    private LecturerAdapter adapterLect;


    private static Date AppointmentDate; // static because need to be accessed by DatePickerFragment

    private Context context;
    /**
     * Date picker fragment class
     * Reference: https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            // create a date object from selected year, month and day
            AppointmentDate = new GregorianCalendar(year, month, day).getTime();

            // display in the label beside the button with specific date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tvAppointmentDate.setText( sdf.format(AppointmentDate) );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        userService = ApiUtils.getUserService();

        // store context
        context = this;

        // get view objects references
        txtReason = findViewById(R.id.txtReason);
        tvAppointmentDate = findViewById(R.id.tvAppointmentDate);
        spinnerTime = findViewById(R.id.spinnerTime);
        spinnerLect = findViewById(R.id.spinnerLect);
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        userService.getAllLect(user.getToken()).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // Get list of user object from response
                List<User> users = response.body();

                // initialize adapter
                adapterLect= new LecturerAdapter(context, users);
                String [] idItems = new String[users.size()];
                for(int i = 0; i< users.size(); i++)
                {   User tempUser = users.get(i);
                    idItems[i]=  String.valueOf(tempUser.getId() + "-" + tempUser.getName());
                }
                ArrayAdapter<String> adapterLect = new ArrayAdapter<String>(NewAppointmentActivity.this, R.layout.spinner_item_lect,idItems);
                // set adapter to the dropdown
                spinnerLect.setAdapter(adapterLect);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });


        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.TimeArray, R.layout.spinner_item_time);

        adapter.setDropDownViewResource(R.layout.spinner_item_time);

        spinnerTime.setAdapter(adapter);

        // set default createdAt value, get current date
        AppointmentDate = new Date();

        // display in the label beside the button with specific date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tvAppointmentDate.setText( sdf.format(AppointmentDate) );
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Called when Add appointment button is clicked
     * @param v
     */
    public void addNewAppointment(View v){
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String name =  user.getName();
        int student_id = user.getId();
        String temptSpinner = (String) spinnerLect.getSelectedItem();
        int lecturer_id =  Integer.parseInt(temptSpinner.replaceAll("[^0-9]", ""));
        String reason = txtReason.getText().toString();


        // convert AppointmentDate date to format in DB
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        String ap_date = sdf.format(AppointmentDate);
        // set updated_at with the same value as created_at
        String updated_at = ap_date;
        String status = "New";
        String time = (String) spinnerTime.getSelectedItem();

        // get user info from SharedPreferences
        Appointment a = new Appointment( name, student_id, lecturer_id, ap_date, reason, status,time);
        System.out.println(a.getName());

        AppointmentService appointmentService = ApiUtils.getAppointmentService();

        Call<List<Appointment>> callR = appointmentService.getAllAppointmentReqbyID(user.getToken(),lecturer_id);
        callR.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                //Get list from the response
                List<Appointment> appointmentstemp = response.body();
                //variable to check if same date and time
                String dateCheck;
                String timeCheck;
                //variable to check status if accepted/approve
                String statusCheck ;
                //variable check
                int checker=0;
                //Temporary object for Appointment
                Appointment appTemp;

                for(int j=0 ; j<appointmentstemp.size() ; j++){

                    appTemp = appointmentstemp.get(j);
                    dateCheck = String.valueOf(appTemp.getAppointmentDate());
                    timeCheck = String.valueOf(appTemp.getTime());
                    statusCheck = String.valueOf(appTemp.getStatus());
                    String temptDate = String.valueOf(ap_date);

                    System.out.println(dateCheck.substring(0,10).equals(temptDate.substring(0,10)));
                    System.out.println(timeCheck.equals(time));
                    System.out.println(temptDate.substring(0,10));
                    System.out.println(statusCheck.equals( "Approve"));

                    if(dateCheck.substring(0,10).equals(temptDate.substring(0,10)) && timeCheck.equals(time) && statusCheck.equals( "Approve")){
                        checker = 1;
                    }
                }

                if(checker == 1){
                    displayAlert("Time and Date slot is not available");
                }
                else{
                    Call<Appointment> callS = appointmentService.addAppointment(user.getToken(), a);
                    callS.enqueue(new Callback<Appointment>() {
                        @Override
                        public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                            // for debug purpose
                            Log.d("MyApp:", "Response: " + response.raw().toString());

                            // invalid session?
                            if (response.code() == 401)
                                displayAlert("Invalid session. Please re-login");

                            // appointment added successfully?
                            Appointment addedAppointment = response.body();
                            if (addedAppointment != null) {
                                // display message
                                Toast.makeText(context,
                                        addedAppointment.getName() + " added successfully.",
                                        Toast.LENGTH_LONG).show();

                                // end this activity and forward user to LectListActivity
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                displayAlert("Add New Appointment failed.");
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


            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {

            }
        });



    }
    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
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
}