package com.task.management.workflow.iam.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("account_id")
    val accountId: Long = 0L,
    @ColumnInfo("account_username")
    val username: String,
    @ColumnInfo("account_password")
    val password: String,
)