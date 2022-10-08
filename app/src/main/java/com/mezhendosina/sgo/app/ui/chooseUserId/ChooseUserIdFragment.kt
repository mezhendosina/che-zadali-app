package com.mezhendosina.sgo.app.ui.chooseUserId

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseUserIdBinding

class ChooseUserIdFragment : Fragment(R.layout.fragment_choose_user_id) {

    private lateinit var binding: FragmentChooseUserIdBinding
    private val viewModel: ChooseUserIdViewModel by viewModels()

    private val adapter = UserIdAdapter { viewModel.login(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseUserIdBinding.bind(view)

        observeUsers()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeUsers() {
        viewModel.usersId.observe(viewLifecycleOwner) {
            adapter.users = it
            println(it)
        }
    }
}
