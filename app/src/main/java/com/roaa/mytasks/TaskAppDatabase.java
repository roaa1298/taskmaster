package com.roaa.mytasks;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.roaa.mytasks.data.Task;


@Database(entities = {Task.class}, version = 1)
public abstract class TaskAppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static TaskAppDatabase taskAppDatabase;

    public TaskAppDatabase(){}

    public static synchronized TaskAppDatabase getInstance(Context context) {

        if(taskAppDatabase == null)
        {
            taskAppDatabase = Room.databaseBuilder(context,
                    TaskAppDatabase.class, "TaskAppDatabase").allowMainThreadQueries().build();
        }
        return taskAppDatabase;
    }

}
