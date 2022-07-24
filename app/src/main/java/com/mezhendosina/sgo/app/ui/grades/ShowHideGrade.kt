package com.mezhendosina.sgo.app.ui.grades

import android.view.View
import com.mezhendosina.sgo.app.databinding.ItemGradeValueBinding

fun showBadGrade(binding: ItemGradeValueBinding) {
    binding.apply {
        badGrade.visibility = View.VISIBLE

        midGrade.visibility = View.GONE
        goodGrade.visibility = View.GONE
        dutyMark.visibility = View.GONE
    }
}

fun showMidGrade(binding: ItemGradeValueBinding) {
    binding.apply {
        midGrade.visibility = View.VISIBLE

        goodGrade.visibility = View.GONE
        badGrade.visibility = View.GONE
        dutyMark.visibility = View.GONE

    }
}

fun showGoodGrade(binding: ItemGradeValueBinding) {
    binding.apply {
        goodGrade.visibility = View.VISIBLE

        midGrade.visibility = View.GONE
        badGrade.visibility = View.GONE
        dutyMark.visibility = View.GONE
    }
}