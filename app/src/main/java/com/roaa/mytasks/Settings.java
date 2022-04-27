package com.roaa.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class Settings extends AppCompatActivity {

    private EditText userName;
    public static final String USERNAME = "userN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        userName=findViewById(R.id.username);
        Button saveBtn = findViewById(R.id.save);

        saveBtn.setOnClickListener(view -> {
            saveUserName();
            View view2 = this.getCurrentFocus();
            if (view2 != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!saveBtn.isEnabled()) {
                    saveBtn.setEnabled(true);
                }

                if (editable.toString().length() == 0){
                    saveBtn.setEnabled(false);
                }
            }
        });

    }

    private void saveUserName() {
        String UName = userName.getText().toString();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

        preferenceEditor.putString(USERNAME, UName);
        preferenceEditor.apply();

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }
}