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

package com.mezhendosina.sgo.data.netschool.base

import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.data.github.GithubUpdateDownloader
import com.mezhendosina.sgo.data.netschool.api.announcements.RetrofitAnnouncementsSource
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.attachments.RetrofitAttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.diary.RetrofitDiarySource
import com.mezhendosina.sgo.data.netschool.api.grades.RetrofitGradesService
import com.mezhendosina.sgo.data.netschool.api.homework.HomeworkSource
import com.mezhendosina.sgo.data.netschool.api.homework.RetrofitHomeworkSource
import com.mezhendosina.sgo.data.netschool.api.login.LoginSource
import com.mezhendosina.sgo.data.netschool.api.login.RetrofitLoginSource
import com.mezhendosina.sgo.data.netschool.api.settings.RetrofitSettingsSource
import com.mezhendosina.sgo.data.netschool.api.settings.SettingsSource

class RetrofitSourcesProvider(private val config: RetrofitConfig) : SourcesProvider {
    override fun getLoginSource(): LoginSource {
        return RetrofitLoginSource(config)
    }

    override fun getAttachmentsSource(): AttachmentsSource {
        return RetrofitAttachmentsSource(config)
    }


    override fun getDiarySource(): DiarySource {
        return RetrofitDiarySource(config)
    }

    override fun getHomeworkSource(): HomeworkSource {
        return RetrofitHomeworkSource(config)
    }

    override fun getAnnouncementsSource(): AnnouncementsSource {
        return RetrofitAnnouncementsSource(config)
    }

    override fun getSettingsSource(): SettingsSource {
        return RetrofitSettingsSource(config)
    }

    override fun getGradesSource(): GradesSource {
        return RetrofitGradesService(config)
    }

    override fun getGithubUpdateDownloader(): GithubUpdateDownloader {
        return GithubUpdateDownloader(config)
    }
}