package com.example.groupproject557.model;

import com.example.groupproject557.remote.AppointmentService;

public class Appointment {
    private int id;
    private String name;
    private int student_id;
    private int lecturer_id;
    private String appointmentDate;
    private String reason;
    private String status;
    private String time;


    public Appointment( String name, int student_id, int lecturer_id, String appointmentDate, String reason, String status,String time) {

        this.name = name;
        this.student_id = student_id;
        this.lecturer_id = lecturer_id;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
        this.status = status;
        this.time = time;
    }

    public Appointment(){}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getLecturer_id() {
        return lecturer_id;
    }

    public void setLecturer_id(int lecturer_id) {
        this.lecturer_id = lecturer_id;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", student_id=" + student_id +
                ", lecturer_id=" + lecturer_id +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
