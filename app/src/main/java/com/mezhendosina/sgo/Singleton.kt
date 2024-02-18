/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo


import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayout
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.model.journal.DiaryStyle
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.ui.main.container.ContainerFragment
import com.mezhendosina.sgo.app.uiEntities.FilterUiEntity
import com.mezhendosina.sgo.app.uiEntities.UserUIEntity
import com.mezhendosina.sgo.app.utils.LoadStates
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.netschool.api.announcements.AnnouncementsResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.PastMandatoryEntity
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import com.mezhendosina.sgo.data.netschool.api.settings.entities.MySettingsResponseEntity
import okhttp3.internal.http2.Header

object Singleton {
    var loggedIn = false
    var at = ""
    var welcomeShowed = false
    var announcements: List<AnnouncementsResponseEntity> = emptyList()
    var selectedAnnouncement: AnnouncementsResponseEntity? = null

    val diaryStyle = MutableLiveData<String>(DiaryStyle.AS_CARD)

    var users: List<UserUIEntity> = emptyList()
    var lesson: LessonUiEntity? = null
    var pastMandatoryItem: PastMandatoryEntity? = null

    val gradesTerms = MutableLiveData<List<FilterUiEntity>>()
    var grades: List<GradesItem> = emptyList()
    val gradesRecyclerViewLoaded = MutableLiveData<Boolean>(true)

    var mySettings: MutableLiveData<MySettingsResponseEntity> = MutableLiveData()

    val weeks = mutableListOf<WeekStartEndEntity>()

    var currentWeek: Int? = null
    val loadedDiaryUiEntity: MutableList<DiaryUiEntity> = mutableListOf()
    val currentDiaryUiEntity = MutableLiveData<DiaryUiEntity>()

    var journalTabsLayout: TabLayout? = null

    val answerUpdated = MutableLiveData<Boolean>(false)


    var gradesWithWeight = false

    val updateGradeState = MutableLiveData<LoadStates>()

    val mainContainerScreen = MutableLiveData<String>(ContainerFragment.JOURNAL)

    val STATIC_HEADERS = mutableListOf(
        Header("UserAgent", "che-zadali-app v${BuildConfig.VERSION_NAME}"),
        Header("X-Requested-With", "XMLHttpRequest"),
        Header("Sec-Fetch-Site", "same-origin"),
        Header("Sec-Fetch-Mode", "cors"),
        Header("Sec-Fetch-Dest", "empty"),
        Header(
            "sec-ch-ua",
            "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"105\", \"Microsoft Edge\";v=\"105\""
        )
    )
}

