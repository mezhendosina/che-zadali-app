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

package com.mezhendosina.sgo.app.ui.announcementsFlow.announcementsContainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerAnnouncementsBinding
import com.mezhendosina.sgo.app.utils.findTopNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnouncementsContainerFragment : Fragment(R.layout.container_announcements) {

    private var binding: ContainerAnnouncementsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ContainerAnnouncementsBinding.bind(view)
        val innerNavController =
            childFragmentManager.findFragmentById(binding!!.lessonFragmentContainer.id)
                ?.findNavController()

        binding!!.announcementToolbar.setNavigationOnClickListener {
            if (innerNavController?.currentDestination?.id == innerNavController?.graph?.startDestinationId) {
                findTopNavController().navigateUp()
            } else {
                innerNavController?.navigateUp()
            }
        }

        innerNavController?.addOnDestinationChangedListener { controller, destination, arguments ->
            binding!!.collapsingtoolbarlayout.title = when (destination.id) {
                controller.graph.startDestinationId -> requireContext().getString(R.string.announcements)
                else -> Singleton.selectedAnnouncement?.name ?: ""
            }
        }
    }
}