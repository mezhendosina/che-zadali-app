package com.mezhendosina.sgo.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.ui.updateDialog
import com.mezhendosina.sgo.data.announcements.AnnouncementsResponse
import com.mezhendosina.sgo.data.assignRequest.AssignResponse
import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.checkUpdates.CheckUpdates
import com.mezhendosina.sgo.data.diary.Diary
import com.mezhendosina.sgo.data.diary.diary.DiaryResponse
import com.mezhendosina.sgo.data.diary.init.DiaryInit
import com.mezhendosina.sgo.data.grades.GradeItem
import com.mezhendosina.sgo.data.homeworkTypes.TypesResponseItem
import com.mezhendosina.sgo.data.login.LoginResponse
import com.mezhendosina.sgo.data.pastMandatory.PastMandatoryItem
import com.mezhendosina.sgo.data.preLoginNotice.PreLoginNoticeResponse
import com.mezhendosina.sgo.data.schools.SchoolsResponse
import com.mezhendosina.sgo.data.studentTotal.StudentTotalResponse
import com.mezhendosina.sgo.data.yearList.YearListResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest

data class GetData(
    val lt: String,
    val salt: String,
    val ver: String
)


fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

class Requests {

    private val client = HttpClient(CIO) {
        expectSuccess = true
//        install(Logging) {
//            level = LogLevel.HEADERS
//            logger = Logger.DEFAULT
//        }
        install(ContentNegotiation) {
            gson()
        }
        install(WebSockets)
        install(HttpCookies)
        defaultRequest {
            url("https://sgo.edu-74.ru")
            headers {
                append(HttpHeaders.Host, "sgo.edu-74.ru")
                append(HttpHeaders.Origin, "https://sgo.edu-74.ru")
                append(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36"
                )
                append("X-Requested-With", "XMLHttpRequest")
                append("Sec-Fetch-Site", "same-origin")
                append("Sec-Fetch-Mode", "cors")
                append("Sec-Fetch-Dest", "empty")
                append("Referer", "https://sgo.edu-74.ru/")
                append(
                    "sec-ch-ua",
                    "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Microsoft Edge\";v=\"101\""
                )

            }
        }
    }

    /**
     *  Достать NSSESSIONID
     */
    private suspend fun loginData() {
        client.get("/webapi/logindata")
    }

    /**
     * Получить соль, версию и lt
     */
    private suspend fun getData(): GetData =
       client.post("/webapi/auth/getdata").body()


    /**
     * Вход
     * @param loginData данные для входа
     */
    suspend fun login(loginData: SettingsLoginData): LoginResponse {
        loginData()
        val getData = getData()
        val password = (getData.salt + loginData.PW).toMD5()
        val login = client.submitForm("/webapi/login", Parameters.build {
            append("LoginType", "1")
            append("cid", loginData.cid)
            append("sid", loginData.sid)
            append("pid", loginData.pid)
            append("cn", loginData.cn)
            append("sft", loginData.sft)
            append("scid", loginData.scid)
            append("UN", loginData.UN)
            append("PW", loginData.PW)
            append("lt", getData.lt)
            append("pw2", password)
            append("ver", getData.ver)
        }).body<LoginResponse>()

        Singleton.at = login.at
        return login
    }

    /**
     * Выход
     */
    suspend fun logout() {
        client.submitForm("/asp/logout.asp", Parameters.build {
            append("at", Singleton.at)
        })
    }

    suspend fun diaryInit(at: String): DiaryInit = client.get("/webapi/student/diary/init") {
            headers.append("at", at)
        }.body()


