package com.mezhendosina.sgo.app.ui.journal.paging

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.layouts.diary.Diary
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

private const val PAGE_SIZE = 1
const val PLACEHOLDERS = true

class JournalPagingSource(
    private val studentId: Int
) : PagingSource<Long, Diary>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Diary> {
        val page = params.key ?: currentTime()
        return try {
            val response = Singleton.requests.diary(
                Singleton.at,
                studentId,
                weekEndByTime(page),
                weekStartByTime(page),
                Singleton.currentYearId
            )
            val nextKey = page + 7 * 24 * 60 * 60 * 1000
            val prevKey = if (page == 0.toLong()) null else page - 7 * 24 * 60 * 60 * 1000
            LoadResult.Page(listOf(response), prevKey, nextKey)
        } catch (e: ResponseException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, Diary>): Long? = null
//        val anchorPos = state.anchorPosition ?: return null
//        val page = state.closestPageToPosition(anchorPos) ?: return null
//
//        return page.prevKey?.plus(7 * 24 * 60 * 60 * 1000)
//            ?: page.nextKey?.minus(7 * 24 * 60 * 60 * 1000)
//    }
}

@SuppressLint("SimpleDateFormat")
fun currentTime(): Long {
    val s = SimpleDateFormat("w.yyyy").format(Date().time)
    return SimpleDateFormat("w.yyyy").parse(s)!!.time

}

@SuppressLint("SimpleDateFormat")
fun weekStartByTime(time: Long): String {
    val s = SimpleDateFormat("w.yyyy").format(time)
    val a = SimpleDateFormat("w.yyyy").parse(s)
    return SimpleDateFormat("yyyy-MM-dd").format(a!!)
}

@SuppressLint("SimpleDateFormat")
private fun weekEndByTime(time: Long): String {
    val s = SimpleDateFormat("w.yyyy").format(time)
    val a = SimpleDateFormat("w.yyyy").parse(s)!!.time + 6 * 24 * 60 * 60 * 1000
    return SimpleDateFormat("yyyy-MM-dd").format(a)
}