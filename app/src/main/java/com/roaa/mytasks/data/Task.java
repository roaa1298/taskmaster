package com.roaa.mytasks.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "body")
    private final String body;

    @ColumnInfo(name = "state")
    private final String state;


    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        if (state.equals("new") || state.equals("assigned") || state.equals("in progress") || state.equals("complete"))
        {
            this.state = state;
        } else {
            this.state="new";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getState() {
        return state;
    }
}
