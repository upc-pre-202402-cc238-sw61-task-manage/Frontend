package com.task.management.workflow.task.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(taskEntity: TaskEntity)

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Query("select * from tasks")
    suspend fun fetchAll(): List<TaskEntity>

    @Query("select * from tasks where task_id =:taskId")
    suspend fun fetchByTaskId(taskId: Int): TaskEntity?

    @Query("select * from tasks where task_name =:name")
    suspend fun fetchByName(name: String): TaskEntity?

    @Query("select * from tasks where assigned_user =:userId")
    suspend fun fetchByUserId(userId: Int): TaskEntity?
}