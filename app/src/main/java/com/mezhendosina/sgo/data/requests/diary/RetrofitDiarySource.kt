package com.mezhendosina.sgo.data.requests.diary

import com.mezhendosina.sgo.app.model.journal.DiaryModelRequestEntity
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.data.requests.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.requests.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.diary.entities.*

class RetrofitDiarySource(config: RetrofitConfig) : BaseRetrofitSource(config), DiarySource {

    private val diaryApi = retrofit.create(DiaryApi::class.java)

    override suspend fun diaryInit(): DiaryInitResponseEntity =
        wrapRetrofitExceptions {
            diaryApi.diaryInit()
        }

    override suspend fun diary(diaryEntity: DiaryModelRequestEntity): DiaryResponseEntity =
        wrapRetrofitExceptions {
            diaryApi.diary(
                studentId = diaryEntity.studentId,
                weekEnd = diaryEntity.weekEnd,
                weekStart = diaryEntity.weekStart,
                yearId = diaryEntity.yearId,
                withLaAssigns = true
            )
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