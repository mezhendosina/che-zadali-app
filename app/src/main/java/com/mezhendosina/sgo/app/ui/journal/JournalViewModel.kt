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
import com.mezhendosina.sgo.data.layouts.diary.init.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class JournalViewModel : ViewModel() {

    lateinit var diary: Flow<PagingData<Diary>>


    fun loadDiary(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val studentId = Settings(context).currentUserId.first()
            diary = getPagedDiary(studentId)
        }
    }

    private fun getPagedDiary(studentId: Int): Flow<PagingData<Diary>> =
        Pager(
            config = PagingConfig(
                pageSize = 4,
                enablePlaceholders = PLACEHOLDERS,
                initialLoadSize = 4
            ),
            pagingSourceFactory = { JournalPagingSource(studentId) }
        ).flow.cachedIn(viewModelScope)
}

