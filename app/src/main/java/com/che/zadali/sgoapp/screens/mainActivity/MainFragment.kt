package com.che.zadali.sgoapp.screens.mainActivity

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.che.zadali.sgo_app.data.dateToRussian
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.data.diary.TodayHomeworkAdapter
import com.che.zadali.sgoapp.data.schools.SchoolsAdapter
import com.che.zadali.sgoapp.databinding.MainScreenBinding
import com.che.zadali.sgoapp.screens.factory
import com.che.zadali.sgoapp.screens.viewModels.mainActivity.MainScreenViewModel

class MainFragment: Fragment() {
    private lateinit var binding: MainScreenBinding
    private lateinit var lessonsAdapter: TodayHomeworkAdapter

    private val viewModel: MainScreenViewModel by viewModels { factory() }
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
        binding = MainScreenBinding.inflate(inflater, container, false)
        lessonsAdapter = TodayHomeworkAdapter()
        viewModel.lessons.observe(viewLifecycleOwner){
            println(lessonsAdapter.lessons.firstOrNull()?.day)
            binding.header.text = dateToRussian(it[0].day, true)
            lessonsAdapter.lessons = it
        }


        val layoutManager = LinearLayoutManager(requireContext())

        binding.lessonsRecyclerView.layoutManager = layoutManager
        binding.lessonsRecyclerView.adapter = lessonsAdapter
        return binding.root
    }
}