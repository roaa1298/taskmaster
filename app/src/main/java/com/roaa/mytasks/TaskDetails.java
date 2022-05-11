package com.roaa.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class TaskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent titleIntent = getIntent();
        String pageTitle=titleIntent.getStringExtra("TaskTitle");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(pageTitle);

        TextView desc=findViewById(R.id.desc);
        TextView task_state=findViewById(R.id.task_state);
        desc.setText(titleIntent.getStringExtra("TaskDesc"));
        task_state.setText(titleIntent.getStringExtra("TaskState"));
    }
}