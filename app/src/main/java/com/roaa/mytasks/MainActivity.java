package com.roaa.mytasks;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amazonaws.mobileconnectors.cognitoauth.AuthUserSession;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
//import com.roaa.mytasks.data.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView userTasks;
//    List<Task> TaskInfoList = new ArrayList<>();
    List<Team> TeamInfoList = new ArrayList<>();
    private Handler handler;
    private String teamName;

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
    private String teamId;
    private String nickNameUser;
    private TextView user_name;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authSession();

//        try {
//            Amplify.addPlugin(new AWSApiPlugin());
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.configure(getApplicationContext());
//
////            Log.i(TAG, "Initialized Amplify");
//        } catch (AmplifyException e) {
////            Log.e(TAG, "Could not initialize Amplify", e);
//        }

//        Log.i(TAG, "onCreate: just for testing -->"+teamName);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        teamName =sharedPreferences.getString(Settings.TEAMNAME,"Team");

        createTeams();

        Objects.requireNonNull(getSupportActionBar()).setTitle("My Tasks");

        Button addButton = findViewById(R.id.addTask);
        Button allButton = findViewById(R.id.allTasks);
        userTasks=findViewById(R.id.userTasks);
        user_name = findViewById(R.id.user_name);

        // This just for testing
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i("AuthDemo", "User attributes = " + attributes.toString());
                    attributes.forEach(authUserAttribute -> {
                        if (authUserAttribute.getKey().getKeyString().equals("nickname"))
                        {
                            nickNameUser=authUserAttribute.getValue().toString();
                            Log.i(TAG,"The nickName-------->"+nickNameUser);
                        }
                    });
                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );

        user_name.setText(nickNameUser);

