package com.lenovo.example.appointo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

public class AdminSelectDate extends AppCompatActivity {

    CalendarView calendarView;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_date);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                if(dayOfMonth<10 && month<10)
                    date = "0" + dayOfMonth + "-0" + month + "-" + year;
                else if(dayOfMonth<10)
                    date = "0" + dayOfMonth + "-" + month + "-" + year;
                else if(month<10)
                    date = dayOfMonth + "-0" + month + "-" + year;

                Intent intent = new Intent(AdminSelectDate.this,AdminAddSlots.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }
}
