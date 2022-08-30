package com.mezhendosina.sgo.app.ui.container

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.announcements.AnnouncementsRepository
import java.text.SimpleDateFormat
import java.util.*

class ContainerViewModel(
    private val announcementsRepository: AnnouncementsRepository = Singleton.announcementsRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    init {
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

    }
}
