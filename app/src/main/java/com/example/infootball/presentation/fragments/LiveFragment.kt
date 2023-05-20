package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.FragmentLiveBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.LiveOrLeagueMatchesAdapter
import com.example.infootball.presentation.viewmodels.LiveMatchesViewModel

class LiveFragment : Fragment(), LiveOrLeagueMatchesAdapter.OnRvItemClickListener {

    private val binding by lazy {
        FragmentLiveBinding.inflate(layoutInflater)
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var matchesList: List<MatchDto>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm = ViewModelProvider(this)[LiveMatchesViewModel::class.java]

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