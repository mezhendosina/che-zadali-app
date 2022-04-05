package com.che.zadali.sgoapp.activities.modalDrawerActivities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.NoMainContainerBinding
import com.che.zadali.sgoapp.navigators.Navigator
import com.che.zadali.sgoapp.ui.screens.menuFragments.MessageItemFragment
import com.che.zadali.sgoapp.ui.screens.menuFragments.MessagesFragment
import com.che.zadali.sgoapp.ui.screens.menuFragments.NewMessageFragment

class MessagesActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: NoMainContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.messages)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.fab.setOnClickListener {
            binding.collapsingtoolbarlayout.titleCollapseMode =
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.no_main_container_fragment, NewMessageFragment(binding.collapsingtoolbarlayout))
                .commit()
        }
        messages()
    }

    override fun messageItem(messageId: Int) {
        binding.fab.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(
                R.id.no_main_container_fragment,
                MessageItemFragment.newInstance(messageId, binding.collapsingtoolbarlayout)
            )
            .commit()
    }

    override fun messages() {
        binding.fab.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(
                R.id.no_main_container_fragment,
                MessagesFragment(binding.collapsingtoolbarlayout)
            )
            .commit()
    }

    override fun goBack() {
    }

    override fun chooseSchool(typedSchool: String?) {
        TODO("Not yet implemented")
    }

    override fun login(schoolId: Int, typedSchool: String) {
        TODO("Not yet implemented")
    }

    override fun settings() {
        TODO("Not yet implemented")
    }

    override fun changePassword() {
        TODO("Not yet implemented")
    }

    override fun changeControlQuestion() {
        TODO("Not yet implemented")
    }


}