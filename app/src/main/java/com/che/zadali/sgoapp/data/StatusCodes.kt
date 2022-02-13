package com.che.zadali.sgoapp.data

sealed class StatusCodes {

    object Success : StatusCodes()
    object NetworkError : StatusCodes()

}
