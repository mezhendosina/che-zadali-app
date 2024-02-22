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

package com.mezhendosina.sgo.data.netschool.api.announcements

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.data.netschool.NetSchoolExpectedResults
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitAnnouncementsSource
    @Inject
    constructor(
        config: RetrofitConfig,
    ) : BaseRetrofitSource(config), AnnouncementsSource {
        private val announcementsApi = retrofit.create(AnnouncementsApi::class.java)

        override suspend fun getAnnouncements(): List<AnnouncementsResponseEntity> =
            if (!BuildConfig.DEBUG) {
                wrapRetrofitExceptions {
                    announcementsApi.getAnnouncements()
                }
            } else {
                val itemsListType: Type =
                    object : TypeToken<List<AnnouncementsResponseEntity>>() {}.type
                Gson().fromJson(NetSchoolExpectedResults.announcements, itemsListType)
            }
    }
