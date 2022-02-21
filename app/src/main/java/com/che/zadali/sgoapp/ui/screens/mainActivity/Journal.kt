package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.che.zadali.sgoapp.data.adapters.DiaryAdapter
import com.che.zadali.sgoapp.databinding.FragmentJournalBinding
import com.che.zadali.sgoapp.ui.factory
import com.che.zadali.sgoapp.ui.viewModels.JournalViewModel
import com.google.android.material.transition.MaterialFadeThrough


class Journal : Fragment() {
    private lateinit var binding: FragmentJournalBinding
    private lateinit var diaryAdapter: DiaryAdapter

    private val viewModel: JournalViewModel by viewModels { factory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        startPostponedEnterTransition()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        diaryAdapter = DiaryAdapter()
        binding = FragmentJournalBinding.inflate(inflater, container, false)

        viewModel.weekDays.observe(viewLifecycleOwner) {
            diaryAdapter.diary = it
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.diaryRecyclerView.adapter = diaryAdapter
        binding.diaryRecyclerView.layoutManager = layoutManager
        return binding.root
    }
}