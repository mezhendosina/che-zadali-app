package com.mezhendosina.sgo.app.ui.journal

import android.content.Context
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.journal.paging.JournalPagingSource
import com.mezhendosina.sgo.app.ui.journal.paging.PLACEHOLDERS
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.diary.Diary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalViewModel : ViewModel() {

    lateinit var diary: Flow<PagingData<Diary>>

    private lateinit var journalPagingSource: JournalPagingSource

    fun loadDiary(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val studentId = Settings(context).currentUserId.first()
            journalPagingSource = JournalPagingSource(studentId)
            diary = getPagedDiary()
        }
    }

    fun reload() {
        journalPagingSource.invalidate()
    }


    private fun getPagedDiary(): Flow<PagingData<Diary>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = PLACEHOLDERS,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { journalPagingSource }
        ).flow.cachedIn(viewModelScope)
}

