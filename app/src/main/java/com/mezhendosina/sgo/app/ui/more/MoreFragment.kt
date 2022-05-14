package com.mezhendosina.sgo.app.ui.more

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.databinding.LessonItemBinding
import com.mezhendosina.sgo.app.ui.adapters.AttachmentAdapter
import com.mezhendosina.sgo.app.ui.adapters.AttachmentClickListener
import com.mezhendosina.sgo.data.attachments.Attachment
import java.text.SimpleDateFormat

class MoreFragment : Fragment() {

    private val viewModel: MoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        viewModel.findLesson(
            arguments?.getInt("lessonId") ?: 0,
            arguments?.getString("type") ?: ""
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LessonItemBinding.inflate(inflater, container, false)

        val attachmentAdapter = AttachmentAdapter(
            viewModel,
            viewLifecycleOwner,
            object : AttachmentClickListener {
                override fun onClick(attachment: Attachment) {
                    viewModel.downloadAttachment(requireContext(), attachment)
                }
            })

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.attachments.observe(viewLifecycleOwner) {
            attachmentAdapter.attachments = it
            if (it.isNotEmpty()) {
                binding.attachmentsList.visibility = View.VISIBLE
                binding.attachmentsDivider.visibility = View.VISIBLE
                binding.attachmentsHeader.visibility = View.VISIBLE

            } else {
                binding.attachmentsList.visibility = View.GONE
                binding.attachmentsDivider.visibility = View.GONE
                binding.attachmentsHeader.visibility = View.GONE
            }
        }

        viewModel.lesson.observe(viewLifecycleOwner) {
            val homework = it.assignments?.find { assign -> assign.typeId == 3 }

            binding.toolbar.title = it.subjectName

            binding.homeworkBody.text = homework?.assignmentName
            binding.dueDate.text = "Срок сдачи: ${homework?.dueDate?.let { it1 -> parseDate(it1) }}"


        }
        binding.attachmentsList.adapter = attachmentAdapter
        binding.attachmentsList.layoutManager = LinearLayoutManager(inflater.context)

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate(date: String): String {
        val parse = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
        return SimpleDateFormat("dd/MM/yyyy").format(parse!!)
    }
}