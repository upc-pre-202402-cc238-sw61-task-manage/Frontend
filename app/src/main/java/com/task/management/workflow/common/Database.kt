package com.task.management.workflow.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.management.workflow.iam.data.local.AccountDao
import com.task.management.workflow.iam.data.local.AccountEntity
import com.task.management.workflow.project.data.local.ProjectDao
import com.task.management.workflow.project.data.local.ProjectEntity
import com.task.management.workflow.task.data.local.TaskDao
import com.task.management.workflow.task.data.local.TaskEntity

@Database(
    entities = [ProjectEntity::class, TaskEntity::class, AccountEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun getProjectDao(): ProjectDao
    abstract fun getTaskDao(): TaskDao
    abstract fun getAccountDao(): AccountDao
}