package com.task.management.workflow.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.management.workflow.project.data.local.ProjectDao
import com.task.management.workflow.project.data.local.ProjectEntity

@Database(
    entities = [ProjectEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun getProjectDao(): ProjectDao
}