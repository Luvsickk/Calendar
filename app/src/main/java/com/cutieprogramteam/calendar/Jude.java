package com.cutieprogramteam.calendar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;


public class Jude extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jude);

        Button btnSem1 = findViewById(R.id.btnSem1);
        Button btnSem2 = findViewById(R.id.btnSem2);
        Button btnSem3 = findViewById(R.id.btnSem3);

        btnSem1.setOnClickListener(v -> openSemester("1st Semester"));
        btnSem2.setOnClickListener(v -> openSemester("2nd Semester"));
        btnSem3.setOnClickListener(v -> openSemester("3rd Semester"));
    }

    private void openSemester(String semester) {
        Intent intent = new Intent(Jude.this, SemesterActivity.class);
        intent.putExtra("SEMESTER", semester);
        startActivity(intent);
    }
}

