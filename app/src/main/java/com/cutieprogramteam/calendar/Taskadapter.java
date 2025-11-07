package com.cutieprogramteam.calendar;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import java.util.ArrayList;

public class Taskadapter extends ArrayAdapter<Task> {

    public Taskadapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.listitemlayout, parent, false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkBoxTask);

        // Temporarily remove listener before reusing the view
        checkBox.setOnCheckedChangeListener(null);

        // Set text and checked state from Task object
        checkBox.setText(task.getName());
        checkBox.setChecked(task.isDone());

        // Apply strikethrough for completed tasks
        if (task.isDone()) {
            checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setTextColor(0xFF888888);
        } else {
            checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            checkBox.setTextColor(0xFF000000);
        }

        // Reattach listener safely
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            if (isChecked) {
                checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                checkBox.setTextColor(0xFF888888);
            } else {
                checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                checkBox.setTextColor(0xFF000000);
            }
        });

        return convertView;
    }
}