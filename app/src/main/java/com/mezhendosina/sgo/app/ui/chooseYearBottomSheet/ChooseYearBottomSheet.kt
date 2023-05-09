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

package com.mezhendosina.sgo.app.ui.chooseYearBottomSheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetChooseYearBinding
import com.mezhendosina.sgo.data.requests.sgo.settings.entities.YearListResponseEntity

class ChooseYearBottomSheet(
    private val yearList: List<YearListResponseEntity>
) : BottomSheetDialogFragment(R.layout.bottom_sheet_choose_year) {

    private lateinit var binding: BottomSheetChooseYearBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = BottomSheetChooseYearBinding.bind(view)

        val adapter = ChooseYearAdapter(
            object : OnYearClickListener {
                override fun invoke(year: YearListResponseEntity) {
                    Singleton.currentYearId.value = year
                    dismissNow()
                }
            }
        )
        adapter.years = yearList
        binding.yearsRecyclerview.adapter = adapter
        binding.yearsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}