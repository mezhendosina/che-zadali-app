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

package com.mezhendosina.sgo.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.google.firebase.FirebaseApp
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.model.journal.DiaryStyle
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        runBlocking {
            AppCompatDelegate.setDefaultNightMode(
                SettingsDataStore.THEME.getValue(
                    this@SplashActivity,
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ).first()
            )
            NetSchoolSingleton.baseUrl =
                SettingsDataStore.REGION_URL.getValue(this@SplashActivity, "").first()
        }
        if (!BuildConfig.DEBUG) DynamicColors.applyToActivitiesIfAvailable(this.application)
//        DynamicColors.applyToActivitiesIfAvailable(this.application)
        super.onCreate(savedInstanceState)


        CoroutineScope(Dispatchers.Main).launch {
            FirebaseApp.initializeApp(this@SplashActivity)

            val intent =
                if (SettingsDataStore.LOGGED_IN.getValue(this@SplashActivity, false).first()) {
                    Singleton.diaryStyle.value = SettingsDataStore.DIARY_STYLE.getValue(
                        this@SplashActivity,
                        DiaryStyle.AS_CARD
                    ).first()
                    Intent(this@SplashActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}
