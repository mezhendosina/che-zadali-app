/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.data.netschool.base

import com.google.gson.JsonParseException
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
open class BaseRetrofitSource
@Inject constructor(retrofitConfig: RetrofitConfig) {
    val retrofit = retrofitConfig.retrofit
    private val errorAdapter = retrofitConfig.gson.getAdapter(Error::class.java)

    suspend fun <T> wrapRetrofitExceptions(
        requireLogin: Boolean = true,
        debug: Boolean = false,
        debugData: T? = null,
        block: suspend () -> T
    ): T {
        if (debug && debugData != null) {
            return debugData
        }
        if (Singleton.loggedIn || !requireLogin) {
            return try {
                block()
            } catch (e: JsonParseException) {
                throw ParseBackendResponseException(e)
            } catch (e: HttpException) {
                throw createBackendException(e)
            } catch (e: SocketTimeoutException) {
                throw TimeOutError(e)
            } catch (e: IOException) {
                throw ConnectionException(e)
            }
        } else {
            delay(400)
            return wrapRetrofitExceptions { block() }
        }
    }

    private fun createBackendException(e: HttpException): Exception {
        return try {
            val errorBody = errorAdapter.fromJson(e.message())

            BackendException(errorBody.message!!)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }
}
