package com.example.infootball.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.infootball.R
import com.example.infootball.databinding.FragmentCompetitionBinding
import com.example.infootball.presentation.viewmodels.CompetitionViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

private const val ARG_PARAM_COMPETITION_CODE = "param_competition_code"

class CompetitionFragment : Fragment() {

    private var paramCompetitionCode: String? = null

    private val binding by lazy {
        FragmentCompetitionBinding.inflate(layoutInflater)
    }

    private var season: String = "2022"
    private var currentFragment: String = TABLE_FRAGMENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramCompetitionCode = it.getString(ARG_PARAM_COMPETITION_CODE)
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

        val vm = CompetitionViewModel(requireActivity().application)

        vm.getCompetition(paramCompetitionCode ?: "")

        vm.getCompetitionStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is CompetitionViewModel.GetCompetitionState.Error -> {
                    with(binding) {
                        loadedGroup.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is CompetitionViewModel.GetCompetitionState.Loaded -> {
                    with(binding) {
                        loadedGroup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE

                        it.competition.emblem.let { emblemLink ->
                            if (emblemLink?.endsWith(".png") == true) {
                                Picasso.get().load(emblemLink)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(leagueLogo)
                            } else {
                                GlideToVectorYou
                                    .init()
                                    .with(context)
                                    .load(Uri.parse(emblemLink), leagueLogo)
                            }
                        }
                        it.competition.area?.flag.let { flagLink ->
                            if (flagLink?.endsWith(".png") == true) {
                                Picasso.get().load(flagLink)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(flag)
                            } else {
                                GlideToVectorYou
                                    .init()
                                    .with(context)
                                    .load(Uri.parse(flagLink), flag)
                            }
                        }

                        countryName.text = it.competition.area?.name
                        leagueName.text = it.competition.name

                        if (it.competition.type == "CUP") {
                            season = ""
                            spinnerSeasons.visibility = View.INVISIBLE
                        } else {
                            val seasons = listOf("2022/23", "2021/22", "2020/21")
                            val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                seasons
                            )
                            // Определяем разметку для использования при выборе элемента
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerSeasons.adapter = spinnerAdapter

                            val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    season = seasons[position].slice(0..3)
                                    when (currentFragment) {
                                        TABLE_FRAGMENT -> table.performClick()
                                        RESULTS_FRAGMENT -> results.performClick()
                                        CALENDAR_FRAGMENT -> calendar.performClick()
                                        TOPSCORERS_FRAGMENT -> topscorers.performClick()
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                            spinnerSeasons.onItemSelectedListener = itemSelectedListener
                        }

                        table.setOnClickListener {
                            launchTableFragment(
                                paramCompetitionCode ?: throw RuntimeException(),
                                season
                            )
                            setColorsToFragmentSwitchers(table)
                            currentFragment = TABLE_FRAGMENT
                        }

                        results.setOnClickListener {
                            launchResultsOrCalendarFragment(
                                paramCompetitionCode ?: throw RuntimeException(),
                                season,
                                true
                            )
                            setColorsToFragmentSwitchers(results)
                            currentFragment = RESULTS_FRAGMENT
                        }

                        calendar.setOnClickListener {
                            launchResultsOrCalendarFragment(
                                paramCompetitionCode ?: throw RuntimeException(),
                                season,
                                false
                            )
                            setColorsToFragmentSwitchers(calendar)
                            currentFragment = CALENDAR_FRAGMENT
                        }

                        topscorers.setOnClickListener {
                            launchTopscorersFragment(
                                paramCompetitionCode ?: throw RuntimeException(),
                                season
                            )
                            setColorsToFragmentSwitchers(topscorers)
                            currentFragment = TOPSCORERS_FRAGMENT
                        }

                        table.performClick()
                    }
                }
                is CompetitionViewModel.GetCompetitionState.Loading -> {
                    with(binding) {
                        loadedGroup.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setColorsToFragmentSwitchers(tv: TextView) {
        arrayListOf(binding.calendar, binding.results, binding.table, binding.topscorers).forEach {
            it.setBackgroundColor(
                resources.getColor(
                    when (it) {
                        tv -> R.color.red
                        else -> R.color.white_or_black
                    },
                    requireActivity().theme
                )
            )
        }
    }

    private fun launchTableFragment(leagueCode: String, season: String) {
        childFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                TableFragment.newInstance(leagueCode, season)
            )
            .commit()
    }

    private fun launchResultsOrCalendarFragment(
        leagueCode: String,
        season: String,
        isResultsFragment: Boolean
    ) {
        childFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                CompetitionResultsOrCalendarFragment.newInstance(
                    leagueCode,
                    season,
                    isResultsFragment
                )
            )
            .commit()
    }

    private fun launchTopscorersFragment(
        leagueCode: String,
        season: String
    ) {
        childFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                CompetitionTopscorersFragment.newInstance(
                    leagueCode,
                    season
                )
            )
            .commit()
    }

    companion object {

        @JvmStatic
        fun newInstance(competitionCode: String) =
            CompetitionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_COMPETITION_CODE, competitionCode)
                }
            }

        private const val TABLE_FRAGMENT = "table"
        private const val RESULTS_FRAGMENT = "results"
        private const val CALENDAR_FRAGMENT = "calendar"
        private const val TOPSCORERS_FRAGMENT = "topscorers"
    }
}