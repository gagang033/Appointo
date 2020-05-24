package com.lenovo.example.appointo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminAddSlots extends AppCompatActivity {

    CheckBox time00,time01,time02,time03,time04,time05,time06,time07;
    CheckBox time08,time09,time10,time11,time12,time13,time14,time15;
    CheckBox time16,time17,time18,time19,time20,time21,time22,time23;
    Button addSlotsButton;
    ArrayList<String> slotsIds;
    Map<String,String> timeSlots;
    String date;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_slots);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");

        pd = new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.show();

        slotsIds = new ArrayList<>();
        timeSlots = new HashMap<>();
        addTimeSlots();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("appointments").child(date);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Appointment appointment = ds.getValue(Appointment.class);
                    if(appointment.getFree().equals("Yes"))
                        slotsIds.add(appointment.getId());
                }
                for(String slotId : slotsIds){
                    setClickableFalseSlots(slotId);
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addSlotsButton = findViewById(R.id.add_slots_btn);
        addSlotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("appointments").child(date);
                for(int i=0;i<24;i++){
                    String id = Integer.toString(i);
                    String dt = "";
                    for(int j=0;j<5;j++)
                        dt += date.charAt(j);
                    String time = timeSlots.get(id);
                    String free = "No";
                    if(slotsIds.contains(id))
                        free = "Yes";
                    String status = "Available";
                    String with = "N/A";

                    Appointment appointment = new Appointment(id,dt,time,free,status,with);
                    ref.child(id).setValue(appointment);
                }
                Toast.makeText(AdminAddSlots.this,"Slots Added",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        findCheckBoxIds();

        setCheckBoxListeners();
    }

    void setClickableFalseSlots(String slotId){
        switch (slotId){
            case "0":
                time00.setClickable(false);
                time00.setChecked(true);
                break;
            case "1":
                time01.setClickable(false);
                time01.setChecked(true);
                break;
            case "2":
                time02.setClickable(false);
                time02.setChecked(true);
                break;
            case "3":
                time03.setClickable(false);
                time03.setChecked(true);
                break;
            case "4":
                time04.setClickable(false);
                time04.setChecked(true);
                break;
            case "5":
                time05.setClickable(false);
                time05.setChecked(true);
                break;
            case "6":
                time06.setClickable(false);
                time06.setChecked(true);
                break;
            case "7":
                time07.setClickable(false);
                time07.setChecked(true);
                break;
            case "8":
                time08.setClickable(false);
                time08.setChecked(true);
                break;
            case "9":
                time09.setClickable(false);
                time09.setChecked(true);
                break;
            case "10":
                time10.setClickable(false);
                time10.setChecked(true);
                break;
            case "11":
                time11.setClickable(false);
                time11.setChecked(true);
                break;
            case "12":
                time12.setClickable(false);
                time12.setChecked(true);
                break;
            case "13":
                time13.setClickable(false);
                time13.setChecked(true);
                break;
            case "14":
                time14.setClickable(false);
                time14.setChecked(true);
                break;
            case "15":
                time15.setClickable(false);
                time15.setChecked(true);
                break;
            case "16":
                time16.setClickable(false);
                time16.setChecked(true);
                break;
            case "17":
                time17.setClickable(false);
                time17.setChecked(true);
                break;
            case "18":
                time18.setClickable(false);
                time18.setChecked(true);
                break;
            case "19":
                time19.setClickable(false);
                time19.setChecked(true);
                break;
            case "20":
                time20.setClickable(false);
                time20.setChecked(true);
                break;
            case "21":
                time21.setClickable(false);
                time21.setChecked(true);
                break;
            case "22":
                time22.setClickable(false);
                time22.setChecked(true);
                break;
            case "23":
                time23.setClickable(false);
                time23.setChecked(true);
                break;
        }
    }

    void addTimeSlots(){
        timeSlots.put("0","00:00 - 01:00");
        timeSlots.put("1","01:00 - 02:00");
        timeSlots.put("2","02:00 - 03:00");
        timeSlots.put("3","03:00 - 04:00");
        timeSlots.put("4","04:00 - 05:00");
        timeSlots.put("5","05:00 - 06:00");
        timeSlots.put("6","06:00 - 07:00");
        timeSlots.put("7","08:00 - 08:00");
        timeSlots.put("8","08:00 - 09:00");
        timeSlots.put("9","09:00 - 10:00");
        timeSlots.put("10","10:00 - 11:00");
        timeSlots.put("11","11:00 - 12:00");
        timeSlots.put("12","12:00 - 13:00");
        timeSlots.put("13","13:00 - 14:00");
        timeSlots.put("14","14:00 - 15:00");
        timeSlots.put("15","15:00 - 16:00");
        timeSlots.put("16","16:00 - 17:00");
        timeSlots.put("17","17:00 - 18:00");
        timeSlots.put("18","18:00 - 19:00");
        timeSlots.put("19","19:00 - 20:00");
        timeSlots.put("20","20:00 - 21:00");
        timeSlots.put("21","21:00 - 22:00");
        timeSlots.put("22","22:00 - 23:00");
        timeSlots.put("23","23:00 - 00:00");
    }

    void findCheckBoxIds(){
        time00 = findViewById(R.id.time_00);
        time01 = findViewById(R.id.time_01);
        time02 = findViewById(R.id.time_02);
        time03 = findViewById(R.id.time_03);
        time04 = findViewById(R.id.time_04);
        time05 = findViewById(R.id.time_05);
        time06 = findViewById(R.id.time_06);
        time07 = findViewById(R.id.time_07);
        time08 = findViewById(R.id.time_08);
        time09 = findViewById(R.id.time_09);
        time10 = findViewById(R.id.time_10);
        time11 = findViewById(R.id.time_11);
        time12 = findViewById(R.id.time_12);
        time13 = findViewById(R.id.time_13);
        time14 = findViewById(R.id.time_14);
        time15 = findViewById(R.id.time_15);
        time16 = findViewById(R.id.time_16);
        time17 = findViewById(R.id.time_17);
        time18 = findViewById(R.id.time_18);
        time19 = findViewById(R.id.time_19);
        time20 = findViewById(R.id.time_20);
        time21 = findViewById(R.id.time_21);
        time22 = findViewById(R.id.time_22);
        time23 = findViewById(R.id.time_23);
    }

    void setCheckBoxListeners(){
        time00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time00.isChecked())
                    slotsIds.add("0");
                else
                    slotsIds.remove("0");
            }
        });

        time01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time01.isChecked())
                    slotsIds.add("1");
                else
                    slotsIds.remove("1");
            }
        });

        time02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time02.isChecked())
                    slotsIds.add("2");
                else
                    slotsIds.remove("2");
            }
        });

        time03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time03.isChecked())
                    slotsIds.add("3");
                else
                    slotsIds.remove("3");
            }
        });

        time04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time04.isChecked())
                    slotsIds.add("4");
                else
                    slotsIds.remove("4");
            }
        });

        time05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time05.isChecked())
                    slotsIds.add("5");
                else
                    slotsIds.remove("5");
            }
        });

        time06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time06.isChecked())
                    slotsIds.add("6");
                else
                    slotsIds.remove("6");
            }
        });

        time07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time07.isChecked())
                    slotsIds.add("7");
                else
                    slotsIds.remove("7");
            }
        });

        time08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time08.isChecked())
                    slotsIds.add("8");
                else
                    slotsIds.remove("8");
            }
        });

        time09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time09.isChecked())
                    slotsIds.add("9");
                else
                    slotsIds.remove("9");
            }
        });

        time10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time10.isChecked())
                    slotsIds.add("10");
                else
                    slotsIds.remove("10");
            }
        });

        time11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time11.isChecked())
                    slotsIds.add("11");
                else
                    slotsIds.remove("11");
            }
        });

        time12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time12.isChecked())
                    slotsIds.add("12");
                else
                    slotsIds.remove("12");
            }
        });

        time13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time13.isChecked())
                    slotsIds.add("13");
                else
                    slotsIds.remove("13");
            }
        });

        time14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time14.isChecked())
                    slotsIds.add("14");
                else
                    slotsIds.remove("14");
            }
        });

        time15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time15.isChecked())
                    slotsIds.add("15");
                else
                    slotsIds.remove("15");
            }
        });

        time16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time16.isChecked())
                    slotsIds.add("16");
                else
                    slotsIds.remove("16");
            }
        });

        time17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time17.isChecked())
                    slotsIds.add("17");
                else
                    slotsIds.remove("17");
            }
        });

        time18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time18.isChecked())
                    slotsIds.add("18");
                else
                    slotsIds.remove("18");
            }
        });

        time19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time19.isChecked())
                    slotsIds.add("19");
                else
                    slotsIds.remove("19");
            }
        });

        time20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time20.isChecked())
                    slotsIds.add("20");
                else
                    slotsIds.remove("20");
            }
        });

        time21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time21.isChecked())
                    slotsIds.add("21");
                else
                    slotsIds.remove("21");
            }
        });

        time22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time22.isChecked())
                    slotsIds.add("22");
                else
                    slotsIds.remove("22");
            }
        });

        time23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time23.isChecked())
                    slotsIds.add("23");
                else
                    slotsIds.remove("23");
            }
        });
    }
}
