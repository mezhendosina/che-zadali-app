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

package com.mezhendosina.sgo.app.ui.lessonItem

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemAttachmentBinding
import com.mezhendosina.sgo.app.databinding.ItemLessonBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.AttachmentAdapter
import com.mezhendosina.sgo.app.ui.adapters.AttachmentClickListener
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.app.ui.uploadFileBottomSheet.UploadFileBottomSheet
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.Attachment
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.FileUiEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonFragment : Fragment(R.layout.item_lesson) {

    internal val viewModel: LessonViewModel by viewModels()
    private lateinit var binding: ItemLessonBinding

    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            println(it)
        }

    private val whyGradeAdapter = WhyGradeAdapter()
    private val attachmentAdapter = AttachmentAdapter(
        object : AttachmentClickListener {
            override fun onClick(attachment: Attachment, binding: ItemAttachmentBinding) {
                val permission =
                    requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (permission == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    viewModel.downloadAttachment(requireContext(), attachment, binding)
                } else {
                    storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    viewModel.downloadAttachment(requireContext(), attachment, binding)
                }
            }
        }
    )


    private val answerFileAdapter = AnswerFileAdapter(
        object : OnFileClickListener {
            override fun invoke(file: FileUiEntity) {
                viewModel.downloadFile(file.id, file.fileName)
            }
        }, object : FileActionListener {
            override fun deleteFile(attachmentId: Int) {
                viewModel.deleteFile(attachmentId) {
                    Snackbar.make(binding.root, "Файл удален", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun editDescription(attachmentId: Int) {
                val file =
                    viewModel.answerFiles.value?.get(0)?.files?.first { it.id == attachmentId }

                UploadFileBottomSheet(
                    UploadFileBottomSheet.EDIT_DESCRIPTION,
                    viewModel.lesson.value?.assignments?.first { it.typeId == 3 }?.id ?: 0,
                    file
                ) {
                    Snackbar.make(binding.root, "Описание изменено", Snackbar.LENGTH_LONG)
                        .show()
                }.show(childFragmentManager, "UPLOAD_FRAGMENT")
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ItemLessonBinding.bind(view)
        CoroutineScope(Dispatchers.Main).launch {
            showAnimation(binding.progressBar)
            withContext(Dispatchers.IO) {
                viewModel.loadHomework()
            }
            hideAnimation(binding.progressBar, View.GONE)
        }
        with(binding) {
            toolbar.setNavigationOnClickListener {
                Singleton.transition.value = null
                findTopNavController().popBackStack()
            }

            homework.attachmentsList.attachmentsList.adapter = attachmentAdapter
            homework.attachmentsList.attachmentsList.layoutManager =
                LinearLayoutManager(requireContext())

            itemWhyGrade.whyGradeRecyclerView.adapter = whyGradeAdapter
            itemWhyGrade.whyGradeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            sendHomework.sendAttachmentList.adapter = answerFileAdapter
            sendHomework.sendAttachmentList.layoutManager =
                LinearLayoutManager(requireContext())
        }

        observeOnUploadFileButtonClick()
        observeOnCopyIconClick()
        observeOnSendTextClick()

        observeAttachment()
        observeGrades()
        observeLesson()
        observeHomework()
        observeAnswers()
        observeErrors()

    }

    private fun observeOnSendTextClick() {
        binding.sendHomework.sendText.sendHomeworkTextLayout.setEndIconOnClickListener {
            val answer =
                binding.sendHomework.sendText.sendHomeworkTextLayout.editText?.text.toString()
            viewModel.sendAnswer(requireContext(), answer)
            Toast.makeText(requireContext(), "Ответ отправлен", Toast.LENGTH_LONG).show()
            val inputManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                binding.sendHomework.sendText.sendHomeworkEditText.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun observeOnCopyIconClick() {
        binding.homework.copyIcon.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("homework", binding.homework.homeworkBody.text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                requireContext(),
                "Домашнее задание скоприровано в буфер обмена",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeOnUploadFileButtonClick() {
        binding.sendHomework.selectFile.setOnClickListener {
            UploadFileBottomSheet(
                UploadFileBottomSheet.UPLOAD_FILE,
                viewModel.lesson.value?.assignments?.first { it.typeId == 3 }?.id ?: 0
            ) {
                Toast.makeText(requireContext(), "Файл загружен", Toast.LENGTH_LONG).show()
                viewModel.loadHomework()
            }.show(childFragmentManager, "UPLOAD_FRAGMENT")
        }
    }

    private fun observeLesson() {
        viewModel.lesson.observe(viewLifecycleOwner) {
            val homework = it.assignments?.find { assign -> assign.typeId == 3 }

            binding.toolbar.title = it.subjectName
            with(binding.homework) {
                homeworkBody.text = homework?.assignmentName
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

            if (it.assignments.isNullOrEmpty()) binding.sendHomework.root.visibility =
                View.GONE
        }
    }

    private fun observeAttachment() {
        viewModel.attachments.observe(viewLifecycleOwner) {
            attachmentAdapter.attachments = it
            with(binding.homework) {
                if (it.isNotEmpty()) {
                    attachmentsList.attachmentsList.visibility = View.VISIBLE
                    attachmentsList.attachmentsHeader.visibility = View.VISIBLE
                } else {
                    attachmentsList.attachmentsList.visibility = View.GONE
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
                }
                answerFileAdapter.files = answerFiles[0].files
            } else {
                answerFileAdapter.files = emptyList()
            }
        }
    }

    private fun observeHomework() {
        viewModel.homework.observe(viewLifecycleOwner) { assignResponse ->
            if (assignResponse.description?.isNotEmpty() == true) {
                showComment(binding)
                binding.homework.commentBody.text = assignResponse.description
            }
            if (Singleton.lesson == null) {
                binding.homework.homeworkBody.text = assignResponse.assignmentName
                binding.sendHomework.root.visibility = View.VISIBLE
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

    private fun showComment(binding: ItemLessonBinding) {
        with(binding.homework) {
            commentBody.visibility = View.VISIBLE
            commentHeader.visibility = View.VISIBLE
        }

    }

    private fun showWhyGrades(binding: ItemLessonBinding) {
        showAnimation(binding.itemWhyGrade.whyGradeRecyclerView)
        showAnimation(binding.itemWhyGrade.whyGradeDivider)
        showAnimation(binding.itemWhyGrade.whyGradeHeader)
    }
}