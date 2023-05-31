package com.example.infootball.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.databinding.LeagueOfMatchesBinding
import com.example.infootball.domain.entities.LeagueOfMatchesEntity
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class LeaguesOfMatchesAdapter(private val onRvItemClickListener: OnRvItemClickListener) :
    ListAdapter<LeagueOfMatchesEntity, LeaguesOfMatchesAdapter.LeagueViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val view =
            LeagueOfMatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeagueViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LeagueViewHolder(private val binding: LeagueOfMatchesBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(leagueOfMatches: LeagueOfMatchesEntity) = with(binding) {
            countryName.text = leagueOfMatches.countryName
            leagueName.text = leagueOfMatches.leagueName
            numberOfMatches.text = leagueOfMatches.numberOfMatches.toString()
            numberOfLiveMatches.text = leagueOfMatches.numberOfLiveMatches.toString()

            leagueOfMatches.leagueLogo.let { link ->
                if (link.endsWith(".png")) {
                    Picasso.get().load(link)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(leagueLogo)
                } else {
                    GlideToVectorYou
                        .init()
                        .with(root.context)
                        .load(Uri.parse(link), leagueLogo)
                }
            }

            if (leagueOfMatches.countryFlag == leagueOfMatches.leagueLogo) {
                Picasso.get().load(R.drawable.world_flag)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(flag)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(leagueOfMatches.countryFlag), flag)
            }
            binding.root.setOnClickListener(this@LeagueViewHolder)
        }

        override fun onClick(v: View?) {
            onRvItemClickListener.onRvItemClick(adapterPosition)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<LeagueOfMatchesEntity>() {
        override fun areItemsTheSame(
            oldItem: LeagueOfMatchesEntity,
            newItem: LeagueOfMatchesEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LeagueOfMatchesEntity,
            newItem: LeagueOfMatchesEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnRvItemClickListener {
        fun onRvItemClick(position: Int)
    }
}