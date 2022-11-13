package com.mezhendosina.sgo.app.ui.updateBottomSheet

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ModalSheetUpdateBinding
import com.mezhendosina.sgo.app.ui.container.ContainerViewModel
import com.mezhendosina.sgo.data.requests.other.entities.checkUpdates.CheckUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

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

        binding.updateLog.text = updateLog
        binding.updateButton.setOnClickListener {
            this.dismiss()
            onUpdateClickListener.invoke()
        }
    }

    companion object {
        const val UPDATE_LOG = "update_log"
        const val TAG = "UpdateBottomSheet"
        fun newInstance(
            updates: CheckUpdates,
            viewModel: ContainerViewModel,
            file: File
        ): UpdateBottomSheetFragment {
            val modalSheet = UpdateBottomSheetFragment(updates.body) {
                CoroutineScope(Dispatchers.IO).launch {
                    updates.assets.forEach {
                        if (it.contentType == "application/vnd.android.package-archive") {
                            viewModel.downloadUpdate(
                                Singleton.getContext(),
                                file,
                                it.browserDownloadUrl
                            )
                        }
                    }
                }
            }
            return modalSheet
        }
    }
}