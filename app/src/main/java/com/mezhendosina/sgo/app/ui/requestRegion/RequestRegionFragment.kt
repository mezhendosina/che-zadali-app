package com.mezhendosina.sgo.app.ui.requestRegion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentRequestRegionBinding

class RequestRegionFragment : Fragment(R.layout.fragment_request_region) {

    private lateinit var binding: FragmentRequestRegionBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRequestRegionBinding.bind(view)

    }
}