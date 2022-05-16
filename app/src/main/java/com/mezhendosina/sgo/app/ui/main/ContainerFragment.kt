package com.mezhendosina.sgo.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainContainerBinding
import com.mezhendosina.sgo.app.ui.journal.JournalFragment
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.checkUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ContainerFragment : Fragment() {

    private lateinit var binding: MainContainerBinding
    private lateinit var navController: NavController

    private val file: File = File.createTempFile("app", "apk")
    private val downloadState = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        CoroutineScope(Dispatchers.IO).launch {
            checkUpdates(requireContext(), file, downloadState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainContainerBinding.inflate(inflater, container, false)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    activity?.findNavController(R.id.container)?.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.journalFragment -> {
                    binding.toolbar.setTitle(R.string.journal)
                    childFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentContainerView.id, JournalFragment())
                        .commit()
                    true
                }
                R.id.mainFragment -> {
                    binding.toolbar.setTitle(R.string.main)
                    childFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentContainerView.id, MainFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        downloadState.observe(viewLifecycleOwner) {
            val updateProgress = binding.updateProgress.root

            when (it) {
                100, 0 -> {
                    updateProgress.visibility = View.GONE
                }
                1 -> {
                    showAnimation(updateProgress)
                }
                else -> {
                    binding.updateProgress.updateProgress.setProgressCompat(it, true)
                }
            }
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
    }
}
