package com.mezhendosina.sgo.app.ui.container

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.annoucements.AnnouncementsActionListener
import com.mezhendosina.sgo.app.ui.annoucements.AnnouncementsService
import com.mezhendosina.sgo.data.layouts.announcements.AnnouncementsResponseItem
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ContainerViewModel(
    private val announcementsService: AnnouncementsService,
) : ViewModel() {

    private val _announcements = MutableLiveData<List<AnnouncementsResponseItem>>()
    val announcements: LiveData<List<AnnouncementsResponseItem>> = _announcements

    private val announcementsListener: AnnouncementsActionListener = {
        _announcements.value = it
    }


    init {
        announcementsService.addListener(announcementsListener)

    }
    fun loadAnnouncements(context: Context) {
        if (Singleton.announcements.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    announcementsService.announcements()
                } catch (e: ResponseException) {
                    withContext(Dispatchers.Main) {
                        errorDialog(context, e.response.body())
                    }
                }
            }
        } else {
            _announcements.value = Singleton.announcements
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

    override fun onCleared() {
        super.onCleared()
        announcementsService.removeListener(announcementsListener)

    }
}
