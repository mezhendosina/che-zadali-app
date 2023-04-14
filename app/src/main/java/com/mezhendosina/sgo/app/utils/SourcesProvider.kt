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

package com.mezhendosina.sgo.app.utils

import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolsSource
import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.app.model.login.LoginSource
import com.mezhendosina.sgo.app.model.settings.SettingsSource

interface SourcesProvider {

    fun getSchoolsSource(): SchoolsSource

    fun getLoginSource(): LoginSource

    fun getDiarySource(): DiarySource

    fun getHomeworkSource(): HomeworkSource

    fun getAnnouncementsSource(): AnnouncementsSource

    fun getSettingsSource(): SettingsSource

    fun getGradesSource(): GradesSource
}