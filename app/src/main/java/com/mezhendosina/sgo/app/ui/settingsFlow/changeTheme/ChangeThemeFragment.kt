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

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentSettingsThemeBinding

class ChangeThemeFragment : Fragment(R.layout.fragment_settings_theme) {

    private var binding: FragmentSettingsThemeBinding? = null

    private val viewModel by viewModels<ChangeThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsThemeBinding.bind(view)

        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding!!.lightTheme.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> binding!!.darkTheme.isChecked = true
            else -> binding!!.sameAsSystem.isChecked = true
        }


        binding!!.changeThemeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.changeTheme(checkedId, requireContext())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}