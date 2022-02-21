package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.che.zadali.sgoapp.databinding.ChangeControlQuestionFragmentBinding
import com.che.zadali.sgoapp.ui.viewModels.ChangeControlQuestionViewModel
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

class ChangeControlQuestionFragment : Fragment() {

    private lateinit var binding: ChangeControlQuestionFragmentBinding
    private val viewModel: ChangeControlQuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChangeControlQuestionFragmentBinding.inflate(inflater, container, false)

        viewModel.question.observe(viewLifecycleOwner) {
            binding.selectedControlQuestion.text = it
        }
        binding.controlQuestionCard.setOnClickListener {  }//TODO onClick


        return binding.root
    }

}