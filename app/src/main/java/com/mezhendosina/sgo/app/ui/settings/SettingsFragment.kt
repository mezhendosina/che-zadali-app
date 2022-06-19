package com.mezhendosina.sgo.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.activities.LoginActivity
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.databinding.SettingsFragmentBinding
import com.mezhendosina.sgo.app.findTopNavController
import com.mezhendosina.sgo.app.ui.login.LoginFragment
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.Settings
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        println(AppCompatDelegate.getDefaultNightMode())
        when (AppCompatDelegate.getDefaultNightMode()) {
            1 -> binding.lightTheme.isChecked = true
            2 -> binding.darkTheme.isChecked = true
            -1 ->{ binding.sameAsSystem.isChecked = true}
        }
        viewModel.loadProfilePhoto(requireContext(), binding.userPhoto)

//        viewModel.currentTheme.observe(viewLifecycleOwner) {
//            binding.changeThemeRadioGroup.check(it)
//        }
        viewModel.mySettings.observe(viewLifecycleOwner) {
            binding.userName.text = "${it.lastName} ${it.firstName} ${it.middleName}"
            binding.userLogin.text = it.loginName
            binding.birthday.editText?.setText(DateManipulation(it.birthDate).dateFormatter())
            binding.phoneNumber.editText?.setText(it.mobilePhone)
            binding.email.editText?.setText(it.email)
        }

        binding.logoutButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    viewModel.logout(requireContext())
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

            }
        }
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.cacheSize.text =
            "Объем кэша: ${(viewModel.calculateCache(requireContext())).toDouble()}"
        binding.clearCacheCard.setOnClickListener {

        }



        binding.changeThemeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.changeTheme(checkedId)
        }

        return binding.root
    }

//    fun setTheme(theme: Resources.Theme) {
//
//
//        val w = binding.root.measuredWidth
//        val h = binding.root.measuredHeight
//
//        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        binding.root.draw(canvas)
//
//
//
//
//        val finalRadius = hypot(w.toFloat(), h.toFloat())
//        val anim = ViewAnimationUtils.createCircularReveal(container, w / 2, h / 2, 0f, finalRadius)
//        anim.duration = 400L
//        anim.doOnEnd {
//            imageView.setImageDrawable(null)
//            imageView.isVisible = false
//        }
//        anim.start()
//    }
}
