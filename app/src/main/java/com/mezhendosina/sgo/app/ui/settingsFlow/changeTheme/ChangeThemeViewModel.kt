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

package com.mezhendosina.sgo.app.ui.settingsFlow.changeTheme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.editPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeThemeViewModel : ViewModel() {

    fun changeTheme(selectedThemeId: Int, context: Context) {
        val themeId = when (selectedThemeId) {
            R.id.light_theme -> AppCompatDelegate.MODE_NIGHT_NO
            R.id.dark_theme -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        CoroutineScope(Dispatchers.IO).launch {
            SettingsDataStore.THEME.editPreference(context, themeId)
        }

        AppCompatDelegate.setDefaultNightMode(themeId)
    }

}