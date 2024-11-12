package com.task.management.workflow.common.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserSession {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> get() = _userId

    fun setUserId(id: Long) {
        _userId.value = id
    }

    fun getUserId(): Long? {
        return _userId.value
    }

    fun clearUserId() {
        _userId.value = null
    }
}
