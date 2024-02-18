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

package com.mezhendosina.sgo.app.ui.journalFlow.containerLesson

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerLessonBinding
import com.mezhendosina.sgo.app.ui.journalFlow.answer.AnswerFragment.Companion.ADD_ANSWER
import com.mezhendosina.sgo.app.ui.journalFlow.answer.AnswerFragment.Companion.EDIT_ANSWER
import com.mezhendosina.sgo.app.utils.getEmojiLesson
import com.mezhendosina.sgo.app.utils.setLessonEmoji
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LessonContainer : Fragment(R.layout.container_lesson) {
    private var binding: ContainerLessonBinding? = null

    private val viewModel by viewModels<LessonContainerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = ContainerLessonBinding.bind(view)
        val innerNavController =
            childFragmentManager.findFragmentById(binding!!.lessonFragmentContainer.id)
                ?.findNavController()
        with(binding!!.lessonToolbar) {
            itemToolbar.title = viewModel.lesson?.subjectName ?: ""
            val lessonNameUiEntity = getEmojiLesson(viewModel.lesson?.subjectName ?: "")
            setLessonEmoji(requireContext(), lessonNameUiEntity?.nameId)

            itemToolbar.setNavigationOnClickListener {
                if (innerNavController?.currentDestination?.id == innerNavController?.graph?.startDestinationId) {
                    findNavController().navigateUp()
                } else {
                    innerNavController?.navigateUp()
                }
            }
        }

        innerNavController?.addOnDestinationChangedListener { _, destination, args ->
            when (destination.id) {
                R.id.answerFragment -> {
                    binding!!.lessonToolbar.appbarlayout.setExpanded(false)
                    with(binding!!.send) {
                        when (args?.getString("action")) {
                            ADD_ANSWER -> {
                                setText(R.string.add_answer)
                                setIconResource(R.drawable.ic_send)
                            }

                            EDIT_ANSWER -> {
                                setText(R.string.edit_answer)
                                setIconResource(R.drawable.ic_edit)
                            }
                        }
                    }
                    binding!!.send.show()
                }

                R.id.lessonFragment2 -> {
                    binding!!.send.hide()
                    binding!!.lessonToolbar.appbarlayout.setExpanded(true)
                }
            }
        }
        observerSendButton(innerNavController)
    }

    private fun observerSendButton(innerNavController: NavController?) {
        binding!!.send.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.sendAnswers(requireContext())
            }
            innerNavController?.navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TransitionManager.endTransitions(binding!!.lessonToolbar.root)
        binding = null
    }

    companion object {
        const val LESSON_NAME = "lesson_name"
    }
}
