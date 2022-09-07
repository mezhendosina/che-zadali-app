package com.mezhendosina.sgo.app.ui.journal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialFadeThrough
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentJournalBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.adapters.LoadingStateAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class JournalFragment : Fragment(R.layout.fragment_journal) {

    private lateinit var binding: FragmentJournalBinding
    private val viewModel: JournalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        viewModel.loadDiary(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentJournalBinding.bind(view)

        val adapter = JournalPagerAdapter(
            findTopNavController(),
            object : CurrentItemListener {
                override fun invoke(): Int = binding.journalPager.currentItem
            }
        )
        Singleton.currentYearId.observe(viewLifecycleOwner) {
            adapter.refresh()
        }

        val footerAdapter = LoadingStateAdapter()

        val adapterWithLoadState =
            adapter.withLoadStateHeaderAndFooter(footerAdapter, footerAdapter)

        binding.journalPager.adapter = adapterWithLoadState
        observeDiary(adapter)
    }

    private fun observeDiary(adapter: JournalPagerAdapter) {
        lifecycleScope.launch {
            viewModel.diaryEntity.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
            adapter.loadStateFlow.collectLatest {
                println(it.refresh)
            }
        }
    }
}