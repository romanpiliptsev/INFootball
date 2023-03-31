package com.example.infootball.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.data.network.model.LeagueResponseDto
import com.example.infootball.databinding.LeagueBinding
import com.squareup.picasso.Picasso

class TopLeaguesAdapter :
    ListAdapter<LeagueResponseDto, TopLeaguesAdapter.LeagueViewHolder>(DiffCallback()) {

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val view = LeagueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeagueViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LeagueViewHolder(private val binding: LeagueBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leagueResponse: LeagueResponseDto) = with(binding) {
            countryName.text = leagueResponse.league?.name
            leagueName.text = leagueResponse.country?.name
            Picasso.get().load(leagueResponse.league?.logo)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(leagueLogo)
            Picasso.get().load(leagueResponse.country?.flag)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(flag)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<LeagueResponseDto>() {
        override fun areItemsTheSame(oldItem: LeagueResponseDto, newItem: LeagueResponseDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LeagueResponseDto, newItem: LeagueResponseDto): Boolean {
            return oldItem == newItem
        }
    }
}