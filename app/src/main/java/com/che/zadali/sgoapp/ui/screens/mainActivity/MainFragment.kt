package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.che.zadali.sgoapp.data.adapters.AnnouncementsActionListener
import com.che.zadali.sgoapp.data.adapters.AnnouncementsAdapter
import com.che.zadali.sgoapp.data.adapters.HomeworkAdapter
import com.che.zadali.sgoapp.data.dateToRussian
import com.che.zadali.sgoapp.data.layout.announcements.AnnouncementsDataItem
import com.che.zadali.sgoapp.databinding.FragmentMainBinding
import com.che.zadali.sgoapp.ui.factory
import com.che.zadali.sgoapp.ui.viewModels.MainScreenViewModel
import com.google.android.material.transition.MaterialFadeThrough

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainScreenViewModel by viewModels { factory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        viewModel.lessons.observe(viewLifecycleOwner) {
            binding.header.text = dateToRussian(it[0].day, true)
            val lessonsAdapter = HomeworkAdapter(it)
            binding.lessonsRecyclerView.adapter = lessonsAdapter
        }

        viewModel.announcements.observe(viewLifecycleOwner) {
            val adapter = AnnouncementsAdapter(it, parentFragmentManager,
                object : AnnouncementsActionListener {
                    override fun onClick(announcementsDataItem: AnnouncementsDataItem) {
                        AnnouncementsBottomSheet(announcementsDataItem).show(
                            parentFragmentManager,
                            AnnouncementsBottomSheet.TAG
                        )
                    }
                })
            binding.announcementsRecyclerView.adapter = adapter
        }

        val lessonLayoutManager = LinearLayoutManager(requireContext())
        binding.mainFragment.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.lessonsRecyclerView.layoutManager = lessonLayoutManager

        val announcementLayoutManager = LinearLayoutManager(requireContext())
        binding.announcementsRecyclerView.layoutManager = announcementLayoutManager

        return binding.root
    }
}