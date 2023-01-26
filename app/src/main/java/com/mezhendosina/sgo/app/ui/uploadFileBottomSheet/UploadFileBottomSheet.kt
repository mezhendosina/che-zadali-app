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

package com.mezhendosina.sgo.app.ui.uploadFileBottomSheet

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetUploadFileBinding
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.FileUiEntity

class UploadFileBottomSheet(
    private val actionType: Int,
    private val assignmentId: Int,
    private val file: FileUiEntity? = null,
    private val onSuccess: () -> Unit
) :
    BottomSheetDialogFragment(R.layout.bottom_sheet_upload_file) {

    private lateinit var binding: BottomSheetUploadFileBinding
    private val viewModel: UploadFileViewModel by viewModels()

    private val filePath = MutableLiveData<ActivityResult>()

    private val selectFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            filePath.value = it
        }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = BottomSheetUploadFileBinding.bind(view)

        if (file != null) {
            binding.description.editText?.setText(file.description)
            binding.selectFile.text = file.fileName
        }
        when (actionType) {
            EDIT_DESCRIPTION -> {
                binding.header.setText(R.string.edit_description)
                binding.selectFile.isEnabled = false
                binding.sendFileHeader.setText(R.string.edit_description)
                binding.sendFile.setOnClickListener {
                    editDescription(binding.description.editText?.text.toString())
                }
            }
            REPLACE_FILE -> {
                binding.header.setText(R.string.replace_file)
                binding.sendFileHeader.setText(R.string.replace_file)
                binding.sendFile.setOnClickListener { replaceFile() }
            }
            else -> {
                binding.header.setText(R.string.upload_file)
                binding.sendFile.setOnClickListener {
                    sendFile()
                }
            }
        }


        binding.selectFile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (requireContext().checkSelfPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                }
            } else {
                if (requireContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            selectFile.launch(intent)
        }

        filePath.observe(viewLifecycleOwner) {
            if (it != null) {
                val path =
                    getFileNameFromUri(requireContext(), filePath.value?.data?.data)
                binding.selectFile.text = path?.substring(path.lastIndexOf("/") + 1)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.sendFileHeader.isVisible = !it
            binding.sendFileProgress.isVisible = it

        }

        observeErrors()
        observeSuccess()
    }

    private fun sendFile() {
        val file = filePath.value?.data?.data
        if (file != null) {
            viewModel.sendFile(
                assignmentId,
                file,
                binding.description.editText?.text.toString()
            )
        }
    }

    private fun editDescription(description: String) {
        viewModel.editFileDescription(file?.id ?: 0, description)
    }

    private fun replaceFile() {
        TODO()
    }

    private fun observeSuccess() {
        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                onSuccess.invoke()
                dismiss()
            }
        }

    }

    private fun observeErrors() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val UPLOAD_FILE = 0
        const val EDIT_DESCRIPTION = 1
        const val REPLACE_FILE = 2
    }
}
