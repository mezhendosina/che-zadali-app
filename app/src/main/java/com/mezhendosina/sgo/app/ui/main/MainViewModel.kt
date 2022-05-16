package com.mezhendosina.sgo.app.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.ErrorResponse
import com.mezhendosina.sgo.data.announcements.AnnouncementsResponseItem
import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.diary.diary.Lesson
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val todayHomeworkService: TodayHomeworkService,
    private val announcementsService: AnnouncementsService,
    private val gradeServices: GradeService
) : ViewModel() {
    private val _todayHomework = MutableLiveData<List<Lesson>>()
    val todayHomework: LiveData<List<Lesson>> = _todayHomework

    private val _todayAttachments = MutableLiveData<List<AttachmentsResponseItem>>()
    val todayAttachments: LiveData<List<AttachmentsResponseItem>> = _todayAttachments

    private val todayListener: TodayActionListener = {
        _todayHomework.value = it
    }

    private val todayAttachmentsListener: TodayAttachmentsListener = {
        _todayAttachments.value = it
    }

    private val _announcements = MutableLiveData<List<AnnouncementsResponseItem>>()
    val announcements: LiveData<List<AnnouncementsResponseItem>> = _announcements

    private val announcementsListener: AnnouncementsActionListener = {
        _announcements.value = it
    }

    fun loadTodayHomework(context: Context) {
        todayHomeworkService.addListener(todayListener)

        if (Singleton.todayHomework.diaryResponse.weekDays.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    todayHomeworkService.todayHomework()
                } catch (e: ResponseException) {
                    withContext(Dispatchers.Main) {
                        errorDialog(context, e.response.body<ErrorResponse>().message)
                    }
                }
            }
        } else {
            _todayHomework.value = Singleton.todayHomework.diaryResponse.weekDays[0].lessons
            _todayAttachments.value = Singleton.todayHomework.attachmentsResponse
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun todayDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        val date = if (day == Calendar.SUNDAY) {
            Date().time + 86400000
        } else {
            Date().time
        }
        return SimpleDateFormat("Сегодня EE, dd MMMM", Locale("ru", "RU")).format(date)
    }


    fun loadAnnouncements(context: Context) {
        announcementsService.addListener(announcementsListener)
        if (Singleton.announcements.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    announcementsService.announcements()
                } catch (e: ResponseException) {
                    withContext(Dispatchers.Main) {
                        errorDialog(context, e.response.body<ErrorResponse>().message)
                    }
                }
            }
        } else {
            _announcements.value = Singleton.announcements
        }
    }


    fun refreshAll(context: Context, swipeRefreshLayout: SwipeRefreshLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            swipeRefreshLayout.isRefreshing = true

            try {
                todayHomeworkService.todayHomework()
                announcementsService.announcements()
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, e.response.body<ErrorResponse>().message)
                }
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        todayHomeworkService.removeListener(todayListener)
        announcementsService.removeListener(announcementsListener)
    }
}