package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.FragmentTeamResultsBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.ResultsAndCalendarAdapter
import com.example.infootball.presentation.viewmodels.TeamResultsViewModel

private const val ARG_PARAM_TEAM_ID = "param_team_id"

class TeamResultsFragment : Fragment(), ResultsAndCalendarAdapter.OnRvItemClickListener {

    private var paramTeamId: Int? = null

    private val binding by lazy {
        FragmentTeamResultsBinding.inflate(layoutInflater)
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var matchesList: List<MatchDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramTeamId = it.getInt(ARG_PARAM_TEAM_ID)
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

        val vm = TeamResultsViewModel(requireActivity().application)

        val adapter = ResultsAndCalendarAdapter(this)
        rvMatches.adapter = adapter
        rvMatches.layoutManager = LinearLayoutManager(requireContext())

        vm.getTeamResults(paramTeamId ?: -1)

        vm.getTeamResultsStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TeamResultsViewModel.GetTeamResultsState.Error -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is TeamResultsViewModel.GetTeamResultsState.Loaded -> {
                    with(binding) {
                        rvMatches.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.matches.let { list ->
                        adapter.submitList(list)
                        matchesList = list
                    }
                }
                is TeamResultsViewModel.GetTeamResultsState.Loading -> {
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
        startActivity(MatchActivity.newIntent(requireContext(), matchesList[position].id ?: -1))
    }

    companion object {

        @JvmStatic
        fun newInstance(paramTeamId: Int) =
            TeamResultsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_TEAM_ID, paramTeamId)
                }
            }
    }
}