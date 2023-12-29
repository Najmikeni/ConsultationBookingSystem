package com.example.groupproject557.remote;

public class ApiUtils {

    // REST API server URL
    public static final String BASE_URL = "https://groupselek.000webhostapp.com/prestige/";

    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    public static AppointmentService getAppointmentService() {
        return RetrofitClient.getClient(BASE_URL).create(AppointmentService.class);
    }

}