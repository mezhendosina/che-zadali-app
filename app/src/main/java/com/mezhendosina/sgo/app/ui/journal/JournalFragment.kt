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
import com.mezhendosina.sgo.app.ui.adapters.DiaryAdapter

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
        val diaryAdapter = DiaryAdapter()
        binding = JournalFragmentBinding.inflate(inflater, container, false)

        viewModel.diary.observe(viewLifecycleOwner) {
            diaryAdapter.diary = it
            println(it)
        }

        binding.diary.layoutManager = LinearLayoutManager(inflater.context)
        binding.diary.adapter = diaryAdapter

        return binding.root
    }

}