package com.mezhendosina.sgo.app.ui.chooseRegion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseRegionBinding

class ChooseRegionFragment : Fragment(R.layout.fragment_choose_region) {

    private var binding: FragmentChooseRegionBinding? = null

    internal val viewModel: ChooseRegionViewModel by viewModels()
    private val adapter = ChooseRegionAdapter(
        object : OnRegionClickListener {
            override fun invoke(id: String) {
                viewModel.setRegion(
                    arguments?.getInt("from"),
                    id,
                    findNavController()
                )
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChooseRegionBinding.bind(view)
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        if (binding != null) {
            binding!!.noFindQuestion.isVisible = remoteConfig.getBoolean("enableRequestRegion")
            binding!!.noFindQuestion.setOnClickListener {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/che_zadaliBot?start=che")
                    )
                startActivity(intent)
            }
            binding!!.schoolList.adapter = adapter
            binding!!.schoolList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        observeRegions()
        observeErrors()
        observeLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun observeRegions() {
        viewModel.regions.observe(viewLifecycleOwner) {
            adapter.regions = it
        }
    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (binding != null) {
                if (it.isNotEmpty()) {
                    binding!!.loadError.errorDescription.text = it
                    binding!!.loadError.root.visibility = View.VISIBLE
                } else {
                    binding!!.loadError.root.visibility = View.GONE
                }
            }
        }

    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (binding != null) {
                binding!!.progressIndicator.isVisible = it
                if (it) {
                    binding!!.loadError.root.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val FROM_MAIN_ACTIVITY = 0

        fun newInstance(from: Int): ChooseRegionFragment {
            val args = bundleOf("from" to from)

            val fragment = ChooseRegionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}