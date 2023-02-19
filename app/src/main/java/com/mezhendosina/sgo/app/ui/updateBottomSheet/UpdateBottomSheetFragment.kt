/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.mezhendosina.sgo.data.requests.github.checkUpdates.CheckUpdates
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
        val newVersion = requireContext().getString(
            R.string.new_version,
            BuildConfig.VERSION_NAME,
            updateLog.tagName
        )
        binding.appVersion.text = newVersion
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