package com.task.management.workflow.iam.data.remote

object TokenProvider {
    private var token: String? = null

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }
}