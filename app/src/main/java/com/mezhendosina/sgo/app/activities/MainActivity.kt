package com.mezhendosina.sgo.app.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mezhendosina.sgo.app.databinding.ContainerMainActivityBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContainerMainActivityBinding
    private var navController: NavController? = null

    private val viewModel: MainViewModel by viewModels()

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            if (f.findNavController() != navController) navController = f.findNavController()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {

            withContext(Dispatchers.Main) {
                viewModel.errorMessage.observe(this@MainActivity) {
                    errorDialog(this@MainActivity, it)
                }
            }
            viewModel.login(this@MainActivity)

            withContext(Dispatchers.Main) {
                binding = ContainerMainActivityBinding.inflate(layoutInflater)
                setContentView(binding.root)
            }
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onRestart() {
        runBlocking {
            viewModel.login(this@MainActivity)
        }
        super.onRestart()
    }

    override fun onStop() {
        viewModel.logout()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }


    override fun onBackPressed() {
        if (navController?.currentDestination?.id != navController?.graph?.startDestinationId) navController?.navigateUp()
        else super.onBackPressed()
    }
}
