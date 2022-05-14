package com.mezhendosina.sgo

import android.annotation.SuppressLint
import com.mezhendosina.sgo.data.Requests
import com.mezhendosina.sgo.data.SettingsLoginData
import com.mezhendosina.sgo.data.announcements.AnnouncementsResponseItem
import com.mezhendosina.sgo.data.diary.Diary
import com.mezhendosina.sgo.data.diary.diary.DiaryResponse
import com.mezhendosina.sgo.data.schools.SchoolItem
import java.text.SimpleDateFormat
import java.util.*

object Singleton {

    val requests = Requests()

    var at: String = ""
    var todayHomework: Diary =
        Diary(DiaryResponse("", emptyList(), "", emptyList(), "", ""), emptyList())
    var announcements: List<AnnouncementsResponseItem> = emptyList()

    var currentWeek = currentWeek()
    var currentYear: Int = 0
    var diary: Diary =
        Diary(DiaryResponse("", emptyList(), "", emptyList(), "", ""), emptyList())
    var schools = mutableListOf<SchoolItem>()

    suspend fun login(loginData: SettingsLoginData) {
        requests.login(loginData)

        currentYear = requests.yearList(at).first { !it.name.contains("(*) ") }.id
    }

    @SuppressLint("SimpleDateFormat")
    fun currentWeek(): Int {
        return SimpleDateFormat("w").format(Date().time).toInt()
    }
}
