package com.task.management.workflow.calendar.data.remote


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface PackageService {
    @GET("api/v1/events/user/{userId}")
    suspend fun getEventsbyUser(@Path("userId") userId: Int): Response<List<PackageDto>>
}