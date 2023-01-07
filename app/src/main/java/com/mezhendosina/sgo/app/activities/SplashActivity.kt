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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerLoginBinding
import com.mezhendosina.sgo.app.ui.chooseRegion.ChooseRegionFragment
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val settings = Settings(this)
        runBlocking {
            AppCompatDelegate.setDefaultNightMode(settings.theme.first())
        }
        super.onCreate(savedInstanceState)

        Singleton.loadContext(applicationContext)

        CoroutineScope(Dispatchers.Main).launch {
            FirebaseApp.initializeApp(this@SplashActivity)
            if (settings.regionUrl.first().isNullOrEmpty() && settings.loggedIn.first()) {
                val binding =
                    ContainerLoginBinding.inflate(LayoutInflater.from(this@SplashActivity))
                setContentView(binding.root)
                binding.collapsingtoolbarlayout.title = "Выберите регион"
                supportFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.fragmentContainer,
                        ChooseRegionFragment.newInstance(ChooseRegionFragment.FROM_MAIN_ACTIVITY)
                    ).commit()
            } else {
                val intent = if (settings.loggedIn.first()) {
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
}