package com.lenovo.example.appointo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class AdminHome extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Appointment>> {

    FirebaseAuth mAuth;
    TextView noAppointmentsText;
    ListView appointmentsListView;
    ArrayList<Appointment> appointments;
    AppointmentAdapter mAdapter;
    LoaderManager loaderManager;
    FloatingActionButton fab;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.show();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,AdminSelectDate.class));
            }
        });

        noAppointmentsText = findViewById(R.id.no_appointments_text);
        appointmentsListView = findViewById(R.id.appointments_list);

        appointments = new ArrayList<>();
        appointments.clear();
        mAdapter = new AppointmentAdapter(this,appointments);
        appointmentsListView.setAdapter(mAdapter);

        loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(0,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sign_out){
            mAuth.signOut();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Appointment>> onCreateLoader(int id, @Nullable Bundle args) {
        appointments.clear();
        return new AppointmentLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Appointment>> loader, List<Appointment> data) {
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            data.clear();
        }
        else {
            appointmentsListView.setVisibility(View.GONE);
            noAppointmentsText.setVisibility(View.VISIBLE);
        }

        pd.dismiss();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Appointment>> loader) {
        appointments.clear();
        mAdapter.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }
}
