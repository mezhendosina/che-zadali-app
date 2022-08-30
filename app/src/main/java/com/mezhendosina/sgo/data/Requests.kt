package com.mezhendosina.sgo.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.app.BackendException
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.ConnectionException
import com.mezhendosina.sgo.app.ParseBackendResponseException
import com.mezhendosina.sgo.app.ui.bottomSheets.UpdateBottomSheetFragment
import com.mezhendosina.sgo.data.layouts.Error
import com.mezhendosina.sgo.data.layouts.checkUpdates.CheckUpdates
import com.mezhendosina.sgo.data.layouts.schools.SchoolsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import io.ktor.util.network.*
import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest
import kotlin.text.toByteArray

// Стырено со stackOverFlow
fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

// Стырено со stackOverFlow
fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

// Стырено со stackOverFlow
fun uriFromFile(context: Context, file: File): Uri? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    } else {
        Uri.fromFile(file)
    }
//
//@Deprecated("")
//class Requests {
//    val client = HttpClient(CIO) {
//        expectSuccess = true
//        install(Logging) {
//            level = LogLevel.INFO
//            logger = Logger.SIMPLE
//        }
//        install(ContentNegotiation) {
//            gson()
//        }
//
//        install(HttpRequestRetry) {
//            retryOnException(2)
//            exponentialDelay()
//        }
//        install(HttpCookies)
//
//        defaultRequest {
//            url("https://sgo.edu-74.ru")
//            headers {
//                append(HttpHeaders.Host, "sgo.edu-74.ru")
//                append(HttpHeaders.Origin, "https://sgo.edu-74.ru")
//                append(HttpHeaders.UserAgent, "SGO app")
//                append("X-Requested-With", "XMLHttpRequest")
//                append("Sec-Fetch-Site", "same-origin")
//                append("Sec-Fetch-Mode", "cors")
//                append("Sec-Fetch-Dest", "empty")
//                append(HttpHeaders.Referrer, "https://sgo.edu-74.ru/")
//                append(
//                    "sec-ch-ua",
//                    "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Microsoft Edge\";v=\"101\""
//                )
//            }
//        }
//
//
//    }
//
//    /**
//     * Выход
//     */
//    suspend fun logout() {
//        client.submitForm("/asp/logout.asp", Parameters.build {
//            append("at", Singleton.at)
//        })
//    }
//
//
//    suspend fun diary(
//        at: String,
//        studentId: Int,
//        weekEnd: String,
//        weekStart: String,
//        yearId: Int
//    ): Diary {
//        val diary = CoroutineScope(Dispatchers.IO).async {
//            return@async client.get("/webapi/student/diary?studentId=$studentId&weekEnd=$weekEnd&weekStart=$weekStart&withLaAssigns=true&yearId=$yearId") {
//                headers.append("at", at)
//            }.body<DiaryResponseEntity>()
//        }
//
//        val pastMandatory = CoroutineScope(Dispatchers.IO).async {
//            return@async client.get("/webapi/student/diary/pastMandatory?studentId=$studentId&weekEnd=$weekEnd&weekStart=$weekStart&yearId=$yearId") {
//                headers.append("at", at)
//            }.body<List<PastMandatoryEntity>>()
//        }
//
//        val assignsId = mutableListOf<Int>()
//        diary.await().weekDays.forEach { day ->
//            day.lessons.forEach { lesson ->
//                lesson.assignments?.forEach { assign ->
//                    assignsId.add(assign.id)
//                }
//            }
//        }
//
//        val attachments =
//            client.post("https://sgo.edu-74.ru/webapi/student/diary/get-attachments?studentId=$studentId") {
//                headers.append("at", at)
//                contentType(ContentType.Application.Json)
//                setBody(AttachmentsRequestEntity(assignsId))
//            }.body<List<AttachmentsResponseEntity>>()
//        return Diary(diary.await(), attachments, pastMandatory.await())
//    }
//
//
//    suspend fun announcements(at: String): List<AnnouncementsResponseEntity> {
//        val a = client.get("/webapi/announcements") {
//            headers.append("at", at)
//        }.body<List<AnnouncementsResponseEntity>>()
//        return a
//    }
//
//
//    suspend fun yearList(at: String): YearListResponse =
//        client.get("/webapi/mysettings/yearlist") {
//            headers.append("at", at)
//        }.body()
//
//
//    suspend fun downloadAttachment(
//        context: Context,
//        at: String,
//        id: Int,
//        name: String,
//        progressState: MutableLiveData<Int>
//    ) {
//        val request = client.get("/webapi/attachments/$id") {
//            headers.append("at", at)
//            onDownload { downloaded, total ->
//                withContext(Dispatchers.Main) {
//                    progressState.value = (downloaded * 100 / total).toInt()
//                }
//            }
//        }
//        val file = File(context.getExternalFilesDir(null), name)
//        file.writeBytes(request.body())
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(uriFromFile(context, file), request.headers["Content-Type"])
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//        context.startActivity(intent)
//    }
//
//
//    suspend fun loadAssign(at: String, studentId: Int, assignId: Int): AssignResponseEntity =
//        client.get("/webapi/student/diary/assigns/$assignId?studentId=$studentId") {
//            headers.append("at", at)
//        }.body()
//
//
//    suspend fun loadTypes(): List<AssignmentTypesResponseEntity> =
//        client.get("/webapi/grade/assignment/types?all=false").body()
//
//    suspend fun sendAnswer(at: String, studentId: Int, assignmentId: Int, answer: String) =
//        client.post(
//            "/webapi/assignments/$assignmentId/answers?studentId=$studentId",
//        ) {
//            contentType(ContentType.Application.Json)
//            headers.append("at", at)
//            setBody(answer)
//        }
//
//    suspend fun getMySettings(at: String): MySettingsResponseEntity =
//        client.get("/webapi/mysettings") { headers.append("at", at) }.body()
//
//    suspend fun sendMySettings(at: String, mySettingsRequestEntity: MySettingsRequestEntity) =
//        client.post("/webapi/mysettings/") {
//            headers.append("at", at)
//            contentType(ContentType.Application.Json)
//            setBody(mySettingsRequestEntity)
//        }
//
//    suspend fun loadPhoto(at: String, userId: Int, file: File) {
//        client.prepareGet("/webapi/users/photo?at=$at&ver=1655623298878&userId=$userId") {
//            accept(ContentType.Any)
//        }.execute { httpResponse ->
//            val channel: ByteReadChannel = httpResponse.body()
//            while (!channel.isClosedForRead) {
//                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
//                while (!packet.isEmpty) {
//                    val bytes = packet.readBytes()
//                    file.appendBytes(bytes)
//                }
//            }
//        }
//    }
//
//    suspend fun changePassword(oldPasswordMD5: String, newPasswordMD5: String, userId: Int) =
//        client.post("/webapi/users/$userId/password") {
//            headers.append("at", Singleton.at)
//            contentType(ContentType.Application.Json)
//            setBody(ChangePasswordEntity(oldPasswordMD5, newPasswordMD5))
//        }
//
//    suspend fun getParentInfoLetter(at: String): String =
//        client.submitForm("/asp/Reports/ReportParentInfoLetter.asp", Parameters.build {
//            append("at", at)
//            append("RPNAME", "Информационное письмо для родителей")
//            append("RPTID", "ParentInfoLetter")
//        }).body()
//
//    suspend fun getGrades(
//        at: String,
//        pclid: String,
//        reportType: String,
//        termid: String,
//        studentId: String
//    ): List<GradesItem> {
//        val report = client.submitForm("/asp/Reports/ParentInfoLetter.asp", Parameters.build {
//            append("LoginType", "0")
//            append("AT", at)
//            append("PP", "/asp/Reports/ReportParentInfoLetter.asp")
//            append("BACK", "/asp/Reports/ReportParentInfoLetter.asp")
//            append("ThmID", "")
//            append("RPTID", "ParentInfoLetter")
//            append("A", "")
//            append("NA", "")
//            append("TA", "")
//            append("RT", "")
//            append("RP", "")
//            append("dtWeek", "")
//            append("PCLID", pclid)
//            append("ReportType", reportType)
//            append("TERMID", termid)
//            append("SID", studentId)
//        })
//
//        return GradesFromHtml().extractGrades(report.body())
//    }
//
//    suspend fun changeProfilePhoto(at: String, userId: Int, photo: File) {
//        client.post("asp/SetupSchool/PhotoSave.asp?at=$at&ver=1657302916517&_AJAXCALL_=1") {
//            setBody(
//                MultiPartFormDataContent(
//                    formData {
//                        append("fileName", photo.name)
//                        append("userId", userId)
//                        append("file", photo.readBytes(), Headers.build {
//                            append(HttpHeaders.ContentType, ContentType.Image.Any)
//                        })
//                    },
//                    "WebAppBoundary"
//                )
//            )
//        }
//    }
//}
//

suspend fun checkUpdates(
    context: Context,
    file: File,
    downloadProgress: MutableLiveData<Int>,
    supportFragmentManager: FragmentManager
) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
    val r: CheckUpdates =
        client.get("https://api.github.com/repos/mezhendosina/che-zadali-app/releases/latest")
            .body()
    withContext(Dispatchers.Main) {
        if (r.tagName != BuildConfig.VERSION_NAME) {
            val modalSheet = UpdateBottomSheetFragment(r.body) {
                CoroutineScope(Dispatchers.IO).launch {
                    r.assets.forEach {
                        if (it.contentType == "application/vnd.android.package-archive") {
                            val response = client.get(it.browserDownloadUrl) {
                                onDownload { downloaded, total ->
                                    withContext(Dispatchers.Main) {
                                        downloadProgress.value = (downloaded * 100 / total).toInt()
                                    }
                                }
                            }.body<ByteArray>()

                            file.writeBytes(response)
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(
                                    uriFromFile(context, file),
                                    "application/vnd.android.package-archive"
                                )
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            }
            modalSheet.show(supportFragmentManager, UpdateBottomSheetFragment.TAG)
        }
    }
}

suspend fun schools(): SchoolsResponse =
    try {
        HttpClient(CIO) {
            expectSuccess = true
            install(Logging) {
                level = LogLevel.HEADERS
                logger = Logger.DEFAULT
            }
            install(ContentNegotiation) {
                gson()
            }
        }.get("https://che-zadali-server.herokuapp.com/schools").body()
    } catch (e: Exception) {
        when (e) {
            is ResponseException -> {
                try {
                    throw BackendException(e.response.status.value, e.response.body<Error>().message)
                } catch (exc: Exception) {
                    throw ParseBackendResponseException(exc)
                }
            }
            is UnresolvedAddressException -> {
                throw ConnectionException(e)
            }
            else -> {
                throw ParseBackendResponseException(e)
            }
        }
    }
