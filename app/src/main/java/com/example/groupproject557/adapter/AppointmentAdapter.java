package com.example.groupproject557.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject557.R;
import com.example.groupproject557.model.Appointment;
import com.example.groupproject557.model.User;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        public TextView tvAppointmentDate;
        public TextView tvLectId;
        public TextView tvTime;
        public TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            tvAppointmentDate = (TextView) itemView.findViewById(R.id.tvAppointmentDate);
            tvLectId = (TextView) itemView.findViewById(R.id.tvLectName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            itemView.setOnLongClickListener(this);

        }
        @Override
        public boolean onLongClick(View view) {
            currentPos = getAdapterPosition(); //key point, record the position here
            return false;
        }

    }
    private int currentPos;
    private List<Appointment> mListData;   // list of appointment objects
    private Context mContext;       // activity context

    public AppointmentAdapter(Context context, List<Appointment> listData) {
        mListData = listData;
        mContext = context;
    }


    private Context getmContext(){return mContext;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view =inflater.inflate(R.layout.appointment_list_item,parent,false);

        ViewHolder viewHolder= new AppointmentAdapter.ViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, int position) {
        Appointment appointment = mListData.get(position);
        holder.tvAppointmentDate.setText("Date     : " + appointment.getAppointmentDate().substring(0,10));
        String temp = String.valueOf(appointment.getLecturer_id());
        holder.tvLectId.setText("Lecturer ID: " + temp);
        String temp2 = String.valueOf(appointment.getTime());
        holder.tvTime.setText("Time    : " + temp2);
        holder.tvStatus.setText("Status    : " + appointment.getStatus());
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public Appointment getSelectedItem() {
        if(currentPos>=0 && mListData!=null && currentPos<mListData.size()) {
            return mListData.get(currentPos);
        }
        return null;
    }

}
