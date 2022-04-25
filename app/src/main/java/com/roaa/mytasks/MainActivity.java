package com.roaa.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final View.OnClickListener addTaskButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent startSecondActivityIntent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(startSecondActivityIntent);
        }
    };
    private final View.OnClickListener allTasksButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent startThirdActivityIntent = new Intent(getApplicationContext(), MainActivity3.class);
            startActivity(startThirdActivityIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle("My Tasks");

        Button addButton = findViewById(R.id.addTask);
        Button allButton = findViewById(R.id.allTasks);

        addButton.setOnClickListener(addTaskButtonListener);
        allButton.setOnClickListener(allTasksButtonListener);
    }
}