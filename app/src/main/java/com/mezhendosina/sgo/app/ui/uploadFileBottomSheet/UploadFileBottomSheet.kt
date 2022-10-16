package com.mezhendosina.sgo.app.ui.uploadFileBottomSheet

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetUploadFileBinding
import com.mezhendosina.sgo.data.requests.homework.entities.File

class UploadFileBottomSheet(
    private val assignmentId: Int,
    private val file: File? = null
) :
    BottomSheetDialogFragment(R.layout.bottom_sheet_upload_file) {

    private lateinit var binding: BottomSheetUploadFileBinding
    private val viewModel: UploadFileViewModel by viewModels()

    private val filePath = MutableLiveData<Uri>()

    private val selectFile =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            filePath.value = it
            println()
        }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = BottomSheetUploadFileBinding.bind(view)

        if (file != null) {
            binding.description.editText?.setText(file.description)
            binding.sendFile.text = file.fileName
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            selectFile.launch(arrayOf("*/*"))
        }

        filePath.observe(viewLifecycleOwner) {
            if (it != null) {
                val path = it.query
                binding.selectFile.text = path?.substring(path.lastIndexOf("/") + 1)
            }
        }

        binding.sendFile.setOnClickListener {
            if (filePath.value != null) {
                try {
                    viewModel.sendFile(
                        assignmentId,
                        filePath.value!!.normalizeScheme(),
                        binding.description.editText?.text.toString()
                    )
                } catch (e: Exception) {
                    println(e.stackTraceToString())
                }
            }
        }
    }

}
