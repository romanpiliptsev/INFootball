package com.example.infootball.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.databinding.FragmentLeaguesBinding
import com.example.infootball.presentation.adapters.TopLeaguesAdapter
import com.example.infootball.presentation.viewmodels.LeaguesViewModel

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class LeaguesFragment : Fragment() {

//    private var param1: String? = null
//    private var param2: String? = null

    private val binding by lazy {
        FragmentLeaguesBinding.inflate(layoutInflater)
    }

    private val rvTopLeagues by lazy {
        binding.rvTopLeagues
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm = LeaguesViewModel()

        val adapter = TopLeaguesAdapter()
        rvTopLeagues.adapter = adapter
        rvTopLeagues.layoutManager = LinearLayoutManager(requireContext())

        vm.getLeagueList(arrayListOf(1, 2, 3))

        vm.getLeagueListStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is LeaguesViewModel.GetLeagueListState.Error -> {
                    with(binding) {
                        rvTopLeagues.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is LeaguesViewModel.GetLeagueListState.Loaded -> {
                    with(binding) {
                        rvTopLeagues.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }
                    adapter.submitList(it.leagueList)
                }
                is LeaguesViewModel.GetLeagueListState.Loading -> {
                    with(binding) {
                        rvTopLeagues.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            LeaguesFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}