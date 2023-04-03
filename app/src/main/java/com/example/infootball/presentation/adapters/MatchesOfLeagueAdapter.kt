package com.example.infootball.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.MatchItemBinding
import com.squareup.picasso.Picasso

class MatchesOfLeagueAdapter :
    ListAdapter<MatchDto, MatchesOfLeagueAdapter.MatchesViewHolder>(DiffCallback()) {

//    private val listener: OnRvItemClickListener = onRvItemClickListener

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view =
            MatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MatchesViewHolder(private val binding: MatchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(match: MatchDto) = with(binding) {
            awayTeamGoals.text = match.score?.fullTime?.away.toString()
            awayTeamName.text = match.awayTeam?.name
            homeTeamGoals.text = match.score?.fullTime?.home.toString()
            homeTeamName.text = match.homeTeam?.name
            status.text = match.status
            timeOfStart.text = match.utcDate

            Picasso.get().load(match.homeTeam?.crest)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(homeTeamLogo)
            Picasso.get().load(match.awayTeam?.crest)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(awayTeamLogo)
//            binding.root.setOnClickListener(this@LeagueViewHolder)
        }

//        override fun onClick(v: View?) {
//            listener.onRvItemClick(adapterPosition)
//        }
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

//    interface OnRvItemClickListener {
//        fun onRvItemClick(position: Int)
//    }
}