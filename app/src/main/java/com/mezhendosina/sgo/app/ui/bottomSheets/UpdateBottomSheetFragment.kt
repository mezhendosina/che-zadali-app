package com.mezhendosina.sgo.app.ui.bottomSheets

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ModalSheetUpdateBinding
import io.noties.markwon.Markwon

typealias onUpdateClickListener = () -> Unit

class UpdateBottomSheetFragment(
    private val updateLog: String,
    private val onUpdateClickListener: onUpdateClickListener
) :
    BottomSheetDialogFragment(R.layout.modal_sheet_update) {

    private lateinit var binding: ModalSheetUpdateBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ModalSheetUpdateBinding.bind(view)
        Markwon.create(requireContext()).setMarkdown(binding.updateLog, updateLog)

        binding.updateButton.setOnClickListener {
            this.dismiss()
            onUpdateClickListener.invoke()
        }
    }

    companion object {
        const val UPDATE_LOG = "update_log"
        const val TAG = "UpdateBottomSheet"
    }
}