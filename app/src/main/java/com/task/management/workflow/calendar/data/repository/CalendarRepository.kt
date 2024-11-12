package com.task.management.workflow.calendar.data.repository


import com.task.management.workflow.calendar.data.remote.CalendarDto
import com.task.management.workflow.calendar.data.remote.CalendarService
import com.task.management.workflow.calendar.data.remote.toPackage
import com.task.management.workflow.calendar.domain.CreateEventRequest
import com.task.management.workflow.calendar.domain.EventPackage
import com.task.management.workflow.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CalendarRepository(private val service: CalendarService) {

    suspend fun getPackages(userId: Int): Resource<List<EventPackage>> = withContext(Dispatchers.IO) {
        try {
            val response = service.getEventsByUser(userId)
            if (response.isSuccessful) {
                response.body()?.let { calendarDtos: List<CalendarDto> ->
                    val packages = calendarDtos.map { calendarDto: CalendarDto ->
                        calendarDto.toPackage()
                    }.toList()
                    return@withContext Resource.Success(data = packages)
                }
            }
            return@withContext Resource.Error(message = response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An exception occurred getPackages()")
        }
    }

    suspend fun addEvent(event: CreateEventRequest): Resource<Unit> {
        return try {
            val request = CreateEventRequest(
                projectId = event.projectId,
                userId = event.userId,
                title = event.title,
                description = event.description,
                dueDate = event.dueDate
            )
            val response = service.addEvent(request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred addEvent()")
        }
    }

    suspend fun deleteEvent(eventId: Int): Resource<Unit>{
        return try {
            val response = service.deleteEvent(eventId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred deleteEvent()")
        }
    }

    suspend fun editEvent(eventId: Int, event: CreateEventRequest): Resource<Unit>{
        return try {

            val response = service.editEvent(eventId, event)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred editEvent()")
        }
    }


}