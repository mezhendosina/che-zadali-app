package com.mezhendosina.sgo.data.layouts.diary

import com.mezhendosina.sgo.data.layouts.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.layouts.diary.diary.DiaryResponse
import com.mezhendosina.sgo.data.layouts.pastMandatory.PastMandatoryItem

data class Diary(
    val diaryResponse: DiaryResponse,
    val attachmentsResponse: List<AttachmentsResponseItem>,
    val pastMandatory: List<PastMandatoryItem>
)