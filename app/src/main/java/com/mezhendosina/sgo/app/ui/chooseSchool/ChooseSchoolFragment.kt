package com.mezhendosina.sgo.app.ui.chooseSchool

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseSchoolBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.ui.adapters.ChooseSchoolAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnSchoolClickListener
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.login.LoginFragment
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.layouts.ResponseMessageException
import com.mezhendosina.sgo.data.layouts.schools.SchoolItem

class ChooseSchoolFragment : Fragment(R.layout.fragment_choose_school) {

    private lateinit var binding: FragmentChooseSchoolBinding
    private val viewModel: ChooseSchoolViewModel by viewModels { factory() }

    private var loading = MutableLiveData(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            viewModel.loadSchools(requireContext(), loading)
        } catch (e: ResponseMessageException) {
            println(e.message)
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChooseSchoolBinding.bind(view)
        val schoolAdapter = ChooseSchoolAdapter(object : OnSchoolClickListener {
            override fun invoke(p1: SchoolItem) {
                findNavController().navigate(
                    R.id.action_chooseSchoolFragment_to_loginFragment,
                    bundleOf(LoginFragment.ARG_SCHOOL_ID to p1.schoolId)
                )
            }
        })
        if (!binding.schoolEditText.text.isNullOrEmpty()) {
            schoolAdapter.schools =
                viewModel.findSchool(binding.schoolEditText.text.toString()) ?: emptyList()
        }

        binding.schoolEditText.addTextChangedListener(onTextChanged = { it, _, _, _ ->
            schoolAdapter.schools = viewModel.findSchool(it.toString()) ?: emptyList()

        })

        loading.observe(viewLifecycleOwner) {
            if (it) {
                showAnimation(binding.progressIndicator)
            } else {
                hideAnimation(binding.progressIndicator, View.GONE)
            }
        }

        viewModel.schools.observe(viewLifecycleOwner) {
            schoolAdapter.schools = it
        }

        binding.schoolList.adapter = schoolAdapter
        binding.schoolList.layoutManager = LinearLayoutManager(requireContext())
    }
}