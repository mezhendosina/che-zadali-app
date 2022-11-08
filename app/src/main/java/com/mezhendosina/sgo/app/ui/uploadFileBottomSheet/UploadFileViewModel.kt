package com.mezhendosina.sgo.app.ui.uploadFileBottomSheet

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.Singleton.attachmentsRepository
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.requests.homework.entities.SendFileRequestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadFileViewModel(
    private val homeworkSource: HomeworkSource = Singleton.homeworkSource
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun sendFile(
        assignmentID: Int,
        filePath: Uri,
        description: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    _success.value = false
                }
                val contentResolver = Singleton.getContext().contentResolver
                val a = contentResolver.openInputStream(filePath)

                val body = a?.readBytes()?.toRequestBody("*/*".toMediaTypeOrNull())
                val path = filePath.normalizeScheme().path.toString()
                val fileName = path.substring(path.lastIndexOf("/") + 1)
                if (body != null) {
                    val part = MultipartBody.Part.createFormData("file", fileName, body)

                    homeworkSource.sendFileAttachment(
                        part,
                        SendFileRequestEntity(
                            true,
                            assignmentID,
                            description,
                            fileName
                        )
                    )
                }
                withContext(Dispatchers.IO) { a?.close() }
                withContext(Dispatchers.Main) {
                    _success.value = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            }
        }
    }

    fun editFileDescription(fileId: Int, description: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _success.value = false
                withContext(Dispatchers.IO) {
                    attachmentsRepository.editDescription(fileId, description)
                }
                _success.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.toDescription()
            }
        }
    }
}
