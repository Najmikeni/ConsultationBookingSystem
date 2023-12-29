package com.example.groupproject557.remote;

import com.example.groupproject557.model.Appointment;
import com.example.groupproject557.model.DeleteRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppointmentService {

    @GET("api/Appointment")
    Call<List<Appointment>> getAllAppointment(@Header("api-key") String api_key);

    @GET("api/Appointment")
    Call<List<Appointment>> getAllAppointmentbyID(@Header("api-key") String api_key, @Query("student_id") int student_id);

    @GET("api/Appointment")
    Call<List<Appointment>> getAllAppointmentReqbyID(@Header("api-key") String api_key, @Query("lecturer_id") int lecturer_id);

    @GET("api/Appointment/{id}")
    Call<Appointment> getAppointment(@Header("api-key") String api_key, @Path("id") int id);

    @POST("api/Appointment")
    Call<Appointment> addAppointment(@Header ("api-key") String apiKey, @Body Appointment appointment);

    @POST("api/Appointment/delete/{id}")
    Call<DeleteRequest> cancelAppointment(@Header("api-key") String apiKey, @Path("id") int id);

    @POST("api/Appointment/update")
    Call<Appointment> updateAppointment(@Header ("api-key") String apiKey, @Body Appointment appointment);
}
