package com.mezhendosina.sgo.app.ui.uploadFileBottomSheet

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetUploadFileBinding
import com.mezhendosina.sgo.data.requests.homework.entities.File
import java.security.Permission
import java.util.jar.Manifest

class UploadFileBottomSheet(
    private val assignmentId: Int,
    private val file: File? = null
) :
    BottomSheetDialogFragment(R.layout.bottom_sheet_upload_file) {

    private lateinit var binding: BottomSheetUploadFileBinding
    private val viewModel: UploadFileViewModel by viewModels()

    private val fileIntent = MutableLiveData<String>()

    private val selectFile =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            fileIntent.value = it.toString()
        }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {

            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = BottomSheetUploadFileBinding.bind(view)

        if (file != null) {
            binding.description.editText?.setText(file.description)
            binding.sendFile.text = file.fileName
        }

        binding.selectFile.setOnClickListener {
            if (requireContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            selectFile.launch(arrayOf("*/*"))
        }

        fileIntent.observe(viewLifecycleOwner) {
            if (it != null) {
                val path = it
                binding.selectFile.text = path.substring(path.lastIndexOf("/") + 1)
            }
        }

        binding.sendFile.setOnClickListener {
            if (fileIntent.value != null) {
                try {
                    viewModel.sendFile(
                        assignmentId,
                        fileIntent.value!!,
                        binding.description.editText?.text.toString()
                    )
                } catch (e: Exception) {
                    println(e.stackTraceToString())
                }
            }
        }
    }
}