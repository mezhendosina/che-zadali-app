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

package com.mezhendosina.sgo.app.ui.gradesFlow.filter

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.radiobutton.MaterialRadioButton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetFilterBinding
import com.mezhendosina.sgo.app.uiEntities.FilterUiEntity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterBottomSheet(
    private val header: String,
    private val content: List<FilterUiEntity>,
    private val onApplyClickListener: (checkedId: Int) -> Unit
) : BottomSheetDialogFragment(R.layout.bottom_sheet_filter) {

    private var binding: BottomSheetFilterBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

            BottomSheetBehavior.from(bottomSheet).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = BottomSheetFilterBinding.bind(view)
        binding!!.header.text = header

        val selectableItemBackground = TypedValue()
        requireContext().theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            selectableItemBackground,
            true
        )
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        content.forEach {
            val button = MaterialRadioButton(requireContext())
            button.text = it.name
            button.id = it.id
            button.isChecked = it.checked
            button.isClickable = true
            binding!!.radioGroup.addView(button, layoutParams)
            if (it.checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding!!.radioGroupScrollView.scrollToDescendant(button)
            }
        }

        binding!!.button.setOnClickListener {
            dismiss()
            onApplyClickListener.invoke(binding!!.radioGroup.checkedRadioButtonId)
        }
    }
}