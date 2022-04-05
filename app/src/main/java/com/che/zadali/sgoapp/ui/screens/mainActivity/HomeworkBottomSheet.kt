package com.che.zadali.sgoapp.ui.screens.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.che.zadali.sgo_app.data.diary.Lesson
import com.che.zadali.sgoapp.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeworkBottomSheet(private val lessonItem: Lesson) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetBinding.inflate(inflater, container, false)
        binding.header.text = lessonItem.subjectName
        binding.text.text = lessonItem.assignments?.get(0)?.assignmentName
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}