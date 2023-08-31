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

package com.mezhendosina.sgo.app.ui.loginFlow.gosuslugi

import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.login.entities.accountInfo.toUiEntity
import com.mezhendosina.sgo.data.netschool.repo.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GosuslugiViewModel(
    private val loginRepository: LoginRepository = NetSchoolSingleton.loginRepository
) : ViewModel() {
    suspend fun login(
        loginState: String,
        onOneUser: (id: String) -> Unit,
        onMoreUser: () -> Unit
    ) {
        val users = loginRepository.getGosuslugiUsers(loginState)
        withContext(Dispatchers.Main) {
            Singleton.users = users.toUiEntity()
            if (users.size > 1) {
                onMoreUser.invoke()
            } else {
                onOneUser.invoke(users.first().id)
            }
        }
    }
}