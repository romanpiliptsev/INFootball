package com.example.infootball.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.infootball.databinding.FragmentLeaguesBinding

class LeaguesFragment : Fragment() {

    private val binding by lazy {
        FragmentLeaguesBinding.inflate(layoutInflater)
    }

//    private val rvTopLeagues by lazy {
//        binding.rvTopLeagues
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val vm = MatchesOfLeagueViewModel()
//
//        val adapter = TopLeaguesAdapter()
//        rvTopLeagues.adapter = adapter
//        rvTopLeagues.layoutManager = LinearLayoutManager(requireContext())
//
//        vm.getLeagueList(arrayListOf(1, 2, 3))
//
//        vm.getLeagueListStateLiveData.observe(viewLifecycleOwner) {
//            when (it) {
//                is MatchesOfLeagueViewModel.GetLeagueListState.Error -> {
//                    with(binding) {
//                        rvTopLeagues.visibility = View.GONE
//                        progressBar.visibility = View.GONE
//                        errorGroup.visibility = View.VISIBLE
//                    }
//                }
//                is MatchesOfLeagueViewModel.GetLeagueListState.Loaded -> {
//                    with(binding) {
//                        rvTopLeagues.visibility = View.VISIBLE
//                        progressBar.visibility = View.GONE
//                        errorGroup.visibility = View.GONE
//                    }
//                    adapter.submitList(it.leagueList)
//                }
//                is MatchesOfLeagueViewModel.GetLeagueListState.Loading -> {
//                    with(binding) {
//                        rvTopLeagues.visibility = View.GONE
//                        progressBar.visibility = View.VISIBLE
//                        errorGroup.visibility = View.GONE
//                    }
//                }
//            }
//        }
//    }

    companion object {

        @JvmStatic
        fun newInstance() = LeaguesFragment()
    }
}