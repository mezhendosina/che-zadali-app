package com.mezhendosina.sgo.app.model.journal

import android.annotation.SuppressLint
import androidx.paging.*
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.journal.entities.AdapterAssignment
import com.mezhendosina.sgo.app.model.journal.entities.AdapterWeekDay
import com.mezhendosina.sgo.app.model.journal.entities.DiaryAdapterEntity
import com.mezhendosina.sgo.app.model.journal.entities.LessonAdapter
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.requests.diary.entities.*
import com.mezhendosina.sgo.data.requests.homework.entities.AttachmentsRequestEntity
import com.mezhendosina.sgo.data.requests.homework.entities.AttachmentsResponseEntity
import java.text.SimpleDateFormat
import java.util.*

private const val PAGE_SIZE = 1
const val PLACEHOLDERS = true

class JournalPagingSource(
    private val studentId: Int,
    private val diarySource: DiarySource,
    private val homeworkSource: HomeworkSource,
) : PagingSource<Long, DiaryAdapterEntity>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, DiaryAdapterEntity> {
        val page = params.key ?: currentTime()
        return try {
            val diaryEntity = DiaryModelRequestEntity(
                studentId,
                weekStartByTime(page),
                weekEndByTime(page),
                Singleton.currentYearId.value ?: 0
            )
            diarySource.diaryInit()
            val diary = diarySource.diary(diaryEntity)
            val attachmentsId = getAttachmentIDs(diary)

            val attachments = homeworkSource.getAttachments(
                studentId,
                AttachmentsRequestEntity(attachmentsId)
            )
            val pastMandatory = diarySource.getPastMandatory(diaryEntity)

            val response = DiaryResponseMerging(
                diary,
                attachments,
                pastMandatory
            ).toAdapterEntity()

            val nextKey = page + 7 * 24 * 60 * 60 * 1000
            val prevKey = if (page == 0.toLong()) null else page - 7 * 24 * 60 * 60 * 1000
            LoadResult.Page(listOf(response), prevKey, nextKey)
        } catch (e: Exception) {
            println(e.stackTraceToString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, DiaryAdapterEntity>): Long? = null

    private fun getAttachmentIDs(diary: DiaryResponseEntity): List<Int> {
        val listAttachmentsId = mutableListOf<Int>()
        diary.weekDays.forEach { weekDay ->
            weekDay.lessons.forEach { lesson ->
                lesson.assignments?.forEach {
                    listAttachmentsId.add(it.id)
                }
            }
        }
        return listAttachmentsId
    }
}

@SuppressLint("SimpleDateFormat")
fun currentTime(): Long {
    val s = SimpleDateFormat("w.yyyy").format(Date().time)
    return SimpleDateFormat("w.yyyy").parse(s)!!.time

}

@SuppressLint("SimpleDateFormat")
fun weekStartByTime(time: Long): String {
    val s = SimpleDateFormat("w.yyyy").format(time)
    val a = SimpleDateFormat("w.yyyy").parse(s)
    return SimpleDateFormat("yyyy-MM-dd").format(a!!)
}

@SuppressLint("SimpleDateFormat")
internal fun weekEndByTime(time: Long): String {
    val s = SimpleDateFormat("w.yyyy").format(time)
    val a = SimpleDateFormat("w.yyyy").parse(s)!!.time + 6 * 24 * 60 * 60 * 1000
    return SimpleDateFormat("yyyy-MM-dd").format(a)
}

@SuppressLint("SimpleDateFormat")
internal fun dateToRussian(date: String): String {
    val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
    val locale = Locale("ru", "RU")

    return SimpleDateFormat("EEEE, dd MMMM", locale).format(a!!).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}

class DiaryResponseMerging(
    private val diaryResponse: DiaryResponseEntity,
    attachmentsResponseEntity: List<AttachmentsResponseEntity>,
    private val pastMandatoryEntity: List<PastMandatoryEntity>
) {
    var attachments = attachmentsResponseEntity.toMutableList()
    fun toAdapterEntity(): DiaryAdapterEntity =
        DiaryAdapterEntity(
            diaryResponse.className,
            diaryResponse.laAssigns,
            diaryResponse.termName,
            diaryResponse.weekDays.toAdapterList(),
            DateManipulation(diaryResponse.weekEnd).journalDate(),
            DateManipulation(diaryResponse.weekStart).journalDate(),
            pastMandatoryEntity
        )

    @JvmName("toAdapterListWeekDay")
    private fun List<WeekDay>.toAdapterList(): List<AdapterWeekDay> {
        val output = mutableListOf<AdapterWeekDay>()

        this.forEach {
            output.add(
                AdapterWeekDay(
                    dateToRussian(it.date),
                    it.lessons.toAdapterList()
                )
            )
        }
        return output
    }

    @JvmName("toAdapterListLesson")
    private fun List<Lesson>.toAdapterList(): List<LessonAdapter> {
        val output = mutableListOf<LessonAdapter>()

        this.forEach { lesson ->
            val assignmentAdapterList = lesson.assignments?.toAdapterList()

            output.add(
                LessonAdapter(
                    assignmentAdapterList,
                    assignmentAdapterList?.first { it.typeId == 3 },
                    lesson.classmeetingId,
                    lesson.day,
                    lesson.endTime,
                    lesson.isEaLesson,
                    lesson.number,
                    lesson.relay,
                    lesson.room,
                    lesson.startTime,
                    lesson.subjectName
                )
            )
        }
        return output
    }

    private fun List<Assignment>.toAdapterList(): List<AdapterAssignment> {
        val output = mutableListOf<AdapterAssignment>()

        this.forEach { assign ->
            val attachmentsList = mutableListOf<AttachmentsResponseEntity>()
            attachments.forEach { attachment ->
                if (assign.id == attachment.assignmentId) {
                    attachmentsList.add(attachment)
                }
            }

            attachments.removeAll(attachmentsList)
            output.add(
                AdapterAssignment(
                    assign.assignmentName,
                    assign.classMeetingId,
                    assign.dueDate,
                    assign.id,
                    assign.mark,
                    assign.textAnswer,
                    assign.typeId,
                    assign.weight,
                    attachmentsList
                )
            )
        }
        return output
    }
}
