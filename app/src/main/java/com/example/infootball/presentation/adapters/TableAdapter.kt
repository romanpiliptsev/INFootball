package com.example.infootball.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infootball.R
import com.example.infootball.data.network.model.TablePositionDto
import com.example.infootball.databinding.TableItemBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class TableAdapter(private val onRvItemClickListener: OnRvItemClickListener) :
    ListAdapter<TablePositionDto, TableAdapter.TableViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view =
            TableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TableViewHolder(private val binding: TableItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(tablePosition: TablePositionDto) = with(binding) {

            position.text = tablePosition.position.toString()
            wins.text = tablePosition.won.toString()
            draws.text = tablePosition.draw.toString()
            defeat.text = tablePosition.lost.toString()
            difference.text = tablePosition.goalDifference.toString()
            games.text = tablePosition.playedGames.toString()
            points.text = tablePosition.points.toString()
            name.text = tablePosition.team?.shortName

            if (tablePosition.team?.crest?.endsWith(".png") == true) {
                Picasso.get().load(tablePosition.team?.crest ?: "")
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(logo)
            } else {
                GlideToVectorYou
                    .init()
                    .with(binding.root.context)
                    .load(Uri.parse(tablePosition.team?.crest ?: ""), logo)
            }
            binding.root.setOnClickListener(this@TableViewHolder)
        }

        override fun onClick(v: View?) {
            onRvItemClickListener.onRvItemClick(adapterPosition)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<TablePositionDto>() {
        override fun areItemsTheSame(
            oldItem: TablePositionDto,
            newItem: TablePositionDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TablePositionDto,
            newItem: TablePositionDto
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnRvItemClickListener {
        fun onRvItemClick(position: Int)
    }
}