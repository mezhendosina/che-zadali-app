package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.ChangePasswordFragmentBinding
import com.che.zadali.sgoapp.ui.viewModels.ChangePasswordViewModel
import com.google.android.material.transition.MaterialFadeThrough

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: ChangePasswordFragmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChangePasswordFragmentBinding.inflate(inflater, container, false)
        //TODO net code
        return binding.root
    }
}