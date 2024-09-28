package com.task.management.workflow.project.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("SELECT * FROM projects")
    suspend fun getProjects(): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE project_id = :projectId")
    suspend fun getProjectById(projectId: Long): ProjectEntity?

    @Query("SELECT * FROM projects WHERE project_name = :projectName")
    suspend fun getProjectByName(projectName: String): ProjectEntity?

    @Query("SELECT * FROM projects WHERE project_description = :projectDescription")
    suspend fun getProjectByDescription(projectDescription: String): ProjectEntity?
}