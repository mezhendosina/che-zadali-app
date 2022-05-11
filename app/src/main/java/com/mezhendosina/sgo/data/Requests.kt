package com.mezhendosina.sgo.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.ui.updateDialog
import com.mezhendosina.sgo.data.announcements.AnnouncementsResponse
import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.checkUpdates.CheckUpdates
import com.mezhendosina.sgo.data.diary.Diary
import com.mezhendosina.sgo.data.diary.diary.DiaryResponse
import com.mezhendosina.sgo.data.diary.init.DiaryInit
import com.mezhendosina.sgo.data.login.LoginResponse
import com.mezhendosina.sgo.data.preLoginNotice.PreLoginNoticeResponse
import com.mezhendosina.sgo.data.yearList.YearListResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

data class GetData(
    val lt: String,
    val salt: String,
    val ver: String
)

data class LoginData(
    val LoginType: String,
    val cid: String,
    val sid: String,
    val pid: String,
    val cn: String,
    val sft: String,
    val scid: String,
    val UN: String,
    val PW: String,
    val lt: String,
    val pw2: String,
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
        install(Logging) {
            level = LogLevel.HEADERS
            logger = Logger.DEFAULT
        }
        install(ContentNegotiation) {
            gson()
        }
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
    private suspend fun getData(): GetData {
        return client.post("/webapi/auth/getdata").body()
    }

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

    suspend fun diaryInit(at: String): DiaryInit {
        return client.get("/webapi/student/diary/init") {
            headers.append("at", at)
        }.body()
    }

    suspend fun diary(
        at: String,
        studentId: Int,
        weekEnd: String,
        weekStart: String,
        yearId: Int
    ): Diary {
        val diary: DiaryResponse =
            client.get("/webapi/student/diary?studentId=$studentId&weekEnd=$weekEnd&weekStart=$weekStart&withLaAssigns=true&yearId=$yearId") {
                headers.append("at", at)
            }.body()
        val assignsId = mutableListOf<Int>()
        diary.weekDays.forEach { day ->
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
        return Diary(diary, attachments)
    }

    suspend fun preLoginNotice(): PreLoginNoticeResponse {
        return client.get("/webapi/settings/preloginnotice").body()
    }

    suspend fun announcements(at: String): AnnouncementsResponse {
        return client.get("/webapi/announcements") {
            headers.append("at", at)
        }.body()
    }

    suspend fun yearList(at: String): YearListResponse {
        val yearList: YearListResponse = client.get("/webapi/mysettings/yearlist") {
            headers.append("at", at)
        }.body()
        return yearList
    }

}

suspend fun checkUpdates(context: Context, file: File) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
    val r: CheckUpdates =
        client.get(" https://api.github.com/repos/mezhendosina/che-zadali-app/releases/latest")
            .body()
    withContext(Dispatchers.Main) {
        if (r.tagName != BuildConfig.VERSION_NAME) {
            updateDialog(context, r.body) {
                CoroutineScope(Dispatchers.IO).launch {
                    r.assets.forEach {
                        if (it.contentType == "application/vnd.android.package-archive") {
                            val response = client.get(it.browserDownloadUrl) {
                                onDownload { downloaded, total ->
                                    println("$downloaded $total")
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

private fun uriFromFile(context: Context, file: File): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    } else {
        Uri.fromFile(file)
    }
}

