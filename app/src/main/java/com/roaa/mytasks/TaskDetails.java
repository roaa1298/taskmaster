package com.roaa.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

public class TaskDetails extends AppCompatActivity {

    private static final String TAG = TaskDetails.class.getSimpleName();
    private ImageView taskImage;
    String taskImg;
    Bitmap bitmap;

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
        TextView T_title=findViewById(R.id.taskTitleView);
        taskImage = findViewById(R.id.taskImage);

        String taskId=titleIntent.getStringExtra("TaskId");
        Log.i(TAG,"The id for this task-------->"+taskId);

        Amplify.API.query(
                ModelQuery.get(Task.class,taskId),
                response -> {
                    Task currentTask = response.getData();
                    if (currentTask.getImage() != null) {
                        taskImg=currentTask.getImage();
                        Log.i(TAG,"The image key for this task-------->"+taskImg);
                        if (taskImg!=null)
                        {
                            pictureDownload(taskImg);
                        }
                    }else{
                        putDefaultPicture();
                    }
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );


        desc.setText(titleIntent.getStringExtra("TaskDesc"));
        task_state.setText(titleIntent.getStringExtra("TaskState"));
        T_title.setText(pageTitle);
    }

    private void putDefaultPicture() {
        runOnUiThread(() -> {
            taskImage.setImageResource(R.drawable.task_details);
        });
    }

    private void pictureDownload(String taskImg) {
        Amplify.Storage.downloadFile(
                taskImg,
                new File(getApplicationContext().getFilesDir() + "/download.jpg"),
                result -> {
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getPath());
                    runOnUiThread(() -> {
//                    Picasso.get().load(result.getFile().getPath()).into(taskImage);


                    bitmap= BitmapFactory.decodeFile(result.getFile().getPath());
                    taskImage.setImageBitmap(bitmap);
                    });
                },
                error -> Log.e(TAG,  "Download Failure", error)
        );
    }
}