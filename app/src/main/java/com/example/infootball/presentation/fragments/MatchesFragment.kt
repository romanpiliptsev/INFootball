package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.R
import com.example.infootball.databinding.FragmentMatchesBinding
import com.example.infootball.domain.entities.LeagueOfMatchesEntity
import com.example.infootball.presentation.activities.MainActivity
import com.example.infootball.presentation.adapters.LeaguesOfMatchesAdapter
import com.example.infootball.presentation.viewmodels.CompetitionViewModel
import com.example.infootball.presentation.viewmodels.LeaguesOfMatchesViewModel
import java.time.LocalDate

class MatchesFragment : Fragment(), LeaguesOfMatchesAdapter.OnRvItemClickListener {

    private val binding by lazy {
        FragmentMatchesBinding.inflate(layoutInflater)
    }

    private val rvLeagues by lazy {
        binding.rvLeagues
    }

    private lateinit var leagueList: List<LeagueOfMatchesEntity>
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm = ViewModelProvider(this)[LeaguesOfMatchesViewModel::class.java]

        date = LocalDate.now().toString()
        binding.today.text = LocalDate.now().toString()
        binding.todayMinus2.text = LocalDate.now().plusDays(-2).toString()
        binding.todayMinus1.text = LocalDate.now().plusDays(-1).toString()
        binding.todayPlus1.text = LocalDate.now().plusDays(1).toString()
        binding.todayPlus2.text = LocalDate.now().plusDays(2).toString()

        val daysArr = arrayListOf(
            binding.today, binding.todayMinus2, binding.todayMinus1,
            binding.todayPlus1, binding.todayPlus2
        )
        daysArr.forEach { tv ->
            tv.setOnClickListener { textView ->
                date = tv.text as String
                vm.getLeagueList(LocalDate.parse(tv.text))
                daysArr.forEach {
                    if (it == textView) {
                        it.setBackgroundColor(
                            resources.getColor(
                                R.color.red,
                                requireActivity().theme
                            )
                        )
                    } else {
                        it.setBackgroundColor(
                            resources.getColor(
                                R.color.white_or_black,
                                requireActivity().theme
                            )
                        )
                    }
                }
            }
        }

        val adapter = LeaguesOfMatchesAdapter(this)
        rvLeagues.adapter = adapter
        rvLeagues.layoutManager = LinearLayoutManager(requireContext())

        vm.getLeagueList(LocalDate.parse(binding.today.text))

        vm.getLeaguesOfMatchesStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is LeaguesOfMatchesViewModel.GetMatchesState.Error -> {
                    with(binding) {
                        rvLeagues.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is LeaguesOfMatchesViewModel.GetMatchesState.Loaded -> {
                    with(binding) {
                        rvLeagues.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }
                    it.matches.let { list ->
                        adapter.submitList(list)
                        leagueList = list
                    }
                }
                is LeaguesOfMatchesViewModel.GetMatchesState.Loading -> {
                    with(binding) {
                        rvLeagues.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getLeagueList(LocalDate.parse(date))
        }
    }

    private fun launchMatchesOfLeagueFragment(position: Int) {
        (requireActivity() as MainActivity).launchMatchesOfLeagueFragment(
            leagueList[position].leagueCode,
            date
        )
    }

    override fun onRvItemClick(position: Int) {
        launchMatchesOfLeagueFragment(position)
    }

    companion object {

        @JvmStatic
        fun newInstance() = MatchesFragment()
    }
}