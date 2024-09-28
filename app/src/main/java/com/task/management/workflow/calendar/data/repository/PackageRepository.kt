package com.task.management.workflow.calendar.data.repository


import com.task.management.workflow.calendar.data.remote.PackageDto
import com.task.management.workflow.calendar.data.remote.PackageService
import com.task.management.workflow.calendar.data.remote.toPackage
import com.task.management.workflow.calendar.domain.CreateEventRequest
import com.task.management.workflow.calendar.domain.EventPackage
import com.task.management.workflow.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PackageRepository(private val service: PackageService) {
suspend fun getPackages(userId: Int): Resource<List<EventPackage>> = withContext(Dispatchers.IO) {
    try {
        val response = service.getEventsbyUser(userId)
        if (response.isSuccessful) {
            response.body()?.let { packageDtos: List<PackageDto> ->
                val packages = packageDtos.map { packageDto: PackageDto ->
                    packageDto.toPackage()
                }.toList()
                return@withContext Resource.Success(data = packages)
            }
        }
        return@withContext Resource.Error(message = response.message())
    } catch (e: Exception) {
        return@withContext Resource.Error(message = e.message ?: "An exception occurred")
    }
}

    suspend fun addEvent(event: CreateEventRequest): Resource<Unit> {
        return try {
            val request = CreateEventRequest(
                projectId = event.projectId,
                userId = event.userId,
                title = event.title,
                description = event.description,
                day = event.day,
                month = event.month,
                year = event.year
            )
            val response = service.addEvent(request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}