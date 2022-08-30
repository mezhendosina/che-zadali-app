package com.mezhendosina.sgo.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.LoadStates
import androidx.recyclerview.widget.RecyclerView
import com.mezhendosina.sgo.app.databinding.JournalLoadingStateBinding

class LoadingStateAdapter : LoadStateAdapter<LoadingStateAdapter.LoadingStateHolder>() {

    class LoadingStateHolder(private val binding: JournalLoadingStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) = with(binding) {
            println(loadState)
            somethingWentWrong.isVisible = loadState is LoadState.Error
            tryAgainButton.isVisible = loadState is LoadState.Error

            progressBar.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadingStateHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JournalLoadingStateBinding.inflate(inflater, parent, false)

        return LoadingStateHolder(binding)
    }
}