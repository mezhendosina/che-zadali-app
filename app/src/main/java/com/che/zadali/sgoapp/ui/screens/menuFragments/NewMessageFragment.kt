package com.che.zadali.sgoapp.ui.screens.menuFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.NewMessageFragmentBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.transition.MaterialSharedAxis

class NewMessageFragment(private val toolbarLayout: CollapsingToolbarLayout) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarLayout.title = getString(R.string.new_message)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = NewMessageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}