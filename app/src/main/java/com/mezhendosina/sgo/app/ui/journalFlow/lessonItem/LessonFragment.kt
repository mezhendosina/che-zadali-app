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

package com.mezhendosina.sgo.app.ui.journalFlow.lessonItem

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentItemLessonBinding
import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.AttachmentDownloadManager
import com.mezhendosina.sgo.app.model.attachments.AttachmentsUtils
import com.mezhendosina.sgo.app.ui.journalFlow.answer.AnswerFragment
import com.mezhendosina.sgo.app.utils.AttachmentAdapter
import com.mezhendosina.sgo.app.utils.AttachmentClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LessonFragment : Fragment(R.layout.fragment_item_lesson) {

    internal val viewModel: LessonViewModel by viewModels()
    private var binding: FragmentItemLessonBinding? = null

    @Inject
    lateinit var attachmentDownloadManager: AttachmentDownloadManager
    val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.changePermissionStatus(it.all { b -> b.value })
            }
        }


    private val onAttachmentClickListener = object : AttachmentClickListener {
        override fun invoke(attachment: FileUiEntity, loadingList: MutableList<Int>) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.downloadAttachment(requireContext(), attachment)
            }
            if (!AttachmentsUtils.checkPermissions(requireContext())) {
                if (Build.VERSION.SDK_INT in 30..32) {
                    storagePermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                } else if (Build.VERSION.SDK_INT <= 29) {
                    storagePermission.launch(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
            }
        }
    }

    private var whyGradeAdapter: WhyGradeAdapter? = null
    private var attachmentAdapter: AttachmentAdapter? = AttachmentAdapter(onAttachmentClickListener)
    private var answerFileAdapter: AttachmentAdapter? = AttachmentAdapter(onAttachmentClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)


        whyGradeAdapter = WhyGradeAdapter()
        attachmentAdapter = AttachmentAdapter(onAttachmentClickListener)
        answerFileAdapter = AttachmentAdapter(onAttachmentClickListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemLessonBinding.bind(view)

        binding!!.homework.attachmentsList.attachmentsListRecyclerView.apply {
            adapter = attachmentAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding!!.sendHomework.sendAttachmentList.apply {
            adapter = answerFileAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding!!.itemWhyGrade.whyGradeRecyclerView.apply {
            adapter = whyGradeAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        bindLesson()

        observeOnAddAnswerClick()
        observeOnEditAnswerClick()
        observeOnCopyHomeworkClick()
        observeOnAnswerUpdated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding!!.homework.attachmentsList.attachmentsListRecyclerView.invalidate()
        binding!!.sendHomework.sendAttachmentList.invalidate()
        binding!!.itemWhyGrade.whyGradeRecyclerView.invalidate()

        binding = null
        whyGradeAdapter = null
        attachmentAdapter = null
        answerFileAdapter = null
    }

    private fun bindLesson() {
        viewModel.lesson.observe(viewLifecycleOwner) { lesson ->
            if (lesson != null) with(binding!!) {
                homework.homeworkBody.text = lesson.homework

                if (lesson.homework.isEmpty()) {
                    homework.root.visibility = View.GONE
                } else {
                    if (!lesson.homeworkComment.isNullOrEmpty()) {
                        homework.commentBody.text = lesson.homeworkComment
                        homework.commentBody.visibility = View.VISIBLE
                        homework.commentHeader.visibility = View.VISIBLE
                    } else {
                        homework.commentBody.visibility = View.GONE
                        homework.commentHeader.visibility = View.GONE
                    }

                    if (!lesson.attachments.isNullOrEmpty()) {
                        homework.attachmentsList.root.visibility = View.VISIBLE
                        attachmentAdapter?.attachments = lesson.attachments
                    } else {
                        homework.attachmentsList.root.visibility = View.GONE
                    }
                    if (lesson.answerText.isNullOrBlank() && lesson.answerFiles.isNullOrEmpty()) {
                        sendHomework.root.visibility = View.GONE
                        homework.addAnswerButton.visibility = View.VISIBLE
                    } else {
                        sendHomework.root.visibility = View.VISIBLE
                        homework.addAnswerButton.visibility = View.GONE

                        if (lesson.answerFiles.isNullOrEmpty()) {
                            sendHomework.sendAttachmentList.visibility = View.GONE
                        } else {
                            sendHomework.sendAttachmentList.visibility = View.VISIBLE
                            answerFileAdapter?.attachments = lesson.answerFiles
                        }
                        if (lesson.answerText.isNullOrEmpty()) {
                            sendHomework.answerText.visibility = View.GONE
                        } else {
                            sendHomework.answerText.visibility = View.VISIBLE
                            sendHomework.answerText.text = lesson.answerText
                        }
                    }
                }



                if (!lesson.whyGradeEntity.isNullOrEmpty()) {
                    itemWhyGrade.root.visibility = View.VISIBLE
                    whyGradeAdapter?.grades = lesson.whyGradeEntity
                } else {
                    itemWhyGrade.root.visibility = View.GONE
                }
            }
        }
    }

    private fun observeOnAddAnswerClick() {
        binding!!.homework.addAnswerButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_lessonFragment2_to_answerFragment,
                bundleOf("action" to AnswerFragment.ADD_ANSWER)
            )
        }
    }

    private fun observeOnEditAnswerClick() {
        binding!!.sendHomework.editAnswer.setOnClickListener {
            findNavController().navigate(
                R.id.action_lessonFragment2_to_answerFragment,
                bundleOf("action" to AnswerFragment.EDIT_ANSWER)
            )
        }
    }

    private fun observeOnCopyHomeworkClick() {
        binding!!.homework.copyIcon.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("homework", binding!!.homework.homeworkBody.text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                requireContext(),
                "Домашнее задание скоприровано в буфер обмена",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeOnAnswerUpdated() {
        Singleton.answerUpdated.observe(viewLifecycleOwner) {
            if (it) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.loadLesson()
                }
                Singleton.answerUpdated.value = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}