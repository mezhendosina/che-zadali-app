package com.mezhendosina.sgo.app.ui.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.getWeeksList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalViewModel : ViewModel() {
    private val _weeks = MutableLiveData<List<WeekStartEndEntity>>()
    val weeks: LiveData<List<WeekStartEndEntity>> = _weeks

    init {
        loadWeeks()
    }

    private fun loadWeeks() {
        CoroutineScope(Dispatchers.IO).launch {
            if (Singleton.weeks.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    _weeks.value = Singleton.weeks
                }
            } else {
                val a = getWeeksList()
                println(a)
                withContext(Dispatchers.Main) {
                    _weeks.value = a
                }

            }

        }
    }
//    val diaryEntity: Flow<PagingData<DiaryUiEntity>> = runBlocking {
//        val settings = Settings(context.applicationContext)
//        val studentId = settings.currentUserId.first()
//
//        return@runBlocking getPagedDiary(studentId)
//    }
//
//
//    private fun getPagedDiary(studentId: Int): Flow<PagingData<DiaryUiEntity>> = Pager(
//        config = PagingConfig(
//            pageSize = 1,
//            enablePlaceholders = true,
//            initialLoadSize = 1,
//        ),
////        remoteMediator = JournalRemoteMediator(
////            studentId,
////            Singleton.database.getJournalDao(),
////            Singleton.diarySource,
////            Singleton.homeworkSource
////        ),
//        pagingSourceFactory = {
//            JournalPagingSource(
//                studentId,
//                Singleton.diarySource,
//                Singleton.homeworkSource
//            )
////            Singleton.database.getJournalDao().getWeek()
//        },
//    ).flow
//        .cachedIn(viewModelScope)
////        .map { pagingData ->
////            pagingData.map { it.diaryResponseEntity }
////        }
}