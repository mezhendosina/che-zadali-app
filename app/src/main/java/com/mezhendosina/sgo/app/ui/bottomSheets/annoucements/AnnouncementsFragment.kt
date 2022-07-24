package com.mezhendosina.sgo.app.ui.bottomSheets.annoucements

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentAnnouncementItemBinding
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.ui.adapters.AttachmentAdapter
import com.mezhendosina.sgo.app.ui.adapters.AttachmentClickListener
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.layouts.attachments.Attachment
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

//TODO formatting announcement body

class AnnouncementsFragment : Fragment(R.layout.fragment_announcement_item) {

    private lateinit var binding: FragmentAnnouncementItemBinding

    private val viewModel: AnnouncementsFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnnouncementItemBinding.bind(view)


        val announcement =
            Singleton.announcements.find { it.id == arguments?.getInt(Singleton.ANNOUNCEMENTS_ID) }
        val markwon = Markwon.builder(requireContext())
            .usePlugin(HtmlPlugin.create())
            .build()

        with(binding) {
            collapsingtoolbarlayout.title = announcement?.name
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            announcement?.description?.let { markwon.setMarkdown(homeworkBody, it) }
            if (!announcement?.attachments.isNullOrEmpty()) {
                val attachmentAdapter = AttachmentAdapter(
                    object : AttachmentClickListener {
                        override fun onClick(
                            attachment: Attachment,
                            binding: ItemAttachmentBinding
                        ) {
                            val downloadState = MutableLiveData(0)
                            viewModel.downloadAttachment(
                                requireContext(),
                                attachment,
                                downloadState
                            )
                            downloadState.observe(viewLifecycleOwner) {
                                binding.progressBar.progress = it
                                if (it == 100) {
                                    hideAnimation(binding.progressBar, View.INVISIBLE)
                                }
                            }
                        }
                    }
                )
                attachmentAdapter.attachments = announcement?.attachments ?: emptyList()

                attachmentsList.adapter = attachmentAdapter
                attachmentsList.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                showAttachments(this)
            }
            author.text = announcement?.author?.nickName
            date.text =
                "Дата публикации: ${announcement?.postDate?.let { DateManipulation(it).dateToRussianWithTime() }}"
        }
    }

    private fun showAttachments(binding: FragmentAnnouncementItemBinding) {
        binding.attachmentsList.isVisible = true
        binding.attachmentsDivider.isVisible = true
        binding.attachmentsHeader.isVisible = true
    }
}
