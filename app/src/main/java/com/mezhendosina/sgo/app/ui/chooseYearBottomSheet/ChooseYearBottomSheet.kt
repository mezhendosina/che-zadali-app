package com.mezhendosina.sgo.app.ui.chooseYearBottomSheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.BottomSheetChooseYearBinding
import com.mezhendosina.sgo.data.requests.settings.entities.YearListResponseEntity

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
                    Singleton.currentYearId.value = year.id
                    dismissNow()
                }
            }
        )
        adapter.years = yearList
        binding.yearsRecyclerview.adapter = adapter
        binding.yearsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        const val TAG = "choose_school_bottom_sheet_tag"
    }
}