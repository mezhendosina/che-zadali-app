package com.che.zadali.sgoapp.ui.screens.menuFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.che.zadali.sgoapp.databinding.MessageItemFragmentBinding
import com.che.zadali.sgoapp.ui.factory
import com.che.zadali.sgoapp.ui.viewModels.MessagesItemViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.transition.MaterialFadeThrough


class MessageItemFragment(private val appBar: CollapsingToolbarLayout) :
    Fragment() {
    private lateinit var binding: MessageItemFragmentBinding
    private val viewModel: MessagesItemViewModel by viewModels { factory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadMessage(requireArguments().getInt(ARG_MESSAGE_ID))
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MessageItemFragmentBinding.inflate(inflater, container, false)

        fun hideAttachments() {
            binding.attachments.visibility = View.GONE
            binding.attachmentsDivider.visibility = View.GONE
            binding.attachmentsRecyclerView.visibility = View.GONE
        }

        viewModel.message.observe(viewLifecycleOwner) {
            appBar.title = ""
            binding.header.text = it.subj
            binding.text.text =
                "Здесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\nЗдесь может быть текст вашего сообения\n"
            binding.sent.text =
                "Отправитель: ${it.fromName}\nПолучатель: ${it.sentTo}\nОтправлено: ${it.sent}"
            hideAttachments()

        }

        return binding.root
    }

    companion object {
        private const val ARG_MESSAGE_ID = "ARG_MESSAGE_ID"
        fun newInstance(id: Int, supportActionBar: CollapsingToolbarLayout): Fragment {
            val fragment = MessageItemFragment(supportActionBar)
            fragment.arguments = bundleOf(ARG_MESSAGE_ID to id)
            return fragment
        }
    }
}