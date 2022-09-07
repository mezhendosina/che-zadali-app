package com.mezhendosina.sgo.app.ui.changeBirthday

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChangeBirthDateBinding

class ChangeBirthdayFragment : Fragment(R.layout.fragment_change_birth_date) {

    lateinit var binding: FragmentChangeBirthDateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChangeBirthDateBinding.bind(view)


        binding.textInputLayout.editText?.setText(arguments?.getString(BIRTHDAY_DATE))

        binding.fab.setOnClickListener {

        }
    }

    companion object {
        const val BIRTHDAY_DATE = "birthday_date"
    }

}