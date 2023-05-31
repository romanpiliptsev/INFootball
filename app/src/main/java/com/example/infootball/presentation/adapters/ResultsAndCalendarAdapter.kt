package com.example.infootball.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.MatchItemBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class ResultsAndCalendarAdapter(private val onRvItemClickListener: OnRvItemClickListener) :
    ListAdapter<MatchDto, ResultsAndCalendarAdapter.MatchesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view =
            MatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MatchesViewHolder(private val binding: MatchItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(match: MatchDto) = with(binding) {
            val awayTeamGoalsString = match.score?.fullTime?.away.toString()
            if (awayTeamGoalsString == "null") {
                awayTeamGoals.visibility = View.GONE
            } else {
                awayTeamGoals.text = awayTeamGoalsString
            }

            val homeTeamGoalsString = match.score?.fullTime?.home.toString()
            if (homeTeamGoalsString == "null") {
                homeTeamGoals.visibility = View.GONE
            } else {
                homeTeamGoals.text = homeTeamGoalsString
            }
            awayTeamName.text = match.awayTeam?.name
            homeTeamName.text = match.homeTeam?.name
            status.text = match.status?.replace("_", " ")
            timeOfStart.text = match.utcDate?.slice(0..9)

            if (match.homeTeam?.crest?.endsWith(".png") == true) {
                Picasso.get().load(match.homeTeam?.crest)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(homeTeamLogo)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(match.homeTeam?.crest ?: ""), homeTeamLogo)
            }
            if (match.awayTeam?.crest?.endsWith(".png") == true) {
                Picasso.get().load(match.awayTeam?.crest)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(awayTeamLogo)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(match.awayTeam?.crest ?: ""), awayTeamLogo)
            }
            binding.root.setOnClickListener(this@MatchesViewHolder)

            binding.root
        }

        override fun onClick(v: View?) {
            onRvItemClickListener.onRvItemClick(adapterPosition)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<MatchDto>() {
        override fun areItemsTheSame(
            oldItem: MatchDto,
            newItem: MatchDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MatchDto,
            newItem: MatchDto
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnRvItemClickListener {
        fun onRvItemClick(position: Int)
    }
}