package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.che.zadali.sgoapp.activities.SettingsActivity
import com.che.zadali.sgoapp.databinding.OtherFragmentBinding
import com.google.android.material.transition.MaterialFadeThrough

class OtherFragment : Fragment() {
    private lateinit var binding: OtherFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OtherFragmentBinding.inflate(inflater, container, false)

        binding.settings.setOnClickListener {
            startActivity(Intent(inflater.context, SettingsActivity::class.java))
        }

        return binding.root
    }
}