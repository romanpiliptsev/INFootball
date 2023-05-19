package com.example.infootball.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.data.network.model.PlayerOfTeamDto
import com.example.infootball.databinding.PlayerItemBinding

class PlayersAdapter :
    ListAdapter<PlayerOfTeamDto, PlayersAdapter.PlayerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view =
            PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlayerViewHolder(private val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(player: PlayerOfTeamDto) = binding.let {
            it.playerName.text = player.name
            it.playerPosition.text = "(${player.position})"
            it.playerCountryAndBirthday.text = "${player.nationality}, ${player.dateOfBirth}"
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PlayerOfTeamDto>() {
        override fun areItemsTheSame(
            oldItem: PlayerOfTeamDto,
            newItem: PlayerOfTeamDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PlayerOfTeamDto,
            newItem: PlayerOfTeamDto
        ): Boolean {
            return oldItem == newItem
        }
    }
}