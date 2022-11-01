package com.mezhendosina.sgo.app.ui.chooseSchool

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseSchoolOrRegionBinding
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolUiEntity
import com.mezhendosina.sgo.app.ui.adapters.ChooseSchoolAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnSchoolClickListener
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.login.LoginFragment
import com.mezhendosina.sgo.app.ui.showAnimation

class ChooseSchoolFragment : Fragment(R.layout.fragment_choose_school_or_region) {

    private lateinit var binding: FragmentChooseSchoolOrRegionBinding
    private val viewModel: ChooseSchoolViewModel by viewModels()

    private val schoolAdapter = ChooseSchoolAdapter(object : OnSchoolClickListener {
        override fun invoke(p1: SchoolUiEntity) {
            findNavController().navigate(
                R.id.action_chooseSchoolFragment_to_loginFragment,
                bundleOf(LoginFragment.ARG_SCHOOL_ID to p1.schoolId)
            )
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSchools()

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseSchoolOrRegionBinding.bind(view)
        if (!binding.schoolEditText.text.isNullOrEmpty()) {
            schoolAdapter.schools =
                viewModel.findSchool(binding.schoolEditText.text.toString()) ?: emptyList()
        }

        binding.schoolEditText.addTextChangedListener(onTextChanged = { it, _, _, _ ->
            schoolAdapter.schools = viewModel.findSchool(it.toString()) ?: emptyList()
        })

        binding.loadError.retryButton.setOnClickListener {
            binding.schoolList.visibility = View.VISIBLE
            viewModel.loadSchools()
        }

        observeSchools()
        observeErrors()
        observeLoading()

        binding.schoolList.adapter = schoolAdapter

        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

        binding.schoolList.addItemDecoration(dividerItemDecoration)

        binding.schoolList.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun observeSchools() {
        viewModel.schools.observe(viewLifecycleOwner) {
            schoolAdapter.schools = it
        }
    }

    private fun observeErrors() {
        viewModel.isError.observe(viewLifecycleOwner) {
            if (it) {
                showAnimation(binding.loadError.root)
                binding.schoolList.visibility = View.GONE
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.loadError.errorDescription.text = it
        }

    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showAnimation(binding.progressIndicator)
                binding.loadError.root.visibility = View.GONE
            } else {
                hideAnimation(binding.progressIndicator, View.GONE)
                val schools = viewModel.findSchool(binding.schoolEditText.text.toString())
                if (schools != null) schoolAdapter.schools = schools
            }
        }
    }

}