//        initialiseData();
//        TaskInfoList=TaskAppDatabase.getInstance(getApplicationContext()).taskDao().getAll();
//        Amplify.DataStore.query(Task.class ,
//                tasks -> {
//                    while (tasks.hasNext()) {
//                        Task eachTask = tasks.next();
//                        TaskInfoList.add(eachTask);
//                    }
//                },
//                failure -> Log.e(TAG, "Could not query DataStore", failure)
//                );

        if (teamName!="Team"){
            getTeamTasks();
        } else {
            getAllTasks();
        }


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

    private void createTeams() {
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    ArrayList<Team> teamsList = new ArrayList<>();
                    for (Team team : response.getData()) {
                        teamsList.add(team);
                    }
                    if(teamsList.size() < 3){
                        for (int i = 1; i <4 ; i++) {
                            Team team = Team.builder()
                                    .name("Team" + i)
                                    .build();
                            TeamInfoList.add(team);

                            Amplify.DataStore.save(team,
                                    success -> {//Log.i(TAG, "Saved team: " + success.item().getName());
                                },
                                    error -> { //Log.e(TAG, "Could not save team to DataStore", error);
                            }
                            );

                            Amplify.API.mutate(
                                    ModelMutation.create(team),
                                    success -> { // Log.i(TAG, "Saved team: " + success.getData().getName());
                                        },
                                    error -> { // Log.e(TAG, "Could not save team to API", error);
                                    }
                            );
                        }
                    }

                },
                error ->  { // Log.e(TAG, "could not save the teams ", error);
                }
        );
    }
     private void getTeamTasks(){
        List<Task> teamTasks=new ArrayList<>();
         Amplify.API.query(ModelQuery.list(Team.class,Team.NAME.eq(teamName)),
                 teamSuccess->{
                     if (teamSuccess.hasData())
                     {
                         for (Team team : teamSuccess.getData())
                         {
                                 teamId=team.getId();
//                                 Log.i(TAG, "getTeamId: just for testing ->"+team.getName());
                         }
                         Amplify.API.query(
                                 ModelQuery.list(Task.class,Task.TEAM_TASKS_ID.eq(teamId)),
                                 success -> {
                                     if (success.hasData()) {
                                         for (Task task : success.getData()) {
                                             teamTasks.add(task);
                                         }
                                     }
                                     Bundle bundle = new Bundle();
                                     bundle.putString("tasksList", success.toString());

                                     Message message = new Message();
                                     message.setData(bundle);

                                     handler.sendMessage(message);

                                 },
                                 error -> { //Log.e(TAG, "Could not query Api", error);
                                 }
                         );
                     }
                 }
                 , error->{
//                     Log.e(TAG,"Could not query Api",error);
         });

         handler = new Handler(Looper.getMainLooper(), msg -> {
             user_name.setText(nickNameUser);
             RecyclerView recyclerView = findViewById(R.id.tasks);
             TaskRecyclerViewAdapter taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(
                     teamTasks, position -> {

                 Intent intent1=new Intent(getApplicationContext(),TaskDetails.class);
                 intent1.putExtra("TaskTitle",teamTasks.get(position).getTitle());
                 intent1.putExtra("TaskDesc",teamTasks.get(position).getDescription());
                 intent1.putExtra("TaskState",teamTasks.get(position).getStatus().toString());
                 intent1.putExtra("TaskId",teamTasks.get(position).getId());
                 startActivity(intent1);
             });
             recyclerView.setAdapter(taskRecyclerViewAdapter);

             recyclerView.setHasFixedSize(true);
             recyclerView.setLayoutManager(new LinearLayoutManager(this));

             return true;

         });

     }
     private void getAllTasks(){
         List<Task> TaskInfoList = new ArrayList<>();
//         Log.i(TAG, "filterTeams: just for testing->"+teamName);

//         Log.i(TAG, "filterTeams: the first one-> ");
         Amplify.API.query(
                     ModelQuery.list(Task.class),
                     success -> {
                         if (success.hasData()) {
                             for (Task task : success.getData()) {
                                 TaskInfoList.add(task);
                             }
                         }
                         Bundle bundle = new Bundle();
                         bundle.putString("tasksList", success.toString());

                         Message message = new Message();
                         message.setData(bundle);

                         handler.sendMessage(message);

                     },
                     error -> { //Log.e(TAG, "Could not query Api", error);
                     }
         );

         handler = new Handler(Looper.getMainLooper(), msg -> {
             user_name.setText(nickNameUser);
             RecyclerView recyclerView = findViewById(R.id.tasks);
             TaskRecyclerViewAdapter taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(
                     TaskInfoList, position -> {

                 Intent intent1=new Intent(getApplicationContext(),TaskDetails.class);
                 intent1.putExtra("TaskTitle",TaskInfoList.get(position).getTitle());
                 intent1.putExtra("TaskDesc",TaskInfoList.get(position).getDescription());
                 intent1.putExtra("TaskState",TaskInfoList.get(position).getStatus().toString());
                 intent1.putExtra("TaskId",TaskInfoList.get(position).getId());
                 startActivity(intent1);
             });
             recyclerView.setAdapter(taskRecyclerViewAdapter);

             recyclerView.setHasFixedSize(true);
             recyclerView.setLayoutManager(new LinearLayoutManager(this));

             return true;

         });

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
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.i(TAG, "onResume: just for testing -->"+teamName);
        setUserName();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        teamName =sharedPreferences.getString(Settings.TEAMNAME,"Team");

        if (teamName!="Team"){
            getTeamTasks();
        } else {
            getAllTasks();
        }

    }

    private void authSession() {
        Amplify.Auth.fetchAuthSession(
                result -> Log.i(TAG,  result.toString()),
                error -> Log.e(TAG, error.toString())
        );
    }

    private void logout() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    authSession();
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    private void navigateToSettings() {
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }
    @SuppressLint("SetTextI18n")
    private void setUserName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        teamName =sharedPreferences.getString(Settings.TEAMNAME,"Team");
//        Log.i(TAG, "setUserName: just for testing ->"+teamName);
        userTasks.setText(sharedPreferences.getString(Settings.TEAMNAME, "Team")+"'s Tasks");
    }


//    private void initialiseData() {
//        TaskInfoList.add(new Task("Task1", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "new"));
//        TaskInfoList.add(new Task("Task2", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "assigned"));
//        TaskInfoList.add(new Task("Task3", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "in progress"));
//        TaskInfoList.add(new Task("Task4", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "complete"));
//    }

}