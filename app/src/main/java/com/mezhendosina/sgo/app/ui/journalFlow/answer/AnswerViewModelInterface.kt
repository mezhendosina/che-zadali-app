package com.mezhendosina.sgo.app.ui.journalFlow.answer

import android.content.Context
import com.mezhendosina.sgo.app.model.answer.FileUiEntity

interface AnswerViewModelInterface {

    fun getHomework(): String
    fun getAnswer(): String?
    suspend fun sendAnswer(answerText: String?)
    suspend fun uploadFiles(context: Context)
    suspend fun downloadFiles(context: Context)
    suspend fun downloadFile(context: Context, fileUiEntity: FileUiEntity)
    fun openFile(context: Context, fileUiEntity: FileUiEntity)
    fun addFile(fileUiEntity: FileUiEntity)
    fun deleteFile(fileUiEntity: FileUiEntity)
}