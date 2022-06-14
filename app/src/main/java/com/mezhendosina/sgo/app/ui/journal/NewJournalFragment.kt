package com.mezhendosina.sgo.app.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mezhendosina.sgo.app.databinding.JournalFragmentBinding
import com.mezhendosina.sgo.app.ui.adapters.NewDiaryAdapter
import com.mezhendosina.sgo.app.ui.adapters.OnHomeworkClickListener
import com.mezhendosina.sgo.data.layouts.diary.diary.Lesson

class NewJournalFragment : Fragment() {

    private lateinit var binding: JournalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JournalFragmentBinding.inflate(inflater, container, false)

        binding.journalPager.adapter = NewDiaryAdapter(object : OnHomeworkClickListener {
            override fun invoke(p1: Lesson) {
                TODO("Not yet implemented")
            }
        }
        )

        return binding.root
    }
}