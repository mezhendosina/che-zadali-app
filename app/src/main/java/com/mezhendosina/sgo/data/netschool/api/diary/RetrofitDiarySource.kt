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

package com.mezhendosina.sgo.data.netschool.api.diary

import com.mezhendosina.sgo.app.model.journal.DiaryModelRequestEntity
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.data.netschool.api.diary.entities.DiaryInitResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.DiaryResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.PastMandatoryEntity
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig

class RetrofitDiarySource(config: RetrofitConfig) : BaseRetrofitSource(config), DiarySource {

    private val diaryApi = retrofit.create(DiaryApi::class.java)

    override suspend fun diaryInit(): DiaryInitResponseEntity =
        wrapRetrofitExceptions {
            diaryApi.diaryInit()
        }

    override suspend fun diary(diaryEntity: DiaryModelRequestEntity): DiaryResponseEntity =
        wrapRetrofitExceptions {
//            if (!BuildConfig.DEBUG)
            diaryApi.diary(
                studentId = diaryEntity.studentId,
                weekEnd = diaryEntity.weekEnd,
                weekStart = diaryEntity.weekStart,
                yearId = diaryEntity.yearId,
                withLaAssigns = true
            )
//                            else {
//                Gson().fromJson(TestData.DIARY, DiaryResponseEntity::class.java)
//            }
        }

    override suspend fun getPastMandatory(diaryEntity: DiaryModelRequestEntity): List<PastMandatoryEntity> =
        wrapRetrofitExceptions {
            diaryApi.getPastMandatory(
                diaryEntity.studentId,
                diaryEntity.weekEnd,
                diaryEntity.weekStart,
                diaryEntity.yearId
            )
        }
}