package com.mezhendosina.sgo.app.ui.updateBottomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.BuildConfig
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
    private val updateLog: CheckUpdates,
    private val onUpdateClickListener: onUpdateClickListener,
    private val onCancel: () -> Unit
) :
    BottomSheetDialogFragment(R.layout.modal_sheet_update) {

    private lateinit var binding: ModalSheetUpdateBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ModalSheetUpdateBinding.bind(view)

        binding.appVersion.text = BuildConfig.VERSION_NAME + " ➡ " + updateLog.tagName
        binding.updateLog.text = updateLog.body
        binding.updateButton.setOnClickListener {
            this.dismiss()
            onUpdateClickListener.invoke()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancel.invoke()
    }

    companion object {
        const val UPDATE_LOG = "update_log"
        const val TAG = "UpdateBottomSheet"
        fun newInstance(
            updates: CheckUpdates,
            viewModel: ContainerViewModel,
            file: File
        ): UpdateBottomSheetFragment {
            val modalSheet = UpdateBottomSheetFragment(
                updates,
                onUpdateClickListener = {
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
                },
                onCancel = {
                    viewModel.changeUpdateDialogState(false)
                }
            )
            return modalSheet
        }
    }
}