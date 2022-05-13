package com.mezhendosina.sgo.app.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mezhendosina.sgo.app.databinding.JournalFragmentBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.navigator
import com.mezhendosina.sgo.app.ui.adapters.DiaryAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnHomeworkClickListener
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.diary.diary.Lesson

class JournalFragment : Fragment() {

    private lateinit var binding: JournalFragmentBinding

    private val viewModel: JournalViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDiary(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JournalFragmentBinding.inflate(inflater, container, false)
        requireActivity().actionBar?.title = "Дневник"
        try {
            val diaryAdapter = DiaryAdapter(object : OnHomeworkClickListener {
                override fun invoke(p1: Lesson) {
                    navigator().more(p1.classmeetingId, "journal")
                }
            })
            viewModel.diary.observe(viewLifecycleOwner) {
                diaryAdapter.diary = it
            }
            viewModel.attachments.observe(viewLifecycleOwner) {
                diaryAdapter.attachments = it
            }
            binding.swipeRefresh.setOnRefreshListener {
                viewModel.refreshDiary(requireContext(), binding.swipeRefresh)
            }
            binding.diary.layoutManager = LinearLayoutManager(inflater.context)
            binding.diary.adapter = diaryAdapter

        } catch (e: Exception) {
            errorDialog(requireContext(), e.message ?: "")
        }


        return binding.root
    }
}