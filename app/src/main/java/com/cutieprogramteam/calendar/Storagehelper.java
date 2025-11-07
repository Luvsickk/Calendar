package com.cutieprogramteam.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Storagehelper {
    private static final String PREF_NAME = "todo_list_pref";
    private static final String KEY_TASKS = "tasks";

    public static void saveTasks(Context context, ArrayList<Task> tasks) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (Task task : tasks) {
                JSONObject obj = new JSONObject();
                obj.put("name", task.getName());
                obj.put("isDone", task.isDone());
                jsonArray.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_TASKS, jsonArray.toString()).apply();
    }

    public static ArrayList<Task> loadTasks(Context context) {
        ArrayList<Task> tasks = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_TASKS, null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    tasks.add(new Task(obj.getString("name"), obj.getBoolean("isDone")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }
}