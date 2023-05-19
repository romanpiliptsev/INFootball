package com.example.infootball.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.data.network.model.CompetitionWithAreaDto
import com.example.infootball.databinding.LeagueBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class CompetitionsAdapter(onRvItemClickListener: OnRvItemClickListener) :
    ListAdapter<CompetitionWithAreaDto, CompetitionsAdapter.CompetitionViewHolder>(DiffCallback()) {

    private val listener: OnRvItemClickListener = onRvItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitionViewHolder {
        val view =
            LeagueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompetitionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompetitionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CompetitionViewHolder(private val binding: LeagueBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(competition: CompetitionWithAreaDto) = with(binding) {
            countryName.text = competition.area?.name
            leagueName.text = competition.name

            competition.emblem.let { link ->
                if (link?.endsWith(".png") == true) {
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

            competition.area?.flag?.let { link ->
                if (link.endsWith(".png")) {
                    Picasso.get().load(link)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(flag)
                } else {
                    GlideToVectorYou
                        .init()
                        .with(root.context)
                        .load(Uri.parse(link), flag)
                }
            }
//            if (competition.area?.flag == competition.emblem) {
//                Picasso.get().load(R.drawable.world_flag)
//                    .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(flag)
//            } else {
//                GlideToVectorYou
//                    .init()
//                    .with(binding.root.context)
//                    .load(Uri.parse(competition.area?.flag), flag)
//            }
            binding.root.setOnClickListener(this@CompetitionViewHolder)
        }

        override fun onClick(v: View?) {
            listener.onRvItemClick(adapterPosition)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CompetitionWithAreaDto>() {
        override fun areItemsTheSame(
            oldItem: CompetitionWithAreaDto,
            newItem: CompetitionWithAreaDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CompetitionWithAreaDto,
            newItem: CompetitionWithAreaDto
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnRvItemClickListener {
        fun onRvItemClick(position: Int)
    }
}