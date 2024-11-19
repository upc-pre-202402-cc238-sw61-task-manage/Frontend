package com.task.management.workflow.profiles.data.remote.dataprofile

data class ProfileResponse(
    val id : Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val userId: Long,
    val profilePhoto : String,
)
