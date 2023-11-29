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

package com.mezhendosina.sgo.app.ui.announcementsFlow.announcementsItem

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentAnnouncementItemBinding
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.ANNOUNCEMENT
import com.mezhendosina.sgo.app.utils.AttachmentAdapter
import com.mezhendosina.sgo.app.utils.AttachmentClickListener
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.netschool.api.announcements.AnnouncementsResponseEntity
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup


@AndroidEntryPoint
class AnnouncementsItemFragment : Fragment(R.layout.fragment_announcement_item) {

    private lateinit var binding: FragmentAnnouncementItemBinding

    internal val viewModel: AnnouncementsFragmentViewModel by viewModels()


    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnnouncementItemBinding.bind(view)

        val announcement = Singleton.selectedAnnouncement
        if (announcement != null) {
            val markwon = Markwon.builder(requireContext())
                .usePlugin(HtmlPlugin.create())
                .build()
            with(binding) {
                val jsoup = markwon.toMarkdown(announcement.description).toString()
                homeworkBody.text = Jsoup.parse(jsoup).wholeText()
                homeworkBody.visibility = View.VISIBLE

                author.text = announcement.author.nickName
                val publicationDate =
                    announcement.postDate.let { DateManipulation(it).dateToRussianWithTime() }
                date.text = requireContext().getString(R.string.publication_date, publicationDate)
            }
            observeErrors()
            setupAttachments(announcement)
        }
    }

    private fun setupAttachments(announcement: AnnouncementsResponseEntity) {
        if (announcement.attachments.isNotEmpty()) {
            val attachmentAdapter = AttachmentAdapter(
                object : AttachmentClickListener {
                    override fun invoke(attachment: FileUiEntity, loadingList: MutableList<Int>) {
                        val permission =
                            requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            CoroutineScope(Dispatchers.Main).launch {
                                TODO()
//                                viewModel.downloadAttachment(attachment)
                            }
                        } else {
                            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    }
                }
            )
            attachmentAdapter.attachments =
                announcement.attachments.map { it.toUiEntity(ANNOUNCEMENT, announcement.id) }
            binding.attachmentsList.attachmentsListRecyclerView.adapter = attachmentAdapter
            binding.attachmentsList.attachmentsListRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            showAttachments(binding)
        }
    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }
    }


    private fun showAttachments(binding: FragmentAnnouncementItemBinding) {
        binding.attachmentsList.root.isVisible = true
        binding.attachmentsDivider.isVisible = true
    }
}
