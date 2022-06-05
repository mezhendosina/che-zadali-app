package com.mezhendosina.sgo.data.diary

import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.diary.diary.DiaryResponse
import com.mezhendosina.sgo.data.pastMandatory.PastMandatoryItem

data class Diary(
    val diaryResponse: DiaryResponse,
    val attachmentsResponse: List<AttachmentsResponseItem>,
    val pastMandatory: List<PastMandatoryItem>
)