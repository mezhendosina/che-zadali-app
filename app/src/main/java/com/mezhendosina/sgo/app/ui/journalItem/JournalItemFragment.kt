package com.mezhendosina.sgo.app.ui.journalItem

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentItemJournalBinding
import com.mezhendosina.sgo.app.databinding.FragmentJournalBinding

class JournalItemFragment : Fragment(R.layout.fragment_item_journal) {

    private lateinit var binding: FragmentItemJournalBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemJournalBinding.bind(view)


    }
}