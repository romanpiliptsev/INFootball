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
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.FragmentMatchesOfLeagueBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.LiveOrLeagueMatchesAdapter
import com.example.infootball.presentation.viewmodels.MatchesOfLeagueViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

private const val ARG_COMPETITION = "competition"
private const val ARG_DATE = "date"

class MatchesOfLeagueFragment : Fragment(), LiveOrLeagueMatchesAdapter.OnRvItemClickListener {

    private var paramCompetition: String? = null
    private var paramDate: String? = null

    private val binding by lazy {
        FragmentMatchesOfLeagueBinding.inflate(layoutInflater)
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var matchesList: List<MatchDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramCompetition = it.getString(ARG_COMPETITION)
            paramDate = it.getString(ARG_DATE)
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

        val vm = ViewModelProvider(this)[MatchesOfLeagueViewModel::class.java]

        val adapter = LiveOrLeagueMatchesAdapter(this)
        rvMatches.adapter = adapter
        rvMatches.layoutManager = LinearLayoutManager(requireContext())

        vm.getMatchList(paramCompetition ?: "", paramDate ?: "")

        vm.getMatchesStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MatchesOfLeagueViewModel.GetMatchesState.Error -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is MatchesOfLeagueViewModel.GetMatchesState.Loaded -> {
                    with(binding) {
                        rvMatches.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.matches.let { list ->
                        adapter.submitList(list)
                        matchesList = list
                    }

                    val matchFromList = matchesList[0]
                    binding.date.text = matchFromList.utcDate?.slice(0..9)
                    binding.leagueName.text = matchFromList.competition?.name
                    binding.country.text = matchFromList.area?.name

                    matchFromList.competition?.emblem.let { emblemLink ->
                        if (emblemLink?.endsWith(".png") == true) {
                            Picasso.get().load(emblemLink)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(binding.leagueLogo)
                        } else {
                            GlideToVectorYou
                                .init()
                                .with(context)
                                .load(Uri.parse(emblemLink), binding.leagueLogo)
                        }
                    }
                    GlideToVectorYou
                        .init()
                        .with(context)
                        .load(Uri.parse(matchFromList.area?.flag), binding.flag)
                }
                is MatchesOfLeagueViewModel.GetMatchesState.Loading -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getMatchList(paramCompetition ?: "", paramDate ?: "")
        }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(MatchActivity.newIntent(requireContext(), matchesList[position].id ?: -1))
    }

    companion object {

        @JvmStatic
        fun newInstance(competition: String, date: String) =
            MatchesOfLeagueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COMPETITION, competition)
                    putString(ARG_DATE, date)
                }
            }
    }
}