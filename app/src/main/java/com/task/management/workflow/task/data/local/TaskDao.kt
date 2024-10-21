package com.task.management.workflow.task.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.task.management.workflow.task.domain.TaskStatus

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(taskEntity: TaskEntity)

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks")
    suspend fun fetchAll(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE task_id =:taskId")
    suspend fun fetchByTaskId(taskId: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE task_name =:name")
    suspend fun fetchByName(name: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE assigned_user =:userId")
    suspend fun fetchByUserId(userId: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE (:status IS NULL OR status = :status)")
    suspend fun fetchByStatus(status: TaskStatus?): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE assigned_project = :projectId AND (:status IS NULL OR status = :status)")
    suspend fun fetchByProjectIdAndStatus(projectId: Int, status: TaskStatus?): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE assigned_user = :userId AND assigned_project = :projectId AND (:status IS NULL OR status = :status)")
    suspend fun fetchByProjectIdAndUserIdAndStatus(userId: Int, projectId: Int, status: TaskStatus?): List<TaskEntity>

}