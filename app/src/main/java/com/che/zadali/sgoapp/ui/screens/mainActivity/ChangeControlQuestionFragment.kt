package com.che.zadali.sgoapp.ui.screens.mainActivity

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.ui.viewModels.ChangeControlQuestionViewModel

class ChangeControlQuestionFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeControlQuestionFragment()
    }

    private lateinit var viewModel: ChangeControlQuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_control_question_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangeControlQuestionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}