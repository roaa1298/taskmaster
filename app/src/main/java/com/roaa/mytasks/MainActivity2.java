package com.roaa.mytasks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getSimpleName();
    private TextView TotalText;
    private int counter=0;
    public static final int REQUEST_CODE = 123;

    String[] teams={"Team1","Team2","Team3"};
    String teamSelected;
    private Button uploadButton;
    private EditText taskTitle;
    private File file;
    String imageKey=null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent2=getIntent();
        String imageAction= intent2.getAction();
        String imageType= intent2.getType();

        if (Intent.ACTION_SEND.equals(imageAction) && imageType != null)
        {
            if (imageType.equals("image/*"))
            {
                handleSendImage(intent2);
            }
        }


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
        uploadButton = findViewById(R.id.upload);
        TotalText=findViewById(R.id.total);
        taskTitle = findViewById(R.id.task_title);
        EditText taskDescription=findViewById(R.id.task_desc);


        addingButton.setOnClickListener(view -> {
            counter++;
            TotalText.setText("Total Tasks: "+counter);

            Amplify.Storage.uploadFile(
                    taskTitle.getText().toString()+".jpg",
                    file,
                    result -> {
                        Log.i(TAG, "Successfully uploaded: " + result.getKey());
                        imageKey = result.getKey();
                        Amplify.API.query(
                                ModelQuery.list(Team.class, Team.NAME.eq(teamSelected)),
                                response -> {
                                    for (Team teamItem : response.getData()) {
                                        if(teamItem.getName().equals(teamSelected)){
                                            Task newTask=Task.builder().title(taskTitle.getText().toString())
                                                    .status(Status.ASSIGNED)
                                                    .teamTasksId(teamItem.getId())
                                                    .description(taskDescription.getText().toString())
                                                    .image(imageKey)
                                                    .build();

                                            Amplify.DataStore.save(newTask,
                                                    success -> { //Log.i(TAG, "Saved item: " + success.item().getTitle());
                                                    },
                                                    error -> { //Log.e(TAG, "Could not save item to DataStore", error);
                                                    }
                                            );

                                            Amplify.API.mutate(
                                                    ModelMutation.create(newTask),
                                                    success -> { //Log.i(TAG, "Saved item: " + success.getData().getTitle());
                                                    },
                                                    error -> { //Log.e(TAG, "Could not save item to API", error);
                                                    }
                                            );
                                        }
                                    }
                                },
                                error -> { //Log.e(TAG, error.toString(), error);
                                }
                        );

                    },
                    storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
            );



//            Task task=new Task(taskTitle.getText().toString(),taskDescription.getText().toString(),"assigned");
//            Long newTask = TaskAppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);
//            System.out.println("----------- Task ID: "+newTask+" -------------------");
            Toast.makeText(this,"submitted!",Toast.LENGTH_SHORT).show();
        });

        uploadButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent, REQUEST_CODE);
        });

    }

    private void handleSendImage(Intent intent2) {
        Uri imageUri =  intent2.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            try {
                Bitmap bitmap = getBitmapFromUri(imageUri);
                String tit;
                if (taskTitle!=null)
                {
                    tit = taskTitle.getText().toString();
                } else {
                    tit="task";
                }

                file = new File(getApplicationContext().getFilesDir(), tit+".jpg");
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }

        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);

                try {
                    Bitmap bitmap = getBitmapFromUri(currentUri);
                    String tit = taskTitle.getText().toString();
                    if (tit==null || tit.length()==0)
                    {
                        tit="task";
                    }

                    file = new File(getApplicationContext().getFilesDir(), tit+".jpg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }
}