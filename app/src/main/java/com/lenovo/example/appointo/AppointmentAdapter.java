package com.lenovo.example.appointo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.sql.DataSource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {

    private Context mContext;

    public AppointmentAdapter(@NonNull Context context, @NonNull List<Appointment> objects) {
        super(context,0, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.appointments_list_item, parent, false);
        }

        Appointment appointment = getItem(position);

        TextView appointmentDate = listItemView.findViewById(R.id.appointment_date);
        TextView appointmentTime = listItemView.findViewById(R.id.appointment_time);
        TextView appointmentStatus = listItemView.findViewById(R.id.appointment_status);
        TextView appointmentWith = listItemView.findViewById(R.id.appointment_with);

        String date = appointment.getDate();
        String time = appointment.getTime();
        String status = appointment.getStatus();
        String with = appointment.getWith();

        appointmentDate.setText(date);
        appointmentDate.setGravity(Gravity.CENTER);
        appointmentDate.setBackgroundResource(R.drawable.date_bg);

        appointmentTime.setText(time);
        appointmentStatus.setText(status);
        appointmentWith.setText(with);

        Log.e("date",date);

        return listItemView;
    }

}
