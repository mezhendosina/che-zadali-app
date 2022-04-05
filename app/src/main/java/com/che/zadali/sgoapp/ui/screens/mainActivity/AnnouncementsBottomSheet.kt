package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.data.adapters.AnnouncementsFilesAdapter
import com.che.zadali.sgoapp.data.dateToRussianWithTime
import com.che.zadali.sgoapp.data.layout.announcements.AnnouncementsDataItem
import com.che.zadali.sgoapp.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AnnouncementsBottomSheet(val data: AnnouncementsDataItem) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetBinding.inflate(inflater, container, false)

        binding.header.text = data.name
        binding.text.text = data.description
        binding.author.text = data.author.fio
        binding.datePublishing.text =
            getString(R.string.publish_date, dateToRussianWithTime(data.postDate))
        if (data.attachments.isNotEmpty()) {
            binding.files.visibility = View.VISIBLE
            binding.filesDivider.visibility = View.VISIBLE
            binding.filesRecyclerView.visibility = View.VISIBLE

            binding.filesRecyclerView.adapter = AnnouncementsFilesAdapter(data.attachments)
            binding.filesRecyclerView.layoutManager =
                LinearLayoutManager(inflater.context, LinearLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}