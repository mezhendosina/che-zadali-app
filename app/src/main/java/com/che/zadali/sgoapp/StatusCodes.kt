package com.che.zadali.sgoapp

sealed class StatusCodes {

    object Success : StatusCodes()
    object NetworkError : StatusCodes()

}
