package com.cutieprogramteam.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SemesterActivity extends AppCompatActivity {
    String semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jude2);

        semester = getIntent().getStringExtra("SEMESTER");

        Button btnPrelims = findViewById(R.id.btnPrelims);
        Button btnMidterm = findViewById(R.id.btnMidterm);
        Button btnEndterm = findViewById(R.id.btnEndterm);

        btnPrelims.setOnClickListener(v -> openTerm("Prelims"));
        btnMidterm.setOnClickListener(v -> openTerm("Midterm"));
        btnEndterm.setOnClickListener(v -> openTerm("Endterm"));
    }

    private void openTerm(String term) {
        Intent intent = new Intent(SemesterActivity.this, TermActivity.class);
        intent.putExtra("SEMESTER", semester);
        intent.putExtra("TERM", term);
        startActivity(intent);
    }
}
