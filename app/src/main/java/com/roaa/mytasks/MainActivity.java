package com.roaa.mytasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
//import com.roaa.mytasks.data.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView userTasks;
//    List<Task> TaskInfoList = new ArrayList<>();
    List<Task> TaskInfoList = new ArrayList<>();

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

        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("My Tasks");

        Button addButton = findViewById(R.id.addTask);
        Button allButton = findViewById(R.id.allTasks);
        userTasks=findViewById(R.id.userTasks);

//        initialiseData();
//        TaskInfoList=TaskAppDatabase.getInstance(getApplicationContext()).taskDao().getAll();
        Amplify.DataStore.query(Task.class ,
                tasks -> {
                    while (tasks.hasNext()) {
                        Task eachTask = tasks.next();
                        TaskInfoList.add(eachTask);
                    }
                },
                failure -> Log.e(TAG, "Could not query DataStore", failure)
                );

        RecyclerView recyclerView = findViewById(R.id.tasks);
        TaskRecyclerViewAdapter taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(
                TaskInfoList, position -> {

            Intent intent1=new Intent(getApplicationContext(),TaskDetails.class);
            intent1.putExtra("TaskTitle",TaskInfoList.get(position).getTitle());
            intent1.putExtra("TaskDesc",TaskInfoList.get(position).getDescription());
            intent1.putExtra("TaskState",TaskInfoList.get(position).getStatus().toString());
            startActivity(intent1);
        });
        recyclerView.setAdapter(taskRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        Button task1 = findViewById(R.id.task1);
//        Button task2 = findViewById(R.id.task2);
//        Button task3 = findViewById(R.id.task3);
//
//        task1.setOnClickListener(view -> {
//            Intent intent1=new Intent(getApplicationContext(),TaskDetails.class);
//            intent1.putExtra("TaskTitle",task1.getText());
//            startActivity(intent1);
//        });
//        task2.setOnClickListener(view -> {
//            Intent intent2=new Intent(getApplicationContext(),TaskDetails.class);
//            intent2.putExtra("TaskTitle",task2.getText());
//            startActivity(intent2);
//        });
//        task3.setOnClickListener(view -> {
//            Intent intent3=new Intent(getApplicationContext(),TaskDetails.class);
//            intent3.putExtra("TaskTitle",task3.getText());
//            startActivity(intent3);
//        });

        addButton.setOnClickListener(addTaskButtonListener);
        allButton.setOnClickListener(allTasksButtonListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                navigateToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserName();
    }
    private void navigateToSettings() {
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }
    @SuppressLint("SetTextI18n")
    private void setUserName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        userTasks.setText(sharedPreferences.getString(Settings.USERNAME, "User Tasks")+"'s Tasks");
    }

//    private void initialiseData() {
//        TaskInfoList.add(new Task("Task1", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "new"));
//        TaskInfoList.add(new Task("Task2", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "assigned"));
//        TaskInfoList.add(new Task("Task3", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "in progress"));
//        TaskInfoList.add(new Task("Task4", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "complete"));
//    }

}