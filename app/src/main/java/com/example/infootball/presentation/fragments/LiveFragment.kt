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
import com.example.infootball.databinding.FragmentLiveBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.LiveOrLeagueMatchesAdapter
import com.example.infootball.presentation.viewmodels.LiveMatchesViewModel
import com.example.infootball.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class LiveFragment : Fragment(), LiveOrLeagueMatchesAdapter.OnRvItemClickListener {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private val binding by lazy {
        FragmentLiveBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val vm: LiveMatchesViewModel by viewModels {
        viewModelFactory
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var matchesList: List<MatchDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LiveOrLeagueMatchesAdapter(this)
        rvMatches.adapter = adapter
        rvMatches.layoutManager = LinearLayoutManager(requireContext())

        vm.getMatchList()

        vm.getMatchesStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is LiveMatchesViewModel.GetMatchesState.Error -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        noMatches.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is LiveMatchesViewModel.GetMatchesState.Loaded -> {
                    with(binding) {
                        if (it.matches.isEmpty()) {
                            noMatches.visibility = View.VISIBLE
                        } else {
                            rvMatches.visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.matches.let { list ->
                        adapter.submitList(list)
                        matchesList = list
                    }
                }
                is LiveMatchesViewModel.GetMatchesState.Loading -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        noMatches.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getMatchList()
        }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(MatchActivity.newIntent(requireContext(), matchesList[position].id ?: -1))
    }

    companion object {

        @JvmStatic
        fun newInstance() = LiveFragment()
    }
}