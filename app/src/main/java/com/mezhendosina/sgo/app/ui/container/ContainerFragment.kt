package com.mezhendosina.sgo.app.ui.container

import android.os.Bundle
import android.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.announcementsBottomSheet.AnnouncementsBottomSheet
import com.mezhendosina.sgo.app.ui.updateBottomSheet.UpdateBottomSheetFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ContainerFragment : Fragment(R.layout.container_main) {

    private lateinit var binding: ContainerMainBinding

    private val file: File = File.createTempFile("app", "apk")

    private val viewModel: ContainerViewModel by viewModels()

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.journalFragment -> {
                    binding.tabsLayout.slideDownAnimation()
                }
                R.id.gradesFragment -> {
                    binding.tabsLayout.slideUpAnimation()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.checkUpdates()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ContainerMainBinding.bind(view)
        Singleton.journalTabsLayout = binding.tabsLayout
        val navHost = childFragmentManager.findFragmentById(R.id.tabs_container) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNavigation.setupWithNavController(navController)
        NavigationUI.setupWithNavController(
            binding.toolbar,
            navController,
            AppBarConfiguration(setOf(R.id.gradesFragment, R.id.journalFragment))
        )

        binding.toolbar.setOnMenuItemClickListener { setupOnMenuItemClickListener(it) }

        navController.addOnDestinationChangedListener(onDestinationChangedListener)
        observeDownloadState()
        observeUpdates()
        Singleton.transition.observe(viewLifecycleOwner) {
            if (it == true) {
                Singleton.transition.value = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        file.delete()
        findNavController().removeOnDestinationChangedListener(onDestinationChangedListener)
    }


    private fun setupOnMenuItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.update_available -> {
                UpdateBottomSheetFragment.newInstance(
                    viewModel.latestUpdate.value!!, viewModel, file
                ).show(childFragmentManager, UpdateBottomSheetFragment.TAG)
                true
            }
            R.id.settings -> {
                findTopNavController().navigate(
                    R.id.action_containerFragment_to_settingsContainer,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.containerFragment, false).build()
                )
                true
            }
            R.id.announcements -> {
                AnnouncementsBottomSheet().show(
                    childFragmentManager, AnnouncementsBottomSheet.TAG
                )
                true
            }
            else -> false
        }

    }


    private fun observeUpdates() {
        viewModel.latestUpdate.observe(viewLifecycleOwner) { updates ->
            if (updates.tagName != BuildConfig.VERSION_NAME && viewModel.showUpdateDialog.value != false) {
                val modalSheet = UpdateBottomSheetFragment.newInstance(updates, viewModel, file)
                modalSheet.show(childFragmentManager, UpdateBottomSheetFragment.TAG)
            }
            if (updates.tagName != BuildConfig.VERSION_NAME) binding.toolbar.menu[0].isVisible =
                true

        }
    }

    private fun observeDownloadState() {
        viewModel.downloadState.observe(viewLifecycleOwner) {
            val updateProgress = binding.updateProgress.root

            if (it) {
                updateProgress.visibility = View.VISIBLE
                binding.updateProgress.updateProgress.isIndeterminate = true
            } else {
                updateProgress.visibility = View.GONE
            }
        }
    }


    private fun TabLayout.slideDownAnimation() {
        this.apply {
            val materialFade = MaterialSharedAxis(MaterialSharedAxis.Y, false)
            TransitionManager.beginDelayedTransition(this, materialFade)
            visibility = View.VISIBLE
        }
    }

    private fun TabLayout.slideUpAnimation() {
        val materialFade = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        TransitionManager.beginDelayedTransition(this, materialFade)
        this.visibility = View.GONE

    }

}
