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

package com.mezhendosina.sgo.app.utils

import android.content.ActivityNotFoundException

open class AppException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class ConnectionException(cause: Throwable) : AppException(cause = cause)
class TimeOutError(cause: Throwable) : AppException(cause = cause)

class PermissionNotGranted : RuntimeException()

/**
 * Server error exception
 */
open class BackendException(
    message: String
) : AppException(message)

class ParseBackendResponseException(
    cause: Throwable
) : AppException(cause = cause)


fun Exception.toDescription(): String {
    println(this.stackTraceToString())
    return when (this) {
        is BackendException -> this.message.toString()
        is ConnectionException -> "Нет подключения к интернету"
        is ParseBackendResponseException -> "Сервер отправил непонятный ответ"
        is TimeOutError -> "Превышено время ожидания ответа от сервера"
        is ActivityNotFoundException -> "Похоже, что на устройстве не установлено приложение для открытия этого файла"
        else -> "Что-то пошло не так"
    }
}

