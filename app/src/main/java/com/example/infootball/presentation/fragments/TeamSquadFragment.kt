package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.InfootballApp
import com.example.infootball.data.network.model.PlayerOfTeamDto
import com.example.infootball.databinding.FragmentTeamSquadBinding
import com.example.infootball.presentation.adapters.PlayersAdapter

private const val ARG_PARAM_PLAYERS = "param_players"

class TeamSquadFragment : Fragment() {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private var paramPlayers: ArrayList<PlayerOfTeamDto>? = null

    private val binding by lazy {
        FragmentTeamSquadBinding.inflate(layoutInflater)
    }

    private val rvPlayers by lazy {
        binding.rvPlayers
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramPlayers = it.getParcelableArrayList(ARG_PARAM_PLAYERS)
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

        val adapter = PlayersAdapter()
        rvPlayers.adapter = adapter
        rvPlayers.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(paramPlayers)
    }

    companion object {

        @JvmStatic
        fun newInstance(matchesArr: ArrayList<PlayerOfTeamDto>) =
            TeamSquadFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM_PLAYERS, matchesArr)
                }
            }
    }
}