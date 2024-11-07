package com.task.management.workflow.profiles.data.remote.dataprofile

data class ProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val userId: Long
)
