package com.mezhendosina.sgo.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainFragmentBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.ui.adapters.AnnouncementsAdapter
import com.mezhendosina.sgo.app.ui.adapters.HomeworkAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnHomeworkClickListener
import com.mezhendosina.sgo.data.diary.diary.Lesson

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


        val parentFragmentNavController = parentFragment?.findNavController()
        val todayHomeworkAdapter = HomeworkAdapter(object : OnHomeworkClickListener {
            override fun invoke(p1: Lesson) {
                parentFragmentNavController?.navigate(
                    R.id.moreFragment,
                    bundleOf("lessonId" to p1.classmeetingId, "type" to "today")
                )
            }
        })

        val announcementsAdapter = AnnouncementsAdapter()

        viewModel.todayHomework.observe(viewLifecycleOwner) {
            println(it)
            binding.todayHomework.day.text = viewModel.todayDate()
            todayHomeworkAdapter.lessons = it
        }

        viewModel.announcements.observe(viewLifecycleOwner) {
            announcementsAdapter.announcements = it
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAll(requireContext(), binding.swipeRefresh)
        }

        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(inflater.context)
        binding.announcementsRecyclerView.adapter = announcementsAdapter

        binding.todayHomework.homeworkRecyclerView.layoutManager =
            LinearLayoutManager(inflater.context)
        binding.todayHomework.homeworkRecyclerView.adapter = todayHomeworkAdapter
        return binding.root
    }


}