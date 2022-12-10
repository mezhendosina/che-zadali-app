package com.mezhendosina.sgo.app.ui.announcementsBottomSheet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetAnnouncementsBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.AnnouncementsAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnAnnouncementClickListener
import com.mezhendosina.sgo.data.requests.sgo.announcements.AnnouncementsResponseEntity

class AnnouncementsBottomSheet :
    BottomSheetDialogFragment(R.layout.bottom_sheet_announcements) {

    private lateinit var binding: BottomSheetAnnouncementsBinding

    private val viewModel: BottomSheetAnnouncementsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadAnnouncements()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = BottomSheetAnnouncementsBinding.bind(view)

        val announcementsAdapter = AnnouncementsAdapter(
            object : OnAnnouncementClickListener {
                override fun invoke(p1: AnnouncementsResponseEntity) {
                    findTopNavController().navigate(
                        R.id.action_containerFragment_to_announcementsFragment,
                        bundleOf(Singleton.ANNOUNCEMENTS_ID to p1.id)
                    )
                }
            }
        )

        viewModel.announcements.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.emptyState.root.visibility = View.VISIBLE
                    binding.emptyState.emptyText.text = "Объявлений нет"
                } else {
                    binding.emptyState.root.visibility = View.GONE
                }
            }
            announcementsAdapter.announcements = it
        }

        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.announcementsRecyclerView.adapter = announcementsAdapter
    }

    companion object {
        const val TAG = "AnnouncementsBottomSheet"
    }

    private fun observeLoading() {
        viewModel.loading.observe(viewLifecycleOwner) {
            TODO()
        }
    }
}