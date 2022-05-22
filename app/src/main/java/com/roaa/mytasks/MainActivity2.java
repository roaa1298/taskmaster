package com.roaa.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Status;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
//import com.roaa.mytasks.data.Task;

import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getSimpleName();
    private TextView TotalText;
    private int counter=0;

    String[] teams={"Team1","Team2","Team3"};
    String teamSelected;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Spinner spino= findViewById(R.id.addTeam);

        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(
                this,
                R.array.teams,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        spino.setAdapter(ad);

        spino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                teamSelected=teams[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Task");

        Button addingButton = findViewById(R.id.adding);
        TotalText=findViewById(R.id.total);
        EditText taskTitle=findViewById(R.id.task_title);
        EditText taskDescription=findViewById(R.id.task_desc);


        addingButton.setOnClickListener(view -> {
            counter++;
            TotalText.setText("Total Tasks: "+counter);

            Amplify.API.query(
                    ModelQuery.list(Team.class, Team.NAME.eq(teamSelected)),
                    response -> {
                        for (Team teamItem : response.getData()) {
                            if(teamItem.getName().equals(teamSelected)){
                                Task newTask=Task.builder().title(taskTitle.getText().toString())
                                        .status(Status.ASSIGNED)
                                        .teamTasksId(teamItem.getId())
                                        .description(taskDescription.getText().toString()).build();

                                Amplify.DataStore.save(newTask,
                                        success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                                        error -> Log.e(TAG, "Could not save item to DataStore", error)
                                );

                                Amplify.API.mutate(
                                        ModelMutation.create(newTask),
                                        success -> Log.i(TAG, "Saved item: " + success.getData().getTitle()),
                                        error -> Log.e(TAG, "Could not save item to API", error)
                                );
                            }
                        }
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );

//            Task task=new Task(taskTitle.getText().toString(),taskDescription.getText().toString(),"assigned");
//            Long newTask = TaskAppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);
//            System.out.println("----------- Task ID: "+newTask+" -------------------");
            Toast.makeText(this,"submitted!",Toast.LENGTH_SHORT).show();
        });

    }


}