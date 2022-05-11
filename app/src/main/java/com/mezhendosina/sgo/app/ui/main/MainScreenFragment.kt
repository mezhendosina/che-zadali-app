package com.mezhendosina.sgo.app.ui.main

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainActivityBinding
import com.mezhendosina.sgo.app.navigator
import com.mezhendosina.sgo.app.ui.journal.JournalFragment

class MainScreenFragment : Fragment() {

    private lateinit var binding: MainActivityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainActivityBinding.inflate(inflater, container, false)

        binding.toolbar.title = "Главная"

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.settings) {
                navigator().settings()
            }
            true
        }

        childFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, MainFragment())
            .commit()


        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main -> {
                    binding.toolbar.title = "Главная"
                    childFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(binding.fragmentContainer.id, MainFragment())
                        .commit()
                    true
                }
                R.id.journal -> {
                    binding.toolbar.title = "Дневник"
                    childFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(binding.fragmentContainer.id, JournalFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        return binding.root
    }
}
