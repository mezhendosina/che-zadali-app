package com.mezhendosina.sgo.data.requests.base

import com.google.gson.JsonParseException
import com.mezhendosina.sgo.app.BackendException
import com.mezhendosina.sgo.app.ConnectionException
import com.mezhendosina.sgo.app.ParseBackendResponseException
import com.mezhendosina.sgo.data.Error
import okio.IOException
import retrofit2.HttpException

open class BaseRetrofitSource(retrofitConfig: RetrofitConfig) {
    val retrofit = retrofitConfig.retrofit

    private val errorAdapter = retrofitConfig.gson.getAdapter(Error::class.java)

    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: JsonParseException) {
            throw ParseBackendResponseException(e)
        } catch (e: HttpException) {
            throw createBackendException(e)
        } catch (e: IOException) {
            throw ConnectionException(e)
        }
    }

    private fun createBackendException(e: HttpException): Exception {
        return try {
            val errorBody = errorAdapter.fromJson(e.response()!!.errorBody()!!.string())

            BackendException(e.code(), errorBody.message)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

}
