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

package com.mezhendosina.sgo.app.model.journal

import com.mezhendosina.sgo.data.netschool.api.diary.entities.DiaryInitResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.DiaryResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.PastMandatoryEntity

interface DiarySource {

    suspend fun diaryInit(): DiaryInitResponseEntity

    suspend fun diary(diaryEntity: DiaryModelRequestEntity): DiaryResponseEntity


    suspend fun getPastMandatory(diaryEntity: DiaryModelRequestEntity): List<PastMandatoryEntity>

}