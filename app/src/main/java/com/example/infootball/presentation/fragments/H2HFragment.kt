package com.example.infootball.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.R
import com.example.infootball.data.network.model.H2HDto
import com.example.infootball.databinding.FragmentH2HBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.H2HMatchesAdapter
import com.example.infootball.presentation.viewmodels.CompetitionViewModel
import com.example.infootball.presentation.viewmodels.H2HViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

private const val ARG_PARAM_MATCH_ID = "param_match_id"

class H2HFragment : Fragment(), H2HMatchesAdapter.OnRvItemClickListener {

    private var paramMatchId: Int? = null

    private val binding by lazy {
        FragmentH2HBinding.inflate(layoutInflater)
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var h2h: H2HDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramMatchId = it.getInt(ARG_PARAM_MATCH_ID, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm = ViewModelProvider(this)[H2HViewModel::class.java]

        val adapter = H2HMatchesAdapter(this)
        rvMatches.adapter = adapter
        rvMatches.layoutManager = LinearLayoutManager(requireContext())

        vm.getH2H(paramMatchId ?: -1)

        vm.getH2HStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is H2HViewModel.GetH2HState.Error -> {
                    with(binding) {
                        okGroup.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is H2HViewModel.GetH2HState.Loaded -> {
                    it.h2h.let { itH2H ->
                        adapter.submitList(itH2H.matches)
                        h2h = itH2H
                    }
                    with(binding) {
                        okGroup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE

                        last15.text = "LAST ${h2h.aggregates?.numberOfMatches} MATCHES STATISTICS"

                        h2h.aggregates?.homeTeam?.wins.let { wins ->
                            if (wins != null) {
                                if (wins != 1) homeTeamWins.text = "$wins wins"
                                else homeTeamWins.text = "$wins win"
                            }
                        }
                        h2h.aggregates?.awayTeam?.wins.let { wins ->
                            if (wins != null) {
                                if (wins != 1) awayTeamWins.text = "$wins wins"
                                else awayTeamWins.text = "$wins win"
                            }
                        }
                        h2h.aggregates?.awayTeam?.draws.let { drawsNumber ->
                            if (drawsNumber != null) {
                                if (drawsNumber != 1) draws.text = "$drawsNumber draws"
                                else draws.text = "$drawsNumber draw"
                            }
                        }

                        if (h2h.matches.isNotEmpty()) {
                            // TODO: Исправить логику вывода эмблемы клуба
                            h2h.matches[0].homeTeam?.crest.let { link ->
                                if (link?.endsWith(".png") == true) {
                                    Picasso.get().load(link)
                                        .placeholder(R.drawable.ic_launcher_foreground)
                                        .into(homeTeamLogo)
                                } else {
                                    GlideToVectorYou
                                        .init()
                                        .with(binding.root.context)
                                        .load(Uri.parse(link), homeTeamLogo)
                                }
                            }
                            h2h.matches[0].awayTeam?.crest.let { link ->
                                if (link?.endsWith(".png") == true) {
                                    Picasso.get().load(link)
                                        .placeholder(R.drawable.ic_launcher_foreground)
                                        .into(awayTeamLogo)
                                } else {
                                    GlideToVectorYou
                                        .init()
                                        .with(binding.root.context)
                                        .load(Uri.parse(link), awayTeamLogo)
                                }
                            }
                        }
                    }
                }
                is H2HViewModel.GetH2HState.Loading -> {
                    with(binding) {
                        okGroup.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getH2H(paramMatchId ?: -1)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(matchId: Int) =
            H2HFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_MATCH_ID, matchId)
                }
            }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(MatchActivity.newIntent(requireContext(), h2h.matches[position].id ?: -1))
    }
}