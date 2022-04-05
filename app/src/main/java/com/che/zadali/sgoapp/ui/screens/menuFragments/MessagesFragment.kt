package com.che.zadali.sgoapp.ui.screens.menuFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.data.adapters.MessagesActionListener
import com.che.zadali.sgoapp.data.adapters.MessagesAdapter
import com.che.zadali.sgoapp.data.layout.messageList.Record
import com.che.zadali.sgoapp.databinding.MessagesActivityBinding
import com.che.zadali.sgoapp.ui.factory
import com.che.zadali.sgoapp.ui.navigator
import com.che.zadali.sgoapp.ui.viewModels.MessagesViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.transition.MaterialSharedAxis

class MessagesFragment(private val appBar: CollapsingToolbarLayout) : Fragment() {
    private lateinit var binding: MessagesActivityBinding
    private lateinit var adapter: MessagesAdapter
    private val viewModel: MessagesViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appBar.title = getString(R.string.messages)

        binding = MessagesActivityBinding.inflate(inflater, container, false)
        adapter = MessagesAdapter(object : MessagesActionListener {
            override fun onClick(message: Record) {
                navigator().messageItem(message.messageId)
            }
        })

        viewModel.messages.observe(viewLifecycleOwner) {
            adapter.messages = it
            binding.messagesList.visibility = View.VISIBLE
        }

        viewModel.loaded.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressCircular.visibility = View.GONE
                binding.messagesList.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.VISIBLE
                binding.messagesList.visibility = View.INVISIBLE
            }
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.messagesList.layoutManager = layoutManager
        binding.messagesList.adapter = adapter

        return binding.root
    }
}
