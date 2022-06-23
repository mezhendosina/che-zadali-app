package com.mezhendosina.sgo.app.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.AnnouncementsFragmentBinding
import com.mezhendosina.sgo.app.factory
import com.mezhendosina.sgo.data.DateManipulation
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

class AnnouncementsFragment : Fragment(R.layout.announcements_fragment) {

    private lateinit var binding: AnnouncementsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AnnouncementsFragmentBinding.bind(view)

        val announcement =
            Singleton.announcements.find { it.id == arguments?.getInt(Singleton.ANNOUNCEMENTS_ID) }
        val markwon = Markwon.builder(requireContext())
            .usePlugin(HtmlPlugin.create())
            .build()



        with(binding) {
            collapsingtoolbarlayout.title = announcement?.name
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            announcement?.description?.let { markwon.setMarkdown(homeworkBody, it) }
            author.text = announcement?.author?.nickName
            date.text =
                "Дата публикации: ${announcement?.postDate?.let { DateManipulation(it).dateToRussianWithTime() }}"
        }
    }
}