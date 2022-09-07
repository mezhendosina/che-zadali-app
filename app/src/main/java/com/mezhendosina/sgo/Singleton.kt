package com.mezhendosina.sgo

import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.app.SourcesProvider
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsRepository
import com.mezhendosina.sgo.app.model.chooseSchool.ChooseSchoolRepository
import com.mezhendosina.sgo.app.model.login.LoginRepository
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.container.ContainerRepository
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.app.model.login.LoginSource
import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.journal.entities.DiaryAdapterEntity
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.app.model.settings.SettingsSource
import com.mezhendosina.sgo.data.requests.announcements.AnnouncementsResponseEntity
import com.mezhendosina.sgo.data.requests.grades.entities.gradeOptions.GradeOptions
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem
import com.mezhendosina.sgo.data.requests.settings.entities.MySettingsResponseEntity
import com.mezhendosina.sgo.data.requests.other.entities.schools.SchoolItem
import com.mezhendosina.sgo.data.requests.SourceProviderHolder

object Singleton {

//    val requests = Requests()

    var at: String = ""
    var announcements: List<AnnouncementsResponseEntity> = emptyList()


    var currentYearId = MutableLiveData<Int>()
    var diaryEntity: DiaryAdapterEntity = DiaryAdapterEntity(
        "",
        emptyList(),
        "",
        emptyList(),
        "",
        "",
        emptyList()
    )
    var schools = mutableListOf<SchoolItem>()

    var gradesOptions: GradeOptions? = null
    var grades: List<GradesItem> = emptyList()

    var mySettings: MySettingsResponseEntity? = null


    private val sourcesProvider: SourcesProvider by lazy {
        SourceProviderHolder.sourcesProvider
    }

//    private val otherSourceProvider: OtherSourceProvider by lazy {
//        OtherRequests.sourcesProvider
//    }

    // --- sources
    val loginSource: LoginSource by lazy {
        sourcesProvider.getLoginSource()
    }

    val diarySource: DiarySource by lazy {
        sourcesProvider.getDiarySource()
    }

    val homeworkSource: HomeworkSource by lazy {
        sourcesProvider.getHomeworkSource()
    }

    private val announcementsSource: AnnouncementsSource by lazy {
        sourcesProvider.getAnnouncementsSource()
    }

    private val gradesSource: GradesSource by lazy {
        sourcesProvider.getGradesSource()
    }

    private val settingsSource: SettingsSource by lazy {
        sourcesProvider.getSettingsSource()
    }

    // --- repositories

    val chooseSchoolRepository: ChooseSchoolRepository by lazy {
        ChooseSchoolRepository()
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepository(loginSource, settingsSource)
    }

    val announcementsRepository by lazy {
        AnnouncementsRepository(announcementsSource)
    }

    val gradesRepository by lazy {
        GradesRepository(gradesSource)
    }

    val settingsRepository by lazy {
        SettingsRepository(settingsSource)
    }

    val containerRepository by lazy {
        ContainerRepository()
    }
//    suspend fun login(loginData: SettingsLoginData) {
//        val login = requests.login(loginData)
//        at = login.at
//        val currentYearResponse = requests.yearList(at).first { !it.name.contains("(*) ") }
//        currentYearId = currentYearResponse.id
//    }

    const val ANNOUNCEMENTS_ID = "announcementsID"
}

