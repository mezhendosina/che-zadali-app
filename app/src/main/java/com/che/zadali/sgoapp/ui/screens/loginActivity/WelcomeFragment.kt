package com.che.zadali.sgoapp.ui.screens.loginActivity

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.WelcomeBinding
import com.che.zadali.sgoapp.ui.navigator
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

class WelcomeFragment : Fragment() {

    private lateinit var binding: WelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition = MaterialFadeThrough()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeBinding.inflate(inflater, container, false)
        binding.loginButton.setOnClickListener {
            navigator().chooseSchool()
        }
        return binding.root
    }
}
