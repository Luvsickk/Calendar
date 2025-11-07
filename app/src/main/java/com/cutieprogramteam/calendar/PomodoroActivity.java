package com.cutieprogramteam.calendar;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PomodoroActivity extends AppCompatActivity {

    private TextView tvDate, tvSelectedTask, tvTimer, tvProgress, tvSessionLabel;
    private EditText etTask, etWorkTime, etBreakTime;
    private Button btnAddTask, btnSetTimer, btnStartPause, btnClearTasks;
    private ListView listViewTasks;
    private ProgressBar progressBarTasks;

    private ArrayList<String> taskList;
    private ArrayAdapter<String> taskAdapter;

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis;
    private int selectedTaskIndex = -1;
    private long breakTimeInMillis = 0;
    private boolean isOnBreak = false;

    private int tasksCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodorolayout);



        tvDate = findViewById(R.id.tvDate);
        tvSelectedTask = findViewById(R.id.tvSelectedTask);
        tvTimer = findViewById(R.id.tvTimer);
        tvProgress = findViewById(R.id.tvProgress);
        etTask = findViewById(R.id.etTask);
        etWorkTime = findViewById(R.id.etWorkTime);
        etBreakTime = findViewById(R.id.etBreakTime);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnSetTimer = findViewById(R.id.btnSetTimer);
        btnStartPause = findViewById(R.id.btnStartPause);
        listViewTasks = findViewById(R.id.listViewTasks);
        progressBarTasks = findViewById(R.id.progressBarTasks);
        btnClearTasks = findViewById(R.id.btnClearTasks);

        // 🔹 Added Label for Work/Break indicator
        tvSessionLabel = new TextView(this);
        tvSessionLabel.setText("Session: None");
        tvSessionLabel.setTextSize(18);
        tvSessionLabel.setPadding(0, 10, 0, 10);
        LinearLayout rootLayout = findViewById(R.id.rootLayout);
        rootLayout.addView(tvSessionLabel, 2);

        // Display current date
        String currentDate = new SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText("Date: " + currentDate);

        // Task setup
        taskList = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, taskList);
        listViewTasks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewTasks.setAdapter(taskAdapter);

        btnAddTask.setOnClickListener(v -> {
            String task = etTask.getText().toString().trim();
            if (!task.isEmpty()) {
                taskList.add(task);
                taskAdapter.notifyDataSetChanged();
                etTask.setText("");
                updateProgress();
            }
        });
        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            listViewTasks.setItemChecked(position, true);
            selectedTaskIndex = position;
            tvSelectedTask.setText("Selected Task: " + taskList.get(position));
        });

        btnSetTimer.setOnClickListener(v -> {
            if (etWorkTime.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter work time", Toast.LENGTH_SHORT).show();
                return;
            }
            int minutes = Integer.parseInt(etWorkTime.getText().toString());
            timeLeftInMillis = minutes * 60 * 1000L;

            if (!etBreakTime.getText().toString().isEmpty()) {
                int breakMin = Integer.parseInt(etBreakTime.getText().toString());
                breakTimeInMillis = breakMin * 60 * 1000L;
            } else {
                breakTimeInMillis = 0;
            }

            isOnBreak = false;
            updateTimerText();
            tvSessionLabel.setText("Session: Work"); // 🔹 show label
            Toast.makeText(this, "Timer set for " + minutes + " minutes" +
                    (breakTimeInMillis > 0 ? " with " + (breakTimeInMillis / 60000) + " min break" : ""), Toast.LENGTH_SHORT).show();
        });

        btnStartPause.setOnClickListener(v -> {
            if (selectedTaskIndex == -1) {
                Toast.makeText(this, "Select a task first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (timeLeftInMillis <= 0) {
                Toast.makeText(this, "Set timer first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (timerRunning) pauseTimer();
            else startTimer();
        });

        btnClearTasks.setOnClickListener(v -> {
            if (!taskList.isEmpty()) {
                new AlertDialog.Builder(PomodoroActivity.this)
                        .setTitle("Clear All Tasks")
                        .setMessage("Are you sure you want to clear all tasks?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            taskList.clear();
                            taskAdapter.notifyDataSetChanged();
                            selectedTaskIndex = -1;
                            tasksCompleted = 0;
                            updateProgress();
                            tvSelectedTask.setText("Selected Task: None");
                            tvProgress.setText("Progress: 0%");
                            Toast.makeText(PomodoroActivity.this, "All tasks cleared", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                Toast.makeText(PomodoroActivity.this, "No tasks to clear", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;

                if (!isOnBreak && selectedTaskIndex != -1) {
                    // ✅ Task finished
                    String finishedTask = taskList.get(selectedTaskIndex) + " ✅";
                    taskList.set(selectedTaskIndex, finishedTask);
                    listViewTasks.setItemChecked(selectedTaskIndex, false);

                    // 🔹 Hide checkbox (disable selection for completed task)
                    listViewTasks.getChildAt(selectedTaskIndex).setEnabled(false);

                    selectedTaskIndex = -1;
                    tvSelectedTask.setText("Selected Task: None");
                    tasksCompleted++;
                    updateProgress();

                    if (breakTimeInMillis > 0) {
                        isOnBreak = true;
                        timeLeftInMillis = breakTimeInMillis;
                        tvSessionLabel.setText("Session: Break 🍵");
                        Toast.makeText(PomodoroActivity.this, "Break time! 🍵", Toast.LENGTH_SHORT).show();
                        startTimer();
                        return;
                    }
                } else if (isOnBreak) {
                    tvSessionLabel.setText("Session: Work 💪");
                    Toast.makeText(PomodoroActivity.this, "Break finished! Back to work 💪", Toast.LENGTH_SHORT).show();
                    isOnBreak = false;
                }

                tvTimer.setText("Done!");
                btnStartPause.setText("Start");
                tvSessionLabel.setText("Session: None");
            }
        }.start();

        timerRunning = true;
        btnStartPause.setText("Pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        btnStartPause.setText("Start");
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void updateProgress() {
        if (taskList.isEmpty()) {
            progressBarTasks.setProgress(0);
            tvProgress.setText("Progress: 0%");
            return;
        }
        int totalTasks = taskList.size();
        int progressPercent = (int) (((double) tasksCompleted / totalTasks) * 100);
        progressBarTasks.setProgress(progressPercent);

        if (progressPercent >= 100) {
            tvProgress.setText("Accomplished! 🎉");
        } else {
            tvProgress.setText("Progress: " + progressPercent + "%");
        }
    }
}
