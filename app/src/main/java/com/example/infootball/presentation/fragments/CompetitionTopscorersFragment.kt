package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.InfootballApp
import com.example.infootball.data.network.model.TopscorerDto
import com.example.infootball.databinding.FragmentCompetitionTopscorersBinding
import com.example.infootball.presentation.adapters.TopscorersAdapter
import com.example.infootball.presentation.viewmodels.CompetitionTopscorersViewModel
import com.example.infootball.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

private const val ARG_PARAM_LEAGUE_CODE = "param_league_code"
private const val ARG_PARAM_SEASON = "param_season"

class CompetitionTopscorersFragment : Fragment() {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private var paramLeagueCode: String? = null
    private var paramSeason: String? = null

    private val binding by lazy {
        FragmentCompetitionTopscorersBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val vm: CompetitionTopscorersViewModel by viewModels {
        viewModelFactory
    }

    private val rvTopscorers by lazy {
        binding.rvTopscorers
    }

    private lateinit var topscorers: ArrayList<TopscorerDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramLeagueCode = it.getString(ARG_PARAM_LEAGUE_CODE)
            paramSeason = it.getString(ARG_PARAM_SEASON)
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

        val adapter = TopscorersAdapter()
        rvTopscorers.adapter = adapter
        rvTopscorers.layoutManager = LinearLayoutManager(requireContext())

        vm.getTopscorerList(paramLeagueCode ?: "", paramSeason ?: "", 100)

        vm.getTopscorersStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is CompetitionTopscorersViewModel.GetTopscorersState.Error -> {
                    with(binding) {
                        rvTopscorers.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is CompetitionTopscorersViewModel.GetTopscorersState.Loaded -> {
                    with(binding) {
                        rvTopscorers.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.topscorers.let { list ->
                        adapter.submitList(list)
                        topscorers = list
                    }
                }
                is CompetitionTopscorersViewModel.GetTopscorersState.Loading -> {
                    with(binding) {
                        rvTopscorers.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getTopscorerList(paramLeagueCode ?: "", paramSeason ?: "", 100)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(paramLeagueCode: String, paramSeason: String) =
            CompetitionTopscorersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_LEAGUE_CODE, paramLeagueCode)
                    putString(ARG_PARAM_SEASON, paramSeason)
                }
            }
    }
}