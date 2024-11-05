// app/src/main/java/com/task/management/workflow/iam/data/repository/IAMRepository.kt
package com.task.management.workflow.iam.data.repository

import android.util.Log
import com.task.management.workflow.common.Resource
import com.task.management.workflow.iam.data.locale.AccountDao
import com.task.management.workflow.iam.data.locale.AccountEntity
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.remote.signin.SignInRequest
import com.task.management.workflow.iam.data.remote.signin.SignInResponse
import com.task.management.workflow.iam.data.remote.signup.SignUpRequest
import com.task.management.workflow.iam.data.remote.signup.SignUpResponse
import retrofit2.Response

class IAMRepository(
    private val service: IAMService,
    private val accountDao: AccountDao
    ) {

    suspend fun saveAccount(account: AccountEntity) {
        try {
            // first validate if have another account with the same username
            val accountByUsername = getAccountByUsername(account.username)
            if (accountByUsername != null) {
                // if have another account with the same username, update it
                accountDao.updateAccount(account)
                return
            } else {
                // if not, save the new account
                accountDao.insertAccount(account)
            }
        } catch (e: Exception) {
            // Handle the exception as needed
            Log.e("IAMRepository", "Error saving account", e)
        }
    }

    suspend fun getAccountByUsername(username: String): AccountEntity? {
        return try {
            accountDao.getAccountByUsername(username)
        } catch (e: Exception) {
            // Handle the exception as needed
            Log.e("IAMRepository", "Error getting account by username", e)
            null
        }
    }

    suspend fun getAccounts(): List<AccountEntity> {
        return try {
           var accounts = accountDao.getAccounts()
            return accounts
        } catch (e: Exception) {
            // Handle the exception as needed
            Log.e("IAMRepository", "Error getting accounts", e)
            emptyList()
        }
    }


    suspend fun deleteAccount(account: AccountEntity) {
        try {
            accountDao.deleteAccount(account)
        } catch (e: Exception) {
            // Handle the exception as needed
            Log.e("IAMRepository", "Error deleting account", e)
        }
    }

    suspend fun updateAccount(account: AccountEntity) {
        try {
            accountDao.updateAccount(account)
        } catch (e: Exception) {
            // Handle the exception as needed
            Log.e("IAMRepository", "Error updating account", e)
        }
    }

    suspend fun signIn(signInRequest: SignInRequest): Resource<SignInResponse> {
        return try {
            val response = service.signIn(signInRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpResponse> {
        return try {
            val response = service.signUp(signUpRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    }
}