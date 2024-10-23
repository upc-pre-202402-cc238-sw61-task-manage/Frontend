package com.task.management.workflow.iam.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenProvider.getToken()

        val publicEndpoints = listOf(
            "api/v1/authentication/sign-in",
            "api/v1/authentication/sign-up"
        )

        val isPublicEndpoint = publicEndpoints.any { request.url.encodedPath.startsWith("/$it") }

        return if (!isPublicEndpoint) {
            if (!token.isNullOrEmpty()) {
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(request)
            }
        } else {
            chain.proceed(request)
        }
    }
}