package com.roaa.mytasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
//import com.roaa.mytasks.data.Task;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity2.class.getSimpleName();
    private TextView TotalText;
    private int counter=0;
    public static final int REQUEST_CODE = 123;

    private FusedLocationProviderClient mFusedLocationClient;
    private String longIt, latIt;

    private int PERMISSION_ID = 44;

    private double latitude;
    private double longitude;

    private GoogleMap googleMap;

    String[] teams={"Team1","Team2","Team3"};
    String teamSelected;
    private Button uploadButton;
    private EditText taskTitle;
    private File file;
    String imageKey=null;

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            longIt = mLastLocation.getLongitude() + "";
            latIt = mLastLocation.getLatitude() + "";
            Log.i(TAG, "long :" + longIt + " Lat : "+ latIt);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();


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
                                            Log.i(TAG, "long :" + longIt + " Lat : "+ latIt);
                                            Log.i(TAG, "long2 :" + longitude + " Lat2 : "+ latitude);
                                            Task newTask=Task.builder().title(taskTitle.getText().toString())
                                                    .status(Status.ASSIGNED)
                                                    .teamTasksId(teamItem.getId())
                                                    .description(taskDescription.getText().toString())
                                                    .image(imageKey)
                                                    .locationLatitude(latitude+"")
                                                    .locationLongitude(longitude+"")
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        Location location=task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            LatLng coordinate = new LatLng(latitude, longitude);

                            }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }
    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
}