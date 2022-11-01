package com.mezhendosina.sgo.app.ui.chooseRegion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseSchoolOrRegionBinding
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation

class ChooseRegionFragment : Fragment(R.layout.fragment_choose_school_or_region) {

    private lateinit var binding: FragmentChooseSchoolOrRegionBinding

    internal val viewModel: ChooseRegionViewModel by viewModels()
    private val adapter = ChooseRegionAdapter(
        object : OnRegionClickListener {
            override fun invoke(id: String) {
                viewModel.setRegion(
                    id,
                    findNavController()
                )
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChooseSchoolOrRegionBinding.bind(view)

        binding.schoolEditText.hint = "Выберите регион"
        binding.chooseSchoolTextView.visibility = View.GONE

        binding.noFindQuestion.visibility = View.VISIBLE
        binding.noFindQuestion.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://t.me/che_zadaliBot?start=che")
                ) //TODO replace che
            startActivity(intent)
        }
        binding.schoolList.adapter = adapter
        binding.schoolList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        observeRegions()
        observeErrors()
        observeLoading()
    }

    private fun observeRegions() {
        viewModel.regions.observe(viewLifecycleOwner) {
            adapter.regions = it
        }
    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.loadError.errorDescription.text = it
                binding.loadError.root.visibility = View.VISIBLE
            } else {
                binding.loadError.root.visibility = View.GONE
            }
        }

    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showAnimation(binding.progressIndicator)
                binding.loadError.root.visibility = View.GONE
            } else {
                hideAnimation(binding.progressIndicator, View.GONE)
            }
        }
    }
}