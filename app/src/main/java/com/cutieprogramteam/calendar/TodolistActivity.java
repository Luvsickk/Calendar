package com.cutieprogramteam.calendar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;
import android.view.View;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Button;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TodolistActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView textViewTaskHeader, textViewNoTasks;
    LinearLayout taskContainer;
    ImageButton buttonAdd;
    Button buttonDelete;

    String selectedDate = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolistlayout);

        // Link XML views
        calendarView = findViewById(R.id.calendarView);
        textViewTaskHeader = findViewById(R.id.textViewTaskHeader);
        textViewNoTasks = findViewById(R.id.textViewNoTasks); //
        taskContainer = findViewById(R.id.taskContainer);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);

        sharedPreferences = getSharedPreferences("MyTasks", MODE_PRIVATE);

        // Default selected date = today
        Calendar cal = Calendar.getInstance();
        selectedDate = getFormattedDate(cal);
        textViewTaskHeader.setText("Tasks for " + selectedDate + ":");
        loadTasksForDate(selectedDate);

        // change tasks when selecting a new date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar cal1 = Calendar.getInstance();
            cal1.set(year, month, dayOfMonth);
            selectedDate = getFormattedDate(cal1);
            textViewTaskHeader.setText("Tasks for " + selectedDate + ":");
            loadTasksForDate(selectedDate);
        });

        // Add Task Dialog
        buttonAdd.setOnClickListener(v -> showAddTaskDialog());

        // delete completed tasks
        buttonDelete.setOnClickListener(v -> removeCompletedTasks());
    }

    // Date Formatting
    private String getFormattedDate(Calendar cal) {
        // Word format → "Month DD, YYYY"
        SimpleDateFormat wordFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        // return numericFormat.format(cal.getTime());
        return wordFormat.format(cal.getTime());
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Task");

        final EditText input = new EditText(this);
        input.setHint("Enter your task...");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String taskText = input.getText().toString().trim();
            if (!taskText.isEmpty()) {
                addTaskToLayout(taskText, false);
                saveTask(selectedDate, taskText);
                textViewNoTasks.setVisibility(View.GONE); // hide message when task added
            } else {
                Toast.makeText(this, "Please enter a task!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addTaskToLayout(String taskText, boolean isChecked) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(taskText);
        checkBox.setTextSize(16);
        checkBox.setChecked(isChecked);
        updateCheckboxStyle(checkBox, isChecked);

        // Save when toggled
        checkBox.setOnCheckedChangeListener((buttonView, isCheckedNow) -> {
            updateCheckboxStyle(checkBox, isCheckedNow);
            saveAllTasks(selectedDate);
        });

        // Long press to delete
        checkBox.setOnLongClickListener(v -> {
            taskContainer.removeView(checkBox);
            saveAllTasks(selectedDate);
            if (taskContainer.getChildCount() == 0) {
                textViewNoTasks.setVisibility(View.VISIBLE); // show when empty
            }
            return true;
        });

        taskContainer.addView(checkBox);
    }

    private void updateCheckboxStyle(CheckBox checkBox, boolean isChecked) {
        if (isChecked) {
            checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setTextColor(Color.parseColor("#999999"));
        } else {
            checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            checkBox.setTextColor(Color.parseColor("#333333"));
        }
    }

    private void saveTask(String date, String taskText) {
        String existing = sharedPreferences.getString(date, "");
        existing += taskText + "|false\n";
        sharedPreferences.edit().putString(date, existing).apply();
    }

    private void loadTasksForDate(String date) {
        taskContainer.removeAllViews();
        String savedTasks = sharedPreferences.getString(date, "");

        if (savedTasks.isEmpty()) {
            textViewTaskHeader.setTextColor(0xFF333333);
            textViewNoTasks.setVisibility(View.VISIBLE); // show "No tasks"
            return;
        } else {
            textViewNoTasks.setVisibility(View.GONE); // hide it
        }

        String[] tasks = savedTasks.split("\n");
        boolean hasTasks = false;
        for (String t : tasks) {
            if (t.trim().isEmpty()) continue;
            String[] parts = t.split("\\|");
            String taskText = parts[0];
            boolean isChecked = parts.length > 1 && Boolean.parseBoolean(parts[1]);
            addTaskToLayout(taskText, isChecked);
            hasTasks = true;
        }

        textViewTaskHeader.setTextColor(0xFF4CAF50);
        textViewNoTasks.setVisibility(hasTasks ? View.GONE : View.VISIBLE);
    }

    private void saveAllTasks(String date) {
        StringBuilder data = new StringBuilder();
        int count = taskContainer.getChildCount();

        for (int i = 0; i < count; i++) {
            View v = taskContainer.getChildAt(i);
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                data.append(cb.getText().toString())
                        .append("|")
                        .append(cb.isChecked())
                        .append("\n");
            }
        }

        sharedPreferences.edit().putString(date, data.toString()).apply();
    }

    private void removeCompletedTasks() {
        int childCount = taskContainer.getChildCount();
        StringBuilder remainingTasks = new StringBuilder();

        for (int i = 0; i < childCount; i++) {
            View v = taskContainer.getChildAt(i);
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                if (!cb.isChecked()) {
                    remainingTasks.append(cb.getText().toString()).append("|false\n");
                }
            }
        }

        sharedPreferences.edit().putString(selectedDate, remainingTasks.toString()).apply();
        loadTasksForDate(selectedDate);
        Toast.makeText(this, "Completed tasks removed!", Toast.LENGTH_SHORT).show();
    }
}