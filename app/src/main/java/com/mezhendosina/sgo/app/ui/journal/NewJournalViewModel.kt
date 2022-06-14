package com.mezhendosina.sgo.app.ui.journal

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.journal.paging.JournalPagingSource
import com.mezhendosina.sgo.app.ui.journal.paging.getPagedDiary
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.diary.Diary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NewJournalViewModel : ViewModel() {

    lateinit var diary: Flow<PagingData<Diary>>

    fun loadDiary(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val studentId = Settings(context).currentUserId.first()
            diary = getPagedDiary(studentId)
        }
    }
}