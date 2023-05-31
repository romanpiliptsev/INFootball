package com.example.infootball.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.data.network.model.MatchDtoForH2H
import com.example.infootball.databinding.H2hMatchItemBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class H2HMatchesAdapter(private val onRvItemClickListener: OnRvItemClickListener) :
    ListAdapter<MatchDtoForH2H, H2HMatchesAdapter.MatchesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view =
            H2hMatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MatchesViewHolder(private val binding: H2hMatchItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(match: MatchDtoForH2H) = with(binding) {

            awayTeamGoals.text = match.score?.fullTime?.away.toString()
            homeTeamGoals.text = match.score?.fullTime?.home.toString()

            awayTeamName.text = match.awayTeam?.name
            homeTeamName.text = match.homeTeam?.name
            date.text = match.utcDate?.slice(0..9)

            if (match.competition?.emblem?.endsWith(".png") == true) {
                Picasso.get().load(match.competition?.emblem)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(leagueLogo)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(match.competition?.emblem), leagueLogo)
            }

            if (match.homeTeam?.crest?.endsWith(".png") == true) {
                Picasso.get().load(match.homeTeam?.crest)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(homeTeamLogo)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(match.homeTeam?.crest), homeTeamLogo)
            }

            if (match.awayTeam?.crest?.endsWith(".png") == true) {
                Picasso.get().load(match.awayTeam?.crest)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(awayTeamLogo)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(match.awayTeam?.crest), awayTeamLogo)
            }
            binding.root.setOnClickListener(this@MatchesViewHolder)
        }

        override fun onClick(v: View?) {
            onRvItemClickListener.onRvItemClick(adapterPosition)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<MatchDtoForH2H>() {
        override fun areItemsTheSame(
            oldItem: MatchDtoForH2H,
            newItem: MatchDtoForH2H
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MatchDtoForH2H,
            newItem: MatchDtoForH2H
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnRvItemClickListener {
        fun onRvItemClick(position: Int)
    }
}