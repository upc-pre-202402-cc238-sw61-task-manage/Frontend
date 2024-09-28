package com.task.management.workflow.project.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProjectEntity::class], version = 1)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun getProjectDao(): ProjectDao
}