package com.mezhendosina.sgo.app.ui.journal

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.journal.entities.DiaryAdapterEntity
import com.mezhendosina.sgo.app.model.journal.JournalPagingSource
import com.mezhendosina.sgo.app.model.journal.PLACEHOLDERS
import com.mezhendosina.sgo.app.model.settings.SettingsRepository
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class JournalViewModel(
    private val settingsRepository: SettingsRepository = Singleton.settingsRepository
) : ViewModel() {

    lateinit var diaryEntity: Flow<PagingData<DiaryAdapterEntity>>


    fun loadDiary(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            val studentId = settings.currentUserId.first()
            val yearId = if (Singleton.currentYearId != null) Singleton.currentYearId
            else {
                val years = settingsRepository.getYears().first { !it.name.contains("(*)") }.id
                Singleton.currentYearId = years
                years
            }
            diaryEntity = getPagedDiary(studentId, yearId!!)
        }
    }

    private fun getPagedDiary(studentId: Int, yearId: Int): Flow<PagingData<DiaryAdapterEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = PLACEHOLDERS,
                initialLoadSize = 2
            ),
            pagingSourceFactory = {
                JournalPagingSource(
                    studentId,
                    yearId,
                    Singleton.diarySource,
                    Singleton.homeworkSource,
                )
            }
        ).flow.cachedIn(viewModelScope)
}

