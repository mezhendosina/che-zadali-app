package com.mezhendosina.sgo.app.ui.journal

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.ErrorResponse
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.attachments.AttachmentsResponseItem
import com.mezhendosina.sgo.data.diary.diary.WeekDay
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class JournalViewModel(private val journalService: JournalService) : ViewModel() {

    private val _diary = MutableLiveData<List<WeekDay>>()
    val diary: LiveData<List<WeekDay>> = _diary

    private val _attachments = MutableLiveData<List<AttachmentsResponseItem>>()
    val attachments: LiveData<List<AttachmentsResponseItem>> = _attachments

    private val diaryListener: journalActionListener = {
        _diary.value = it
    }
    private val attachmentsListener: attachmentActionListener = {
        _attachments.value = it
    }

    fun getDiary(context: Context) {
        journalService.addListener(diaryListener, attachmentsListener)
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            try {
                journalService.loadDiary(
                    settings.currentUserId.first(),
                    Singleton.currentYearId,
                    currentWeekStart(),
                    currentWeekEnd()
                )
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, e.response.body<ErrorResponse>().message)
                }
            }
        }
    }

    fun refreshDiary(context: Context, swipeRefreshLayout: SwipeRefreshLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = true
            }
            val settings = Settings(context)
            try {
                journalService.reloadDiary(
                    settings.currentUserId.first(),
                    Singleton.currentYearId,
                    currentWeekStart(),
                    currentWeekEnd()
                )
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, e.response.body<ErrorResponse>().message)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    fun nextWeek(context: Context, swipeRefreshLayout: SwipeRefreshLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = true
            }
            val singleton = Singleton
            val settings = Settings(context)
            singleton.currentWeek += 1
            journalService.reloadDiary(
                settings.currentUserId.first(),
                Singleton.currentYearId,
                weekStart(singleton.currentWeek),
                weekEnd(singleton.currentWeek)
            )
            withContext(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    fun previousWeek(context: Context, swipeRefreshLayout: SwipeRefreshLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = true
            }
            val singleton = Singleton
            val settings = Settings(context)
            singleton.currentWeek -= 1
            println(
                "${weekStart(singleton.currentWeek)} ${weekEnd(singleton.currentWeek)}"
            )
            journalService.reloadDiary(
                settings.currentUserId.first(),
                singleton.currentYearId,
                weekStart(singleton.currentWeek),
                weekEnd(singleton.currentWeek)
            )
            withContext(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        journalService.removeListener(diaryListener, attachmentsListener)
    }

    @SuppressLint("SimpleDateFormat")
    private fun currentWeekStart(): String {
        val s = SimpleDateFormat("w.yyyy").format(Date().time)
        val a = SimpleDateFormat("w.yyyy").parse(s)
        return SimpleDateFormat("yyyy-MM-dd").format(a!!)
    }

    @SuppressLint("SimpleDateFormat")
    private fun currentWeekEnd(): String {
        val s = SimpleDateFormat("w.yyyy").format(Date().time)
        val a = SimpleDateFormat("w.yyyy").parse(s)!!.time + 6 * 24 * 60 * 60 * 1000
        return SimpleDateFormat("yyyy-MM-dd").format(a)
    }

    @SuppressLint("SimpleDateFormat")
    private fun weekStart(week: Int): String {
        val s = SimpleDateFormat("w.yyyy").format(Date().time)
        val a = SimpleDateFormat("w.yyyy").parse(s)!!.time + week * 7 * 24 * 60 * 60 * 1000
        return SimpleDateFormat("yyyy-MM-dd").format(a)
    }

    @SuppressLint("SimpleDateFormat")
    private fun weekEnd(week: Int): String {
        val s = SimpleDateFormat("w.yyyy").format(Date().time)
        val a =
            SimpleDateFormat("w.yyyy").parse(s)!!.time + week * 7 * 24 * 60 * 60 * 1000 + 6 * 24 * 60 * 60 * 1000
        return SimpleDateFormat("yyyy-MM-dd").format(a)
    }
}