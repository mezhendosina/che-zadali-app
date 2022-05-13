package com.mezhendosina.sgo.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainActivityBinding
import com.mezhendosina.sgo.app.navigator
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.journal.JournalFragment
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.checkUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainScreenFragment : Fragment() {

    private lateinit var binding: MainActivityBinding

    private val file: File = File.createTempFile("app", "apk")
    private val downloadState = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            checkUpdates(requireContext(), file, downloadState)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainActivityBinding.inflate(inflater, container, false)


        val actionBar = requireActivity().actionBar
        actionBar?.customView = binding.toolbar

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.settings) {
                navigator().settings()
            }
            true
        }

        downloadState.observe(viewLifecycleOwner) {
            val updateProgress = binding.updateProgress.root

            if (it == 100 || it == 0) {
                hideAnimation(updateProgress)
                updateProgress.visibility = View.GONE
            } else {
                showAnimation(updateProgress)
                binding.updateProgress.updateProgress.setProgressCompat(it, true)
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    childFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(binding.fragmentContainer.id, MainFragment())
                        .commit()
                    true
                }
                R.id.journalFragment -> {
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

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
    }
}
