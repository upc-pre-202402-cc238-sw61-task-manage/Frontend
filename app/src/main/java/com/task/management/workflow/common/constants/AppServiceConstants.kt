package com.task.management.workflow.common.constants

import android.content.Context
import androidx.room.Room
import com.task.management.workflow.common.Database
import com.task.management.workflow.iam.data.remote.AuthInterceptor
import com.task.management.workflow.iam.data.remote.TokenProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppServiceConstants {
    private val tokenProvider = TokenProvider()
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenProvider))
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(UrlConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun provideDao(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "workflow-db").build()
    }
    fun provideTokenProvider(): TokenProvider {
        return this.tokenProvider
    }
}