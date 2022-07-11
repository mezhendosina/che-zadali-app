package com.mezhendosina.sgo.app.ui.container

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.bottomSheets.AnnouncementsBottomSheet
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.checkUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ContainerFragment : Fragment(R.layout.container_main) {

    private lateinit var binding: ContainerMainBinding

    private val file: File = File.createTempFile("app", "apk")
    private val downloadState = MutableLiveData(0)

    private val viewModel: ContainerViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        viewModel.loadAnnouncements(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            checkUpdates(requireContext(), file, downloadState, childFragmentManager)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = ContainerMainBinding.bind(view)
        val navHost = childFragmentManager.findFragmentById(R.id.tabs_container) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNavigation.setupWithNavController(navController)
        NavigationUI.setupWithNavController(
            binding.toolbar,
            navController,
            AppBarConfiguration(setOf(R.id.gradesFragment, R.id.journalFragment))
        )


        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    findTopNavController().navigate(
                        R.id.action_containerFragment_to_settingsContainer,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.containerFragment, false).build()
                    )
                    true
                }
                R.id.announcements -> {
                    AnnouncementsBottomSheet(viewModel.announcements).show(
                        childFragmentManager,
                        AnnouncementsBottomSheet.TAG
                    )
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
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
    }
}
