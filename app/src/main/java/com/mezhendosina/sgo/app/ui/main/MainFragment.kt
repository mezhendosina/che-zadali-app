package com.mezhendosina.sgo.app.ui.main

import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.MainFragmentBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.*
import com.mezhendosina.sgo.data.layouts.announcements.AnnouncementsResponseItem
import com.mezhendosina.sgo.data.layouts.diary.diary.Lesson
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private val viewModel: MainViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        viewModel.loadTodayHomework(requireContext())
        viewModel.loadAnnouncements(requireContext())
//            viewModel.loadGrades(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        val pastMandatoryAdapter = PastMandatoryAdapter()

        val todayHomeworkAdapter = HomeworkAdapter(object : OnHomeworkClickListener {
            override fun invoke(p1: Lesson) {
                findTopNavController().navigate(
                    R.id.action_containerFragment_to_lessonFragment,
                    bundleOf("lessonId" to p1.classmeetingId),
                )
            }
        })

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

        val gradeAdapter = GradeAdapter()

        viewModel.todayHomework.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.todayHomework.root.visibility = View.VISIBLE
                binding.todayHomework.day.text = viewModel.todayDate()
                todayHomeworkAdapter.lessons = it
            }
        }

        viewModel.todayAttachments.observe(viewLifecycleOwner) {
            todayHomeworkAdapter.attachments = it
        }

        viewModel.todayPastMandatory.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                pastMandatoryAdapter.items = it
                binding.pastMandatory.root.visibility = View.VISIBLE
            } else binding.pastMandatory.root.visibility = View.GONE

        }
        viewModel.announcements.observe(viewLifecycleOwner) {
            announcementsAdapter.announcements = it
        }

        viewModel.grades.observe(viewLifecycleOwner) {
            gradeAdapter.grades = it
        }


        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAll(requireContext(), binding.swipeRefresh)
        }


        binding.pastMandatory.pastMandatoryRecyclerView.layoutManager =
            LinearLayoutManager(inflater.context)
        binding.pastMandatory.pastMandatoryRecyclerView.adapter = pastMandatoryAdapter

        binding.todayHomework.homeworkRecyclerView.layoutManager =
            LinearLayoutManager(inflater.context)
        binding.todayHomework.homeworkRecyclerView.adapter = todayHomeworkAdapter

        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(inflater.context)
        binding.announcementsRecyclerView.adapter = announcementsAdapter

        return binding.root
    }


}