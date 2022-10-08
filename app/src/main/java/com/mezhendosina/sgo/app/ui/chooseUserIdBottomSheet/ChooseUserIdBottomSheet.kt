package com.mezhendosina.sgo.app.ui.chooseUserIdBottomSheet

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChooseUserIdBinding
import com.mezhendosina.sgo.app.ui.chooseUserId.UserIdAdapter
import com.mezhendosina.sgo.app.ui.chooseUserId.toUiEntity
import com.mezhendosina.sgo.data.Settings
import kotlinx.coroutines.launch

class ChooseUserIdBottomSheet(
) : BottomSheetDialogFragment(R.layout.fragment_choose_user_id) {

    private lateinit var binding: FragmentChooseUserIdBinding


    private val adapter = UserIdAdapter {
        lifecycleScope.launch {
            Settings(requireContext()).setCurrentUserId(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseUserIdBinding.bind(view)

        binding.recyclerView.adapter = adapter

        adapter.users = Singleton.users.toUiEntity()
    }
}