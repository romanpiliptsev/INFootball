package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.FragmentCompetitionResultsOrCalendarBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.ResultsAndCalendarAdapter
import com.example.infootball.presentation.viewmodels.CompetitionMatchesViewModel

private const val ARG_PARAM_LEAGUE_CODE = "param_league_code"
private const val ARG_PARAM_SEASON = "param_season"
private const val ARG_PARAM_IS_RESULTS = "param_is_results"

class CompetitionResultsOrCalendarFragment : Fragment(),
    ResultsAndCalendarAdapter.OnRvItemClickListener {

    private var paramLeagueCode: String? = null
    private var paramSeason: String? = null
    private var paramIsResults: Boolean? = null

    private val binding by lazy {
        FragmentCompetitionResultsOrCalendarBinding.inflate(layoutInflater)
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var matches: ArrayList<MatchDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramLeagueCode = it.getString(ARG_PARAM_LEAGUE_CODE)
            paramSeason = it.getString(ARG_PARAM_SEASON)
            paramIsResults = it.getBoolean(ARG_PARAM_IS_RESULTS)
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

        val vm = CompetitionMatchesViewModel(requireActivity().application)

        val adapter = ResultsAndCalendarAdapter(this)
        rvMatches.adapter = adapter
        rvMatches.layoutManager = LinearLayoutManager(requireContext())

        vm.getMatchList(paramLeagueCode ?: "", paramSeason ?: "", paramIsResults ?: true)

        vm.getMatchesStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is CompetitionMatchesViewModel.GetMatchesState.Error -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is CompetitionMatchesViewModel.GetMatchesState.Loaded -> {
                    with(binding) {
                        rvMatches.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.matches.let { list ->
                        adapter.submitList(list)
                        matches = list
                    }
                }
                is CompetitionMatchesViewModel.GetMatchesState.Loading -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(MatchActivity.newIntent(requireContext(), matches[position].id ?: -1))
    }

    companion object {

        @JvmStatic
        fun newInstance(leagueCode: String, season: String, isResults: Boolean) =
            CompetitionResultsOrCalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_LEAGUE_CODE, leagueCode)
                    putString(ARG_PARAM_SEASON, season)
                    putBoolean(ARG_PARAM_IS_RESULTS, isResults)
                }
            }
    }
}