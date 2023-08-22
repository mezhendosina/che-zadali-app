/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.data.github

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.registerReceiver
import com.mezhendosina.sgo.data.netschool.base.BaseRetrofitSource
import com.mezhendosina.sgo.data.netschool.base.RetrofitConfig
import com.mezhendosina.sgo.data.requests.github.checkUpdates.CheckUpdates


class GithubUpdateDownloader(retrofitConfig: RetrofitConfig) : BaseRetrofitSource(retrofitConfig) {
    private val githubApi =
        GithubRetrofitSource.baseRetrofitSource.retrofit.create(GithubApi::class.java)

    suspend fun getLastVersion(): CheckUpdates = wrapRetrofitExceptions {
        githubApi.getLatestUpdate()
    }

    fun downloadUpdate(
        context: Context,
        url: String,
        onProgressChanged: (progress: Int, id: Int) -> Unit
    ) {
        val urlToUri = Uri.parse(url)
        val r = DownloadManager.Request(urlToUri)
        val manager = getSystemService(context, DownloadManager::class.java)
        val enqueue = manager?.enqueue(r)
        if (enqueue != null) {
            val receiver = object : BroadcastReceiver() {
                @SuppressLint("Range")
                override fun onReceive(p0: Context?, p1: Intent?) {
                    val query = DownloadManager.Query().setFilterById(enqueue)
                    val cursor = manager.query(query)
                    if (cursor.moveToFirst()) {
                        val bytesDownloaded =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val bytesTotal =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        val progress = (bytesDownloaded * 100L / bytesTotal).toInt()
                        onProgressChanged.invoke(progress, -1)
                    }
                }
            }

            registerReceiver(
                context,
                receiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }
}