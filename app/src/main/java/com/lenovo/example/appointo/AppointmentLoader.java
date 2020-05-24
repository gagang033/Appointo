package com.lenovo.example.appointo;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class AppointmentLoader extends AsyncTaskLoader<List<Appointment>> {

    ArrayList<Appointment> appointments = new ArrayList<>();
    DatabaseReference ref;

    public AppointmentLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Appointment> loadInBackground() {

        final Semaphore semaphore = new Semaphore(0);

        ref = FirebaseDatabase.getInstance().getReference("appointments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds1 : dataSnapshot.getChildren()){
                    for(DataSnapshot ds : ds1.getChildren()){
                        Log.e("inside","inside");
                        String id = ds.child("id").getValue().toString();
                        String date = ds.child("date").getValue().toString();
                        String free = ds.child("free").getValue().toString();
                        if(free.equals("No"))
                            continue;
                        String time = ds.child("time").getValue().toString();
                        String status = ds.child("status").getValue().toString();
                        String with = "N/A";
                        if(status.equals("Booked"))
                            with = ds.child("with").getValue().toString();
                        Appointment appointment = new Appointment(id,date,time,free,status,with);

                        Log.e("appointment",appointment.getStatus());
                        appointments.add(appointment);
                    }
                    Log.e("semapj=hore","relaesdd");

                }
                semaphore.release();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try{
            semaphore.acquire();
        }
        catch (Exception e){

        }

        return appointments;
    }
}
