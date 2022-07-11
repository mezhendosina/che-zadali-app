package com.mezhendosina.sgo.app.ui.bottomSheets

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetAnnouncementsBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.AnnouncementsAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnAnnouncementClickListener
import com.mezhendosina.sgo.data.layouts.announcements.AnnouncementsResponseItem

class AnnouncementsBottomSheet(private val announcements: LiveData<List<AnnouncementsResponseItem>>) :
    BottomSheetDialogFragment(R.layout.bottom_sheet_announcements) {

    private lateinit var binding: BottomSheetAnnouncementsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = BottomSheetAnnouncementsBinding.bind(view)

        val announcementsAdapter = AnnouncementsAdapter(
            object : OnAnnouncementClickListener {
                override fun invoke(p1: AnnouncementsResponseItem) {
                    findTopNavController().navigate(
                        R.id.action_containerFragment_to_announcementsFragment,
                        bundleOf(Singleton.ANNOUNCEMENTS_ID to p1.id)
                    )
                }
            }
        )

        announcements.observe(viewLifecycleOwner) {
            announcementsAdapter.announcements = it
        }

        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.announcementsRecyclerView.adapter = announcementsAdapter
    }

    companion object {
        const val UPDATE_LOG = "update_log"
        const val TAG = "AnnouncementsBottomSheet"
    }
}