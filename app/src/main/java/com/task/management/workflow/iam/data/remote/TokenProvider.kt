package com.task.management.workflow.iam.data.remote

class TokenProvider {
    private var token: String? = null

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }
}