package com.mezhendosina.sgo

import androidx.paging.PagingData
import com.mezhendosina.sgo.data.Requests
import com.mezhendosina.sgo.data.SettingsLoginData
import com.mezhendosina.sgo.data.layouts.announcements.AnnouncementsResponseItem
import com.mezhendosina.sgo.data.layouts.diary.Diary
import com.mezhendosina.sgo.data.layouts.diary.diary.DiaryResponse
import com.mezhendosina.sgo.data.layouts.schools.SchoolItem
import kotlinx.coroutines.flow.Flow

object Singleton {

    val requests = Requests()

    var at: String = ""
    var todayHomework: Diary =
        Diary(DiaryResponse("", emptyList(), "", emptyList(), "", ""), emptyList(), emptyList())
    var announcements: List<AnnouncementsResponseItem> = emptyList()


    var selectedDiary =
        Diary(DiaryResponse("", emptyList(), "", emptyList(), "", ""), emptyList(), emptyList())
    var currentWeek = 0
    var currentYearId: Int = 0
    var currentYear: String = ""
    var diary: Diary =
        Diary(DiaryResponse("", emptyList(), "", emptyList(), "", ""), emptyList(), emptyList())
    var schools = mutableListOf<SchoolItem>()

    suspend fun login(loginData: SettingsLoginData) {
        requests.login(loginData)
        val currentYearResponse = requests.yearList(at).first { !it.name.contains("(*) ") }
        currentYearId = currentYearResponse.id
    }

    const val ANNOUNCEMENTS_ID = "announcementsID"
}

