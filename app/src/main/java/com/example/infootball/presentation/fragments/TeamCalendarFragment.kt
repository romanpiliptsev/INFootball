package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.InfootballApp
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.FragmentTeamCalendarBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.ResultsAndCalendarAdapter
import com.example.infootball.presentation.viewmodels.TeamCalendarViewModel
import com.example.infootball.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

private const val ARG_PARAM_TEAM_ID = "param_team_id"

class TeamCalendarFragment : Fragment(), ResultsAndCalendarAdapter.OnRvItemClickListener {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private var paramTeamId: Int? = null

    private val binding by lazy {
        FragmentTeamCalendarBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val vm: TeamCalendarViewModel by viewModels {
        viewModelFactory
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var matchesList: List<MatchDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
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

        val adapter = ResultsAndCalendarAdapter(this)
        rvMatches.adapter = adapter
        rvMatches.layoutManager = LinearLayoutManager(requireContext())

        vm.getTeamCalendar(paramTeamId ?: -1)

        vm.getTeamCalendarStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TeamCalendarViewModel.GetTeamCalendarState.Error -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is TeamCalendarViewModel.GetTeamCalendarState.Loaded -> {
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
                is TeamCalendarViewModel.GetTeamCalendarState.Loading -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getTeamCalendar(paramTeamId ?: -1)
        }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(MatchActivity.newIntent(requireContext(), matchesList[position].id ?: -1))
    }

    companion object {

        @JvmStatic
        fun newInstance(paramTeamId: Int) =
            TeamCalendarFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_TEAM_ID, paramTeamId)
                }
            }
    }
}