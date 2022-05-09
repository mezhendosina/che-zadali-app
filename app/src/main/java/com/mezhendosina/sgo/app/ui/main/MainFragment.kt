package com.mezhendosina.sgo.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mezhendosina.sgo.app.databinding.MainFragmentBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.ui.adapters.AnnouncementsAdapter
import com.mezhendosina.sgo.app.ui.adapters.HomeworkAdapter

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private val viewModel: MainViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadTodayHomework(requireContext())
        viewModel.loadAnnouncements(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        val todayHomeworkAdapter = HomeworkAdapter()
        val announcementsAdapter = AnnouncementsAdapter()

        viewModel.todayHomework.observe(viewLifecycleOwner) {
            binding.todayHomework.day.text = viewModel.todayDate()
            todayHomeworkAdapter.lessons = it
        }

        viewModel.announcements.observe(viewLifecycleOwner) {
            announcementsAdapter.announcements = it
        }

        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(inflater.context)
        binding.announcementsRecyclerView.adapter = announcementsAdapter

        binding.todayHomework.homeworkRecyclerView.layoutManager =
            LinearLayoutManager(inflater.context)
        binding.todayHomework.homeworkRecyclerView.adapter = todayHomeworkAdapter
        return binding.root
    }


}