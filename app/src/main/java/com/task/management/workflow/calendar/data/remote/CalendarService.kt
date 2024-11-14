package com.task.management.workflow.calendar.data.remote


import com.task.management.workflow.calendar.domain.CreateEventRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface CalendarService {

    @GET("api/v1/events/user/{userId}")
    suspend fun getEventsByUser(@Path("userId") userId: Long): Response<List<CalendarDto>>

    @POST("api/v1/events")
    suspend fun addEvent(@Body event: CreateEventRequest): Response<Void>

    @DELETE("api/v1/events/{eventId}")
    suspend fun deleteEvent(@Path("eventId") eventId: Long): Response<Void>

    @PUT("api/v1/events/{eventId}")
    suspend fun editEvent(@Path("eventId") eventId: Long, @Body updatedEvent: CreateEventRequest): Response<Void>

}