    suspend fun diary(
        at: String,
        studentId: Int,
        weekEnd: String,
        weekStart: String,
        yearId: Int
    ): Diary {
        val diary = CoroutineScope(Dispatchers.IO).async {
            return@async client.get("/webapi/student/diary?studentId=$studentId&weekEnd=$weekEnd&weekStart=$weekStart&withLaAssigns=true&yearId=$yearId") {
                headers.append("at", at)
            }.body<DiaryResponse>()
        }

        val pastMandatory = CoroutineScope(Dispatchers.IO).async {
            return@async client.get("/webapi/student/diary/pastMandatory?studentId=$studentId&weekEnd=$weekEnd&weekStart=$weekStart&yearId=$yearId") {
                headers.append("at", at)
            }.body<List<PastMandatoryItem>>()
        }

        val assignsId = mutableListOf<Int>()
        diary.await().weekDays.forEach { day ->
            day.lessons.forEach { lesson ->
                lesson.assignments?.forEach { assign ->
                    assignsId.add(assign.id)
                }
            }
        }

        val attachments =
            client.post("https://sgo.edu-74.ru/webapi/student/diary/get-attachments?studentId=$studentId") {
                headers.append("at", at)
                contentType(ContentType.Application.Json)
                setBody(AssignsId(assignsId))
            }.body<List<AttachmentsResponseItem>>()
        return Diary(diary.await(), attachments, pastMandatory.await())
    }

    suspend fun preLoginNotice(): PreLoginNoticeResponse =
        client.get("/webapi/settings/preloginnotice").body()


    suspend fun announcements(at: String): AnnouncementsResponse =
        client.get("/webapi/announcements") {
            headers.append("at", at)
        }.body()


    suspend fun yearList(at: String): YearListResponse =
        client.get("/webapi/mysettings/yearlist") {
            headers.append("at", at)
        }.body()


    suspend fun downloadAttachment(
        context: Context,
        at: String,
        id: Int,
        name: String,
        progressState: MutableLiveData<Int>
    ) {
        val request = client.get("/webapi/attachments/$id") {
            headers.append("at", at)
            onDownload { downloaded, total ->
                withContext(Dispatchers.Main) {
                    progressState.value = (downloaded * 100 / total).toInt()
                }
            }
        }
        val file = File(context.getExternalFilesDir(null), name)
        file.writeBytes(request.body())
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uriFromFile(context, file), request.headers["Content-Type"])
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }


    suspend fun loadAssign(at: String, studentId: Int, assignId: Int): AssignResponse =
        client.get("/webapi/student/diary/assigns/$assignId?studentId=$studentId") {
            headers.append("at", at)
        }.body()


    suspend fun loadTypes(): List<TypesResponseItem> =
        client.get("/webapi/grade/assignment/types?all=false").body()

    suspend fun sendAnswer(at: String, studentId: Int, assignmentId: Int, answer: String) =
        client.submitForm(
            "/webapi/assignments/$assignmentId/answers?studentId=$studentId",
            Parameters.build { append("", answer) }
        ) { headers.append("at", at) }


    suspend fun studentTotal(at: String): StudentTotalResponse =
        client.get("/webapi/reports/studenttotal") { header("at", at) }.body()

    suspend fun negotiate(at: String): NegotiateResponse =
        client.get("/WebApi/signalr/negotiate?clientProtocol=1.5&at=$at&connectionData=%5B%7B%22name%22%3A%22queuehub%22%7D%5D")
            .body()

    suspend fun gradesWebSocket(at: String, negotiateResponse: NegotiateResponse) {
        client.webSocket("/WebApi/signalr/connect?transport=webSockets&clientProtocol=1.5&at=$at&connectionToken=${negotiateResponse.connectionToken}&connectionData=%5B%7B%22name%22%3A%22queuehub%22%7D%5D7&tid=7") {
            client.get("/WebApi/signalr/start?transport=webSockets&clientProtocol=1.5&at=$at&connectionToken=${negotiateResponse.connectionToken}&connectionData=%5B%7B%22name%22%3A%22queuehub%22%7D%5D7")
            client.get("/webapi/reports/studenttotal/queue") {
                contentType(ContentType.Application.Json)
                setBody("{\"selectedData\":[{\"filterId\":\"SID\",\"filterValue\":\"472262\",\"filterText\":\"Меньшенин Евгений\"},{\"filterId\":\"PCLID\",\"filterValue\":\"1248066\",\"filterText\":\"10Б\"},{\"filterId\":\"period\",\"filterValue\":\"2022-03-01T00:00:00.000Z - 2022-05-31T00:00:00.000Z\",\"filterText\":\"01.03.2022 - 31.05.2022\"}],\"params\":[{\"name\":\"SCHOOLYEARID\",\"value\":\"631571\"},{\"name\":\"SERVERTIMEZONE\",\"value\":5},{\"name\":\"FULLSCHOOLNAME\",\"value\":\"Муниципальное бюджетное общеобразовательное учреждение \\\"Средняя общеобразовательная школа №68 г. Челябинска имени Родионова Е.Н.\\\"\"},{\"name\":\"DATEFORMAT\",\"value\":\"dd\\u0001mm\\u0001yy\\u0001.\"}]}")
            }
            this.send("""{"H":"queuehub","M":"StartTask","A":[],"I":0}""")
        }
    }
}

