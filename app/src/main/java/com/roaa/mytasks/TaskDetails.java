package com.roaa.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.predictions.models.LanguageType;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class TaskDetails extends AppCompatActivity {

    private static final String TAG = TaskDetails.class.getSimpleName();
    private ImageView taskImage;
    private ImageButton descReader;
    private ImageView translate;
    String taskImg;
    Bitmap bitmap;
    boolean flag;
    private final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        flag=false;

        Intent titleIntent = getIntent();
        String pageTitle=titleIntent.getStringExtra("TaskTitle");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(pageTitle);


        TextView desc=findViewById(R.id.desc);
        TextView task_state=findViewById(R.id.task_state);
        TextView T_title=findViewById(R.id.taskTitleView);
        taskImage = findViewById(R.id.taskImage);
        descReader=findViewById(R.id.read);
        translate=findViewById(R.id.translate);

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

        descReader.setOnClickListener(view -> {
            Amplify.Predictions.convertTextToSpeech(
                    desc.getText().toString(),
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e("MyAmplifyApp", "Conversion failed", error)
            );
        });

        translate.setOnClickListener(view -> {
            flag= !flag;
            if (flag) {
                Amplify.Predictions.translateText(
                        desc.getText().toString(),
                        LanguageType.ENGLISH,
                        LanguageType.ARABIC,
                        result -> {
                            Log.i("MyAmplifyApp", result.getTranslatedText());
                            desc.setText(result.getTranslatedText());
                        },
                        error -> Log.e("MyAmplifyApp", "Translation failed", error)
                );
            } else {
                Amplify.Predictions.translateText(
                        desc.getText().toString(),
                        LanguageType.ARABIC,
                        LanguageType.ENGLISH,
                        result -> {
                            Log.i("MyAmplifyApp", result.getTranslatedText());
                            desc.setText(result.getTranslatedText());
                        },
                        error -> Log.e("MyAmplifyApp", "Translation failed", error)
                );
            }
        });



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
    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
}