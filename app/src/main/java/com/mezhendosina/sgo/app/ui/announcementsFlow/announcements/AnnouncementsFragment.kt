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

package com.mezhendosina.sgo.app.ui.announcementsFlow.announcements

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentAnnouncementsBinding
import com.mezhendosina.sgo.app.ui.announcementsFlow.announcementsBottomSheet.BottomSheetAnnouncementsViewModel
import com.mezhendosina.sgo.app.utils.findTopNavController

class AnnouncementsFragment : Fragment(R.layout.fragment_announcements) {

    private var binding: FragmentAnnouncementsBinding? = null
    private val viewModel by viewModels<BottomSheetAnnouncementsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadAnnouncements()
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnnouncementsBinding.bind(view)

        binding!!.announcementsCompose.setContent {
            Mdc3Theme {
                AnnouncementsScreen(announcementsViewModel = viewModel, findTopNavController())
            }
        }
    }
}