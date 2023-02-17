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

package com.mezhendosina.sgo.app.ui.chooseUserId

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.model.user.UserUIEntity
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.sgo.login.entities.StudentResponseEntity
import kotlinx.coroutines.launch

class ChooseUserIdViewModel : ViewModel() {

    private val _usersId = MutableLiveData<List<UserUIEntity>>()
    val usersId: LiveData<List<UserUIEntity>> = _usersId

    init {
        _usersId.value = Singleton.users.toUiEntity()
    }


    fun login(userId: Int) {
        val context = Singleton.getContext()
        try {
            val settings = Settings(context)
            viewModelScope.launch {
                settings.editPreference(Settings.CURRENT_USER_ID, userId)
                settings.editPreference(Settings.LOGGED_IN, true)
            }
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(context, intent, null)
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

}

fun List<StudentResponseEntity>.toUiEntity(): List<UserUIEntity> {
    return this.map { UserUIEntity(it.id, 0, it.name, "") }
}
