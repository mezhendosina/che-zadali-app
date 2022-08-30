package com.mezhendosina.sgo.app.model.journal

import com.mezhendosina.sgo.data.requests.diary.entities.*

interface DiarySource {

    suspend fun diaryInit(): DiaryInitResponseEntity

    suspend fun diary(diaryEntity: DiaryModelRequestEntity): DiaryResponseEntity


    suspend fun getPastMandatory(diaryEntity: DiaryModelRequestEntity): List<PastMandatoryEntity>

}