package com.mezhendosina.sgo.app.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.PagingData
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.JournalFragmentBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.NewDiaryAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnHomeworkClickListener
import com.mezhendosina.sgo.data.layouts.diary.Diary
import com.mezhendosina.sgo.data.layouts.diary.diary.Lesson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class JournalFragment : Fragment() {

    private lateinit var binding: JournalFragmentBinding
    private val viewModel: JournalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadDiary(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JournalFragmentBinding.inflate(inflater, container, false)
        val adapter = NewDiaryAdapter(
            object : OnHomeworkClickListener {
                override fun invoke(p1: Lesson) {
                    findTopNavController().navigate(
                        R.id.action_containerFragment_to_lessonFragment,
                        bundleOf("lessonId" to p1.classmeetingId, "type" to "journal")
                    )
                }
            })

        binding.journalPager.adapter = adapter

        observeDiary(adapter)

        return binding.root
    }

    private fun observeDiary(adapter: NewDiaryAdapter) {
        lifecycleScope.launch {
            viewModel.diary.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}