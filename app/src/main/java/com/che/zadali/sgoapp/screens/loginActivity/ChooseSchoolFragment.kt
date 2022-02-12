package com.che.zadali.sgoapp.screens.loginActivity

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.data.schools.SchoolActionListener
import com.che.zadali.sgoapp.data.schools.SchoolsAdapter
import com.che.zadali.sgoapp.databinding.ChooseSchoolFragmentBinding
import com.che.zadali.sgoapp.screens.factory
import com.che.zadali.sgoapp.screens.navigator
import com.che.zadali.sgoapp.screens.viewModels.loginAcitivity.SchoolsListViewModel

class ChooseSchoolFragment : Fragment() {

    private lateinit var binding: ChooseSchoolFragmentBinding
    private lateinit var adapter: SchoolsAdapter

    private val viewModel: SchoolsListViewModel by viewModels { factory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChooseSchoolFragmentBinding.inflate(inflater, container, false)
        adapter = SchoolsAdapter(object : SchoolActionListener {
            override fun onClick(schoolItem: SchoolItem) {
                navigator().login(schoolItem.schoolId)
            }

        })

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
}

