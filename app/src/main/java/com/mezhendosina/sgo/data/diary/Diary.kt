package com.mezhendosina.sgo.data.diary

import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.diary.diary.DiaryResponse

data class Diary(
    val diaryResponse: DiaryResponse,
    val attachmentsResponse: List<AttachmentsResponseItem>
)