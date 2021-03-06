package com.mezhendosina.sgo.app.ui.journal.lessonItem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.databinding.ItemLessonBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.ui.adapters.AttachmentAdapter
import com.mezhendosina.sgo.app.ui.adapters.AttachmentClickListener
import com.mezhendosina.sgo.app.ui.adapters.WhyGradeAdapter
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.layouts.attachments.Attachment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class LessonFragment : Fragment() {

    private val viewModel: LessonViewModel by viewModels { factory() }
    private lateinit var binding: ItemLessonBinding
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
        binding = ItemLessonBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.Main).launch {
            showAnimation(binding.progressBar)
            withContext(Dispatchers.IO) {
                viewModel.loadHomework(requireContext())
                viewModel.loadGrades(requireContext())
            }
            hideAnimation(binding.progressBar, View.GONE)
        }

        val downloadState = MutableLiveData(1)
        val whyGradeAdapter = WhyGradeAdapter()
        val attachmentAdapter = AttachmentAdapter(
            object : AttachmentClickListener {
                override fun onClick(attachment: Attachment, binding: ItemAttachmentBinding) {
                    viewModel.downloadAttachment(requireContext(), attachment, downloadState)
                    downloadState.observe(viewLifecycleOwner) {
                        binding.progressBar.progress = it
                        if (it == 100) {
                            hideAnimation(binding.progressBar, View.INVISIBLE)
                        }
                    }
                }
            }
        )

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
            binding.sendHomeworkTextLayout.editText?.setText(homework?.textAnswer?.answer)
            binding.homeworkBody.text = homework?.assignmentName
            binding.dueDate.text = "???????? ??????????: ${homework?.dueDate?.let { it1 -> parseDate(it1) }}"
        }

        viewModel.homework.observe(viewLifecycleOwner) { assignResponse ->
            var teachers = ""
            assignResponse.teachers.forEach {
                teachers += it.name
                if (assignResponse.teachers.size > 1) {
                    teachers += ", "
                }
            }
            binding.teacherName.text = "??????????????: $teachers"
            if (!assignResponse.description.isNullOrEmpty()) {
                showComment(binding)
                binding.commentBody.text = assignResponse.description
            }
        }
        viewModel.grades.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                whyGradeAdapter.grades = it
                whyGradeAdapter.types = viewModel.types.value ?: emptyList()
                showWhyGrades(binding)

            }
        }

        binding.sendHomeworkTextLayout.setEndIconOnClickListener {
            viewModel.sendAnswer(
                requireContext(),
                binding.sendHomeworkEditText.text.toString(),
                binding
            )
        }

        binding.attachmentsList.adapter = attachmentAdapter
        binding.attachmentsList.layoutManager = LinearLayoutManager(inflater.context)

        binding.whyGradeRecyclerView.adapter = whyGradeAdapter
        binding.whyGradeRecyclerView.layoutManager = LinearLayoutManager(inflater.context)

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate(date: String): String {
        val parse = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
        return SimpleDateFormat("dd/MM/yyyy").format(parse!!)
    }

    private fun showComment(binding: ItemLessonBinding) {
        binding.commentBody.visibility = View.VISIBLE
        binding.commentDivider.visibility = View.VISIBLE
        binding.commentHeader.visibility = View.VISIBLE
    }

    private fun showWhyGrades(binding: ItemLessonBinding) {
        showAnimation(binding.whyGradeRecyclerView)
        showAnimation(binding.whyGradeDivider)
        showAnimation(binding.whyGradeHeader)
    }
}