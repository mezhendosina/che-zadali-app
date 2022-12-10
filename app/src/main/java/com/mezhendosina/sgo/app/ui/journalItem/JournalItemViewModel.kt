package com.mezhendosina.sgo.app.ui.journalItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.journal.JournalRepository
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.WeekStartEndEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class JournalItemViewModel(
    private val journalRepository: JournalRepository = Singleton.journalRepository
) : ViewModel() {

    private val _week = MutableLiveData<DiaryUiEntity>()
    val week: LiveData<DiaryUiEntity> = _week

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun getWeek(weekStart: String?, weekEnd: String?) {
        val settings = Settings(Singleton.getContext())
        withContext(Dispatchers.Main) {
            _isLoading.value = true
            _errorMessage.value = ""
        }
        withContext(Dispatchers.IO) {
            try {
                val findDiaryUiEntity =
                    Singleton.loadedDiaryUiEntity.firstOrNull { it.weekStart == weekStart }
                val a =
                    if (findDiaryUiEntity == null) {
                        val getWeek = journalRepository.getWeek(
                            settings.currentUserId.first(),
                            WeekStartEndEntity(weekStart!!, weekEnd!!),
                            Singleton.currentYearId.value ?: 0
                        )
                        Singleton.loadedDiaryUiEntity.add(getWeek)
                        getWeek
                    } else {
                        findDiaryUiEntity
                    }
                withContext(Dispatchers.Main) {
                    _week.value = a
                }
            } catch (e: Exception) {
                val errorDescription = e.toDescription()
                withContext(Dispatchers.Main) {
                    _errorMessage.value = errorDescription
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}