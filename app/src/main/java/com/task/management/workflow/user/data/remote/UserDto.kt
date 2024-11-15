package com.task.management.workflow.user.data.remote

import com.task.management.workflow.user.domain.Roles
import com.task.management.workflow.user.domain.User

data class UserDto(
    val id: Long,
    val username: String,
    val roles: Set<Roles> = emptySet(),
    val token: String
) {
    fun toUser(): User {
        return User(
            id = this.id,
            username = this.username,
            roles = this.roles
        )
    }
}
