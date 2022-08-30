package com.mezhendosina.sgo.data.requests.diary.entities

import com.mezhendosina.sgo.data.requests.homework.entities.AttachmentsResponseEntity

data class DiaryEntity(
    val diaryResponse: DiaryResponseEntity,
    val attachmentsResponse: List<AttachmentsResponseEntity>,
    val pastMandatory: List<PastMandatoryEntity>
)