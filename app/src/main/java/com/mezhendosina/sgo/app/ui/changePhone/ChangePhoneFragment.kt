package com.mezhendosina.sgo.app.ui.changePhone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentChangePhoneNumberBinding

class ChangePhoneFragment : Fragment(R.layout.fragment_change_phone_number) {

    lateinit var binding: FragmentChangePhoneNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    private var phone = arguments?.getString(PHONE_NUMBER)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChangePhoneNumberBinding.bind(view)

        binding.textInputLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                phone = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // from stackoverflow
            override fun afterTextChanged(editable: Editable) {
                val text = binding.textInputLayout.editText?.text.toString()
                val textLength = binding.textInputLayout.editText?.text?.length

                if (text.endsWith("-") || text.endsWith(" ")) {
                    return
                }

                if (textLength == 1) {
                    if (!text.contains("+")) {
                        setText(StringBuilder(text).insert(text.length - 1, "+").toString())
                    }
                } else if (textLength == 3) {
                    if (!text.contains("(")) {
                        setText(StringBuilder(text).insert(text.length - 1, " (").toString())
                    }
                } else if (textLength == 8) {
                    if (!text.contains(")")) {
                        setText(StringBuilder(text).insert(text.length - 1, ") ").toString())
                    }
                } else if (textLength == 13) {
                    if (!text.contains("-")) {
                        setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                    }
                }
            }

            private fun setText(text: String) {
                binding.textInputLayout.editText?.removeTextChangedListener(this)
                binding.textInputLayout.editText?.text?.length?.let {
                    binding.textInputLayout.editText?.editableText?.replace(
                        0,
                        it,
                        text
                    )
                }
                binding.textInputLayout.editText?.setSelection(text.length)
                binding.textInputLayout.editText?.addTextChangedListener(this)
            }
        })

        binding.fab.setOnClickListener {
            val phoneBundle =
                bundleOf(PHONE_NUMBER to binding.textInputLayout.editText?.text.toString())

            findNavController().navigate(
                R.id.action_changePhoneFragment_to_settingsFragment,
                phoneBundle
            )
        }
    }

    companion object {
        const val PHONE_NUMBER = "phone_number"
        const val PHONE_VISIBILITY = "phone_visibility"
    }
}