suspend fun checkUpdates(context: Context, file: File, downloadProgress: MutableLiveData<Int>) {
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
            updateDialog(context, r.body) {
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
        }
    }
}

suspend fun schools(): SchoolsResponse {
    return HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            level = LogLevel.HEADERS
            logger = Logger.DEFAULT
        }
        install(ContentNegotiation) {
            gson()
        }
    }.get("https://mezhendosina.pythonanywhere.com/schools").body()
}

suspend fun extractGrades(html: String): List<GradeItem> {
    return HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            level = LogLevel.HEADERS
            logger = Logger.DEFAULT
        }
        install(ContentNegotiation) {
            gson()
        }
    }.submitForm("https://mezhendosina.pythonanywhere.com/extract_grades", Parameters.build {
        append("html", html)
    }).body()
}

private fun uriFromFile(context: Context, file: File): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    } else {
        Uri.fromFile(file)
    }
}

//fun studentTotalResponseToGradeRequest(
//    studentTotalResponse: StudentTotalResponse,
//    yearId: Int
//): QueneRequest {
//    val sid = studentTotalResponse.filterSources.find { it.filterId == "SID" }
//    val pclid = studentTotalResponse.filterSources.find { it.filterId == "PCLID" }
//    val range = studentTotalResponse.filterSources.find { it.filterId == "period" }
//    return QueneRequest(
//        listOf(
//            Param("SCHOOLYEARID", yearId.toString()),
//            Param("SERVERTIMEZONE", 5),
//            Param(
//                "FULLSCHOOLNAME",
//                "Муниципальное бюджетное общеобразовательное учреждение \"Средняя общеобразовательная школа №68 г. Челябинска имени Родионова Е.Н.\""
//            ),
//            Param("DATEFORMAT", "dd\u0001mm\u0001yy\u0001.")
//        ),
//        listOf(
//            SelectedData(
//                "SID",
//                sid?.items?.find { it.value == sid.defaultValue }?.title!!,
//                sid.defaultValue
//            ),
//            SelectedData(
//                "PCLID",
//                pclid?.items?.find { it.value == pclid.defaultValue }?.title!!,
//                pclid.defaultValue
//            ),
////            SelectedData("period", "")
//        )
//    )
//}


//    suspend fun loadGrades(at: String, studentId: String): String {
//        client.submitForm("/asp/Reports/ReportParentInfoLetter.asp", Parameters.build {
//            append("at", at)
//            append("RPNAME", "Информационное письмо для родителей")
//            append("RPTID", "ParentInfoLetter")
//        })
//
//        val request = client.submitForm("/asp/Reports/ParentInfoLetter.asp", Parameters.build {
//            append("LoginType", "0")
//            append("AT", at)
//            append("PP", "/asp/Reports/ReportParentInfoLetter.asp")
//            append("BACK", "/asp/Reports/ReportParentInfoLetter.asp ")
//            append("ThmID", "")
//            append("RPTID", "ParentInfoLetter")
//            append("A", "")
//            append("NA", "")
//            append("TA", "")
//            append("RT", "")
//            append("RP", "")
//            append("dtWeek", "16.05.22")
//            append("ReportType", "1")
//            append("TERMID", "671233")
//            append("DATE", "16.05.22")
//            append("SID", studentId)
//        })
//        return request.body()
//    }