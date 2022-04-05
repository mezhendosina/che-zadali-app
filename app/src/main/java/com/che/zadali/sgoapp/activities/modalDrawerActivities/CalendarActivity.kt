package com.che.zadali.sgoapp.activities.modalDrawerActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.databinding.CalendarActivityBinding

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: CalendarActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}