package com.mezhendosina.sgo.app.ui.lessonItem

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.databinding.ItemLessonBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.*
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.app.ui.uploadFileBottomSheet.UploadFileBottomSheet
import com.mezhendosina.sgo.data.requests.homework.entities.Attachment
import com.mezhendosina.sgo.data.requests.homework.entities.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class LessonFragment : Fragment(R.layout.item_lesson) {

    internal val viewModel: LessonViewModel by viewModels()
    private lateinit var binding: ItemLessonBinding

    private val whyGradeAdapter = WhyGradeAdapter()
    private val attachmentAdapter = AttachmentAdapter(
        object : AttachmentClickListener {
            override fun onClick(attachment: Attachment, binding: ItemAttachmentBinding) {
                val checkPermission =
                    requireContext().checkSelfPermission(Manifest.permission_group.STORAGE)
                if (checkPermission == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission_group.STORAGE),
                        1100
                    )
                }
                viewModel.downloadAttachment(requireContext(), attachment, binding)
            }
        }
    )


    private val answerFileAdapter = AnswerFileAdapter(
        object : OnFileClickListener {
            override fun invoke(file: File) {
                viewModel.downloadFile(file.id, file.fileName)
            }
        }, object : FileActionListener {
            override fun deleteFile(attachmentId: Int) {

            }

            override fun editDescription(attachmentId: Int) {
                TODO("Not yet implemented")
            }

            override fun replaceFile(attachmentId: Int) {
                TODO("Not yet implemented")
            }

        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        viewModel.loadHomework(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = ItemLessonBinding.bind(view)
        CoroutineScope(Dispatchers.Main).launch {
            showAnimation(binding.progressBar)
            withContext(Dispatchers.IO) {
                viewModel.loadHomework(requireContext())
                viewModel.loadGrades()
            }
            hideAnimation(binding.progressBar, View.GONE)
        }
        with(binding) {
            toolbar.setNavigationOnClickListener {
                Singleton.transition.value = null
                findTopNavController().popBackStack()
            }

            sendHomework.sendText.sendHomeworkTextLayout.setEndIconOnClickListener {
                val answer = sendHomework.sendText.sendHomeworkTextLayout.editText?.text.toString()
                viewModel.sendAnswer(requireContext(), answer)
                Toast.makeText(requireContext(), "Ответ отправлен", Toast.LENGTH_LONG).show()
                val inputManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    sendHomework.sendText.sendHomeworkEditText.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            sendHomework.selectFile.setOnClickListener {
                UploadFileBottomSheet(
                    viewModel.lesson.value?.assignments?.first { it.typeId == 3 }?.id ?: 0
                ).show(childFragmentManager, "UPLOAD_FRAGMENT")
            }

            homework.attachmentsList.attachmentsList.adapter = attachmentAdapter
            homework.attachmentsList.attachmentsList.layoutManager =
                LinearLayoutManager(requireContext())

            whyGradeRecyclerView.adapter = whyGradeAdapter
            whyGradeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            sendHomework.sendAttachmentList.adapter = answerFileAdapter
            sendHomework.sendAttachmentList.layoutManager =
                LinearLayoutManager(requireContext())

            homework.copyIcon.setOnClickListener {
                val clipboard =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("homework", homework.homeworkBody.text)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(
                    requireContext(),
                    "Домашнее задание скоприровано в буфер обмена",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        observeAttachment()
        observeGrades()
        observeLesson()
        observeHomework()
        observeAnswers()
        observeErrors()
    }

    private fun observeLesson() {
        viewModel.lesson.observe(viewLifecycleOwner) {
            val homework = it.assignments?.find { assign -> assign.typeId == 3 }

            binding.toolbar.title = it.subjectName
            with(binding.homework) {
                homeworkBody.text = homework?.assignmentName
                dueDate.text = "Срок сдачи: ${homework?.dueDate?.let { it1 -> parseDate(it1) }}"
                with(binding.sendHomework.sendText) {
                    if (homework?.textAnswer != null) {
                        homeworkText.visibility = View.VISIBLE
                        homeworkText.text = homework.textAnswer.answer

                        sendHomeworkTextLayout.visibility = View.GONE
                    } else {
                        answerText.visibility = View.GONE
                    }
                }
            }

            if (it.assignments.isNullOrEmpty()) binding.sendHomework.root.visibility = View.GONE
        }
    }

    private fun observeAttachment() {
        viewModel.attachments.observe(viewLifecycleOwner) {
            attachmentAdapter.attachments = it
            with(binding.homework) {
                if (it.isNotEmpty()) {
                    attachmentsList.attachmentsList.visibility = View.VISIBLE
                    attachmentsDivider.visibility = View.VISIBLE
                    attachmentsList.attachmentsHeader.visibility = View.VISIBLE
                } else {
                    attachmentsList.attachmentsList.visibility = View.GONE
                    attachmentsDivider.visibility = View.GONE
                    attachmentsList.attachmentsHeader.visibility = View.GONE
                }
            }
        }
    }

    private fun observeAnswers() {
        viewModel.answerFiles.observe(viewLifecycleOwner) { answerFiles ->
            if (!answerFiles.isNullOrEmpty()) {
                with(binding.sendHomework) {
                    if (answerFiles[0].text != null) {
                        with(sendText) {
                            sendHomeworkTextLayout.visibility = View.GONE
                            answerText.visibility = View.VISIBLE
                            homeworkText.text = answerFiles[0].text?.answer

                            answerText.setOnClickListener {
                                answerText.visibility = View.GONE
                                sendHomeworkTextLayout.editText?.setText(answerFiles[0].text?.answer)
                                sendHomeworkTextLayout.visibility = View.VISIBLE
                            }
                        }
                    }
                    sendAttachmentList.visibility = View.VISIBLE
                    answerFileAdapter.files = answerFiles[0].files
                }
            }
        }
    }

    private fun observeHomework() {
        viewModel.homework.observe(viewLifecycleOwner) { assignResponse ->
            if (!assignResponse.description.isNullOrEmpty()) {
                showComment(binding)
                binding.homework.commentBody.text = assignResponse.description
            }
        }
    }

    private fun observeGrades() {
        viewModel.grades.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                whyGradeAdapter.grades = it
                whyGradeAdapter.types = viewModel.types.value ?: emptyList()
                showWhyGrades(binding)
            }
        }

    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (viewModel.snackbar.value == true) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            } else {
                errorDialog(requireContext(), it)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate(date: String): String {
        val parse = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
        return SimpleDateFormat("dd/MM/yyyy").format(parse!!)
    }

    private fun showComment(binding: ItemLessonBinding) {
        with(binding.homework) {
            commentBody.visibility = View.VISIBLE
            commentDivider.visibility = View.VISIBLE
            commentHeader.visibility = View.VISIBLE
        }

    }

    private fun showWhyGrades(binding: ItemLessonBinding) {
        showAnimation(binding.whyGradeRecyclerView)
        showAnimation(binding.whyGradeDivider)
        showAnimation(binding.whyGradeHeader)
    }
}