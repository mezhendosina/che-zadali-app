package com.che.zadali.sgoapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.activities.modalDrawerActivities.*
import com.che.zadali.sgoapp.databinding.MainContainerBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainContainerBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val drawer = binding.drawerLayout
        val bottomNavigationView = binding.bottomBar
        val navHeaderView = binding.navDrawer.inflateHeaderView(R.layout.modal_drawer_header)
        navHeaderView.findViewById<TextView>(R.id.login).text = "МеньшенинЕ1" //TODO net code
        navHeaderView.findViewById<TextView>(R.id.name).text = "Меньшенин Евгений"
        val navController = findNavController(R.id.fragmentContainerView)
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.main, R.id.journal), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)
        bottomNavigationView.setupWithNavController(navController)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            drawer.open()
        }


        binding.navDrawer.setNavigationItemSelectedListener { a ->
            when (a.title) {
                getString(R.string.settings) -> startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java
                    )
                )
                getString(R.string.assessment) -> startActivity(
                    Intent(
                        this,
                        GradesActivity::class.java
                    )
                )
                getString(R.string.diary) -> startActivity(
                    Intent(
                        this,
                        CalendarActivity::class.java
                    )
                )
                getString(R.string.messages) -> startActivity(
                    Intent(
                        this,
                        MessagesActivity::class.java
                    )
                )
                getString(R.string.forum) -> startActivity(Intent(this, ForumActivity::class.java))
            }
            true
        }

        navController.addOnDestinationChangedListener { _, b, _ ->
            when (b.label) {
                getString(R.string.mainTab) -> {
                    supportActionBar?.show()
                    binding.toolbar.setTitle(R.string.main)
                }
                getString(R.string.journal) -> {
                    supportActionBar?.show()
                    binding.toolbar.setTitle(R.string.journal)
                }
            }
        }
    }
}