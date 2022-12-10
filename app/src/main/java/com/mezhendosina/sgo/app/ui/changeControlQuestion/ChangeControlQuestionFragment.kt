package com.mezhendosina.sgo.app.ui.changeControlQuestion

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChangeControlQuestionBinding
import com.mezhendosina.sgo.app.ui.settings.SettingsViewModel
import com.mezhendosina.sgo.data.toMD5

class ChangeControlQuestionFragment : Fragment(R.layout.fragment_change_control_question) {

    private lateinit var binding: FragmentChangeControlQuestionBinding

    private val viewModel by viewModels<ChangeControlQuestionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChangeControlQuestionBinding.bind(view)

        val question = arguments?.getString(SELECTED_QUEStION)
        if (question == "Не выбрано") {
            binding.answerTextLayout.visibility = View.GONE
        } else if (viewModel.questions.value?.contains(question) == false) {
            binding.selectedQuestionText.text = "Собственный вопрос"
            binding.customQuestion.visibility = View.VISIBLE
        } else {
            binding.selectedQuestionText.text = question
        }

        val answer = arguments?.getString(ANSWER)
        if (answer != null) {
            binding.answerTextLayout.apply {
                editText?.setText(answer)
                endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.selectedQuestionCard.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Выберите вопрос")
                .setItems(viewModel.questions.value) { _, which ->
                    binding.selectedQuestionText.text = viewModel.questions.value?.get(which)

                    binding.customQuestion.visibility =
                        if (viewModel.questions.value?.get(which) == "Собственный вопрос") {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }

                    binding.answerTextLayout.visibility =
                        if (viewModel.questions.value?.get(which) == "Не выбрано") {
                            View.GONE
                        } else {
                            View.VISIBLE
                        }

                }.show()
        }

        binding.answerTextLayout.editText?.addTextChangedListener {
            if (it?.length == 0) {
                binding.answerTextLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.action_changeControlQuestionFragment_to_settingsFragment,
                bundleOf(
                    SettingsViewModel.CONTROL_QUESTION to binding.selectedQuestionText.text,
                    SettingsViewModel.CONTROL_ANSWER to binding.answerTextLayout.editText?.text.toString()
                        .toMD5()
                )
            )
        }
    }

    companion object {
        const val SELECTED_QUEStION = "selected_question"
        const val ANSWER = "answer"
    }
}