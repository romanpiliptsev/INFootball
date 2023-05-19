package com.example.infootball.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.data.network.model.TopscorerDto
import com.example.infootball.databinding.TopscorerItemBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class TopscorersAdapter :
    ListAdapter<TopscorerDto, TopscorersAdapter.TopscorerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopscorerViewHolder {
        val view =
            TopscorerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopscorerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopscorerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TopscorerViewHolder(private val binding: TopscorerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topscorer: TopscorerDto) = binding.let {
            it.topscorerTeam.text = topscorer.team?.name
            it.topscorerName.text = topscorer.player?.name
            it.goals.text = topscorer.goals.toString()
            it.penGoals.text = (topscorer.penalties ?: 0).toString()
            it.assists.text = (topscorer.assists ?: 0).toString()
            it.matches.text = (topscorer.playedMatches ?: 0).toString()
            it.topscorerPosition.text = (currentList.indexOf(topscorer) + 1).toString()

            if (topscorer.team?.crest?.endsWith(".png") == true) {
                Picasso.get().load(topscorer.team?.crest)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(it.teamLogo)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(topscorer.team?.crest), it.teamLogo)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<TopscorerDto>() {
        override fun areItemsTheSame(
            oldItem: TopscorerDto,
            newItem: TopscorerDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TopscorerDto,
            newItem: TopscorerDto
        ): Boolean {
            return oldItem == newItem
        }
    }
}