package com.mezhendosina.sgo.app.ui.journal

import android.content.Context
import androidx.lifecycle.*
import androidx.lifecycle.Observer
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
import java.util.*

class JournalViewModel : ViewModel() {

    lateinit var diaryEntity: Flow<PagingData<DiaryAdapterEntity>>


    fun loadDiary(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            val studentId = settings.currentUserId.first()
            diaryEntity = getPagedDiary(studentId)
        }
    }

    private fun getPagedDiary(studentId: Int): Flow<PagingData<DiaryAdapterEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = true,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                JournalPagingSource(
                    studentId,
                    Singleton.diarySource,
                    Singleton.homeworkSource,
                )
            }
        ).flow.cachedIn(viewModelScope)


}

