package com.example.infootball.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.R
import com.example.infootball.data.network.model.StandingsResponseDto
import com.example.infootball.databinding.FragmentTableBinding
import com.example.infootball.presentation.activities.TeamActivity
import com.example.infootball.presentation.adapters.TableAdapter
import com.example.infootball.presentation.viewmodels.CompetitionViewModel
import com.example.infootball.presentation.viewmodels.StandingsViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

private const val ARG_PARAM_LEAGUE_CODE = "param_league_code"
private const val ARG_PARAM_SEASON = "param_season"

class TableFragment : Fragment(), TableAdapter.OnRvItemClickListener {

    private var paramLeagueCode: String? = null
    private var paramSeason: String? = null

    private val binding by lazy {
        FragmentTableBinding.inflate(layoutInflater)
    }

    private val rvTableItems by lazy {
        binding.rvTableItems
    }

    private lateinit var standings: StandingsResponseDto

    private var currentStandings: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
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

        val vm = ViewModelProvider(this)[StandingsViewModel::class.java]

        val adapter = TableAdapter(this)
        rvTableItems.adapter = adapter
        rvTableItems.layoutManager = LinearLayoutManager(requireContext())

        vm.getMatchStandings(paramLeagueCode ?: "", paramSeason ?: "")

        vm.getStandingsStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is StandingsViewModel.GetStandingsState.Error -> {
                    with(binding) {
                        okGroup.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is StandingsViewModel.GetStandingsState.Loaded -> {
                    with(binding) {
                        okGroup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.standings.let { newStandings ->
                        standings = newStandings
                        adapter.submitList(newStandings.standings[0].table)
                    }

                    binding.leagueName.text = standings.competition?.name

                    standings.competition?.emblem.let { link ->
                        if (link?.endsWith(".png") == true) {
                            Picasso.get().load(link)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(binding.leagueLogo)
                        } else {
                            GlideToVectorYou
                                .init()
                                .with(context)
                                .load(Uri.parse(link), binding.leagueLogo)
                        }
                    }
                    GlideToVectorYou
                        .init()
                        .with(context)
                        .load(Uri.parse(standings.area?.flag), binding.flag)

                    if (standings.competition?.emblem == standings.area?.flag) {
                        binding.flag.visibility = View.GONE
                    }

                    if (standings.competition?.type == "LEAGUE" || standings.standings.size == 1) {
                        binding.spinner.visibility = View.GONE
                    } else {
                        val groups =
                            standings.standings.map { table -> table.group?.replace("_", " ") }
                        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            groups
                        )
                        // Определяем разметку для использования при выборе элемента
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner.adapter = spinnerAdapter

                        val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                adapter.submitList(standings.standings[position].table)
                                currentStandings = position
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                        binding.spinner.onItemSelectedListener = itemSelectedListener
                    }
                }
                is StandingsViewModel.GetStandingsState.Loading -> {
                    with(binding) {
                        okGroup.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
            binding.retryButton.setOnClickListener {
                vm.getMatchStandings(paramLeagueCode ?: "", paramSeason ?: "")
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(paramLeagueCode: String, paramSeason: String = "") =
            TableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_LEAGUE_CODE, paramLeagueCode)
                    putString(ARG_PARAM_SEASON, paramSeason)
                }
            }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(
            TeamActivity.newIntent(
                requireContext(),
                standings.standings[currentStandings].table[position].team?.id ?: -1
            )
        )
    }
}