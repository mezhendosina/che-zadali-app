package com.mezhendosina.sgo.app.ui.announcements

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentAnnouncementItemBinding
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.ui.adapters.AttachmentAdapter
import com.mezhendosina.sgo.app.ui.adapters.AttachmentClickListener
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.requests.homework.entities.Attachment
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import org.jsoup.Jsoup


class AnnouncementsFragment : Fragment(R.layout.fragment_announcement_item) {

    private lateinit var binding: FragmentAnnouncementItemBinding

    internal val viewModel: AnnouncementsFragmentViewModel by viewModels()

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
            if (announcement?.description != null) {
                val jsoup = markwon.toMarkdown(announcement.description).toString()
                homeworkBody.text = Jsoup.parse(jsoup).wholeText()
            } else {
                homeworkBody.visibility = View.GONE
            }
            if (!announcement?.attachments.isNullOrEmpty()) {
                val attachmentAdapter = AttachmentAdapter(
                    object : AttachmentClickListener {
                        override fun onClick(
                            attachment: Attachment,
                            binding: ItemAttachmentBinding
                        ) {
                            viewModel.downloadAttachment(
                                requireContext(),
                                attachment,
                                binding
                            )
                        }
                    }
                )
                attachmentAdapter.attachments = announcement?.attachments ?: emptyList()
                attachmentsList.attachmentsList.adapter = attachmentAdapter
                attachmentsList.attachmentsList.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                showAttachments(this)
            }
            author.text = announcement?.author?.nickName
            date.text =
                "Дата публикации: ${announcement?.postDate?.let { DateManipulation(it).dateToRussianWithTime() }}"
        }
        observeErrors()
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
