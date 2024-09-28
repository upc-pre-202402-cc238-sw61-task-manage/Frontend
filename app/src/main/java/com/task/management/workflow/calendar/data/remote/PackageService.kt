package com.task.management.workflow.calendar.data.remote


import com.task.management.workflow.calendar.domain.CreateEventRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface PackageService {

    @GET("api/v1/events/user/{userId}")
    suspend fun getEventsbyUser(@Path("userId") userId: Int): Response<List<PackageDto>>

    @POST("api/v1/events")
    suspend fun addEvent(@Body event: CreateEventRequest): Response<Void>
}