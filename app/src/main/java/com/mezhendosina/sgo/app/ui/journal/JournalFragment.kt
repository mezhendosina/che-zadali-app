package com.mezhendosina.sgo.app.ui.journal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.JournalFragmentBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.app.ui.adapters.DiaryAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnHomeworkClickListener
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.diary.diary.Lesson
import java.text.SimpleDateFormat
import java.util.*

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
                    activity?.findNavController(R.id.container)?.navigate(
                        R.id.moreFragment,
                        bundleOf("lessonId" to p1.classmeetingId, "type" to "journal")
                    )
                }
            })

            binding.weekSelectorLayout.nextWeekButton.setOnClickListener {
                viewModel.nextWeek(requireContext(), binding.swipeRefresh)
            }

            binding.weekSelectorLayout.prevWeekButton.setOnClickListener {
                viewModel.previousWeek(requireContext(), binding.swipeRefresh)

            }
            binding.weekSelectorLayout.weekSelectorTextView.setOnClickListener() {
                Singleton.currentWeek = 0
                viewModel.refreshDiary(requireContext(), binding.swipeRefresh)
            }
            viewModel.diary.observe(viewLifecycleOwner) {
                diaryAdapter.diary = it
                binding.weekSelectorLayout.weekSelectorTextView.text =
                    "${dateToRussian(Singleton.diary.diaryResponse.weekStart)} - ${
                        dateToRussian(
                            Singleton.diary.diaryResponse.weekEnd
                        )
                    }"
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

    @SuppressLint("SimpleDateFormat")
    fun dateToRussian(date: String): String {
        val a = SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(date)
        val locale = Locale("ru", "RU")

        return SimpleDateFormat("dd.MM.yyyyг.", locale).format(a!!).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

    }
}