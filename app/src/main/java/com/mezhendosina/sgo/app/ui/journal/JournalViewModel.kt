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
import kotlin.properties.Delegates

class JournalViewModel : ViewModel() {

    lateinit var diary: Flow<PagingData<Diary>>

    private lateinit var journalPagingSource: JournalPagingSource

    private var studentId by Delegates.notNull<Int>()

    fun loadDiary(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            studentId = Settings(context).currentUserId.first()
            journalPagingSource = JournalPagingSource(studentId)
            diary = getPagedDiary(journalPagingSource)
        }
    }

    fun reload() {
        journalPagingSource.invalidate()
    }


    private fun getPagedDiary(journalPagingSource: JournalPagingSource): Flow<PagingData<Diary>> =
        Pager(
            config = PagingConfig(
                pageSize = 4,
                enablePlaceholders = PLACEHOLDERS,
                initialLoadSize = 4
            ),
            pagingSourceFactory = { journalPagingSource }
        ).flow.cachedIn(viewModelScope)
}

