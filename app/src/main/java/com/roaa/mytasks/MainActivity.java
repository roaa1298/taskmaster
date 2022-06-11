package com.roaa.mytasks;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amazonaws.mobileconnectors.cognitoauth.AuthUserSession;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
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
import com.amplifyframework.predictions.models.LanguageType;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    private Button interstitialAd;
    private Button rewardedAd;

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
        mAdView = findViewById(R.id.adView);
        interstitialAd=findViewById(R.id.interstitialAd);
        rewardedAd=findViewById(R.id.rewardedAd);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("MainPage")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .build();

        Amplify.Analytics.recordEvent(event);

        Amplify.Predictions.translateText(
                "Great job",
                LanguageType.ENGLISH,
                LanguageType.KOREAN,
                result -> Log.i("MyAmplifyApp", result.getTranslatedText()),
                error -> Log.e("MyAmplifyApp", "Translation failed", error)
        );

        interstitialAd.setOnClickListener(view -> {
            loadInterstitialAd();
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        });

        rewardedAd.setOnClickListener(view -> {
            loadRewardedAd();
            if (mRewardedAd != null) {
                Activity activityContext = MainActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        Toast.makeText(activityContext, "the amount => " + rewardAmount, Toast.LENGTH_SHORT).show();
                        Toast.makeText(activityContext, "the type => " + rewardType, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });

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

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });
                    }
                });
    }


//    private void initialiseData() {
//        TaskInfoList.add(new Task("Task1", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "new"));
//        TaskInfoList.add(new Task("Task2", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "assigned"));
//        TaskInfoList.add(new Task("Task3", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "in progress"));
//        TaskInfoList.add(new Task("Task4", "Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content.", "complete"));
//    }

}