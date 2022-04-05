package com.che.zadali.sgoapp.activities.modalDrawerActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.che.zadali.sgoapp.databinding.ForumAcitvityBinding

class ForumActivity : AppCompatActivity() {
    private lateinit var binding: ForumAcitvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForumAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}