package com.che.zadali.sgoapp.ui.screens.loginActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.data.adapters.SchoolActionListener
import com.che.zadali.sgoapp.data.adapters.SchoolsAdapter
import com.che.zadali.sgoapp.databinding.ChooseSchoolFragmentBinding
import com.che.zadali.sgoapp.ui.factory
import com.che.zadali.sgoapp.ui.navigator
import com.che.zadali.sgoapp.ui.viewModels.SchoolsListViewModel
import com.google.android.material.transition.MaterialSharedAxis

class ChooseSchoolFragment : Fragment() {

    private lateinit var binding: ChooseSchoolFragmentBinding
    private lateinit var adapter: SchoolsAdapter

    private val viewModel: SchoolsListViewModel by viewModels { factory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChooseSchoolFragmentBinding.inflate(inflater, container, false)
        adapter = SchoolsAdapter(object : SchoolActionListener {
            override fun onClick(schoolItem: SchoolItem) {
                navigator().login(schoolItem.schoolId, binding.editTextChooseSchool.text.toString())
            }

        })
        val chosenSchool = requireArguments().getString(ARG_TYPED_SCHOOl)
        if (chosenSchool != null){
            binding.editTextChooseSchool.setText(chosenSchool)
        }
        binding.editTextChooseSchool.addTextChangedListener(onTextChanged = { it, _, _, _ ->
            viewModel.searchSchool(it.toString())
        })

        binding.toolbar.setNavigationOnClickListener {
            navigator().goBack()
        }
        viewModel.schools.observe(viewLifecycleOwner) {
            adapter.schools = it
            container?.doOnPreDraw {
                startPostponedEnterTransition()
            }

        }
        viewModel.inProgress.observe(viewLifecycleOwner) {
            if (it) {
                binding.schoolRecyclerView.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
                binding.schoolRecyclerView.visibility = View.VISIBLE

            }
        }
        val layoutManager = LinearLayoutManager(requireContext())

        binding.schoolRecyclerView.layoutManager = layoutManager
        binding.schoolRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

        private const val ARG_TYPED_SCHOOl = "ARG_TYPED_SCHOOl"

        fun newInstance(string: String?): Fragment {
            val fragment = ChooseSchoolFragment()
            fragment.arguments = bundleOf(ARG_TYPED_SCHOOl to string)
            return fragment
        }
    }
}

