package com.example.infootball.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.InfootballApp
import com.example.infootball.R
import com.example.infootball.data.network.model.H2HDto
import com.example.infootball.databinding.FragmentH2HBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.H2HMatchesAdapter
import com.example.infootball.presentation.viewmodels.H2HViewModel
import com.example.infootball.presentation.viewmodels.ViewModelFactory
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso
import javax.inject.Inject

private const val ARG_PARAM_MATCH_ID = "param_match_id"
private const val ARG_PARAM_HOME_TEAM_EMBLEM_LINK = "param_home_team_emblem_link"
private const val ARG_PARAM_AWAY_TEAM_EMBLEM_LINK = "param_away_team_emblem_link"

class H2HFragment : Fragment(), H2HMatchesAdapter.OnRvItemClickListener {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private var paramMatchId: Int? = null
    private var paramHomeTeamEmblemLink: String? = null
    private var paramAwayTeamEmblemLink: String? = null

    private val binding by lazy {
        FragmentH2HBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val vm: H2HViewModel by viewModels {
        viewModelFactory
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var h2h: H2HDto

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramMatchId = it.getInt(ARG_PARAM_MATCH_ID, -1)
            paramHomeTeamEmblemLink = it.getString(ARG_PARAM_HOME_TEAM_EMBLEM_LINK)
            paramAwayTeamEmblemLink = it.getString(ARG_PARAM_AWAY_TEAM_EMBLEM_LINK)
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

                        last15.text =
                            "LAST ${h2h.aggregates?.numberOfMatches ?: "0"} MATCHES STATISTICS"

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

                        paramHomeTeamEmblemLink.let { link ->
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
                        paramAwayTeamEmblemLink.let { link ->
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
        fun newInstance(matchId: Int, homeTeamEmblemLink: String, awayTeamEmblemLink: String) =
            H2HFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_MATCH_ID, matchId)
                    putString(ARG_PARAM_HOME_TEAM_EMBLEM_LINK, homeTeamEmblemLink)
                    putString(ARG_PARAM_AWAY_TEAM_EMBLEM_LINK, awayTeamEmblemLink)
                }
            }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(MatchActivity.newIntent(requireContext(), h2h.matches[position].id ?: -1))
    }
}