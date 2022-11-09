package com.mezhendosina.sgo.app

open class AppException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class ConnectionException(cause: Throwable) : AppException(cause = cause)

/**
 * Server error exception
 */
open class BackendException(
    val code: Int,
    message: String
) : AppException(message)

class ParseBackendResponseException(
    cause: Throwable
) : AppException(cause = cause)

class TooManyCountGrades : AppException("Too many count grades")

fun Exception.toDescription(): String {
    println(this.stackTraceToString())
    return when (this) {
        is BackendException -> this.message.toString()
        is ConnectionException -> "Нет подключения к интернету"
        is ParseBackendResponseException -> "Сервер отправил непонятный ответ"
        else -> "Что-то пошло не так"
    }
}

