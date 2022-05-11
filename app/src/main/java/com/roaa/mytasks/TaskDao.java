package com.roaa.mytasks;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.roaa.mytasks.data.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id= :id")
    Task getTaskById(Long id);

    @Insert
    Long insertTask(Task task);
}
