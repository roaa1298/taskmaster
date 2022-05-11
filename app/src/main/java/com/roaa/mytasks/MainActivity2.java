package com.roaa.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.roaa.mytasks.data.Task;

import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    private TextView TotalText;
    private int counter=0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Task");

        Button addingButton = findViewById(R.id.adding);
        TotalText=findViewById(R.id.total);
        EditText taskTitle=findViewById(R.id.task_title);
        EditText taskDescription=findViewById(R.id.task_desc);


        addingButton.setOnClickListener(view -> {
            counter++;
            TotalText.setText("Total Tasks: "+counter);
            Task task=new Task(taskTitle.getText().toString(),taskDescription.getText().toString(),"assigned");
            Long newTask = TaskAppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);
            System.out.println("----------- Task ID: "+newTask+" -------------------");
            Toast.makeText(this,"submitted!",Toast.LENGTH_SHORT).show();
        });

    }
}