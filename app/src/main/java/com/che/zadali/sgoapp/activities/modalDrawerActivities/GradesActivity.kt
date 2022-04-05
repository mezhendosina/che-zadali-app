package com.che.zadali.sgoapp.activities.modalDrawerActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.GradesActivityBinding

class GradesActivity : AppCompatActivity() {
    private lateinit var binding: GradesActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GradesActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}