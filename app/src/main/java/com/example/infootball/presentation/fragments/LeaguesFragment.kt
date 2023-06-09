package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infootball.InfootballApp
import com.example.infootball.data.network.model.CompetitionWithAreaDto
import com.example.infootball.databinding.FragmentLeaguesBinding
import com.example.infootball.presentation.activities.MainActivity
import com.example.infootball.presentation.adapters.CompetitionsAdapter
import com.example.infootball.presentation.viewmodels.CompetitionsViewModel
import com.example.infootball.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class LeaguesFragment : Fragment(), CompetitionsAdapter.OnRvItemClickListener {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private val binding by lazy {
        FragmentLeaguesBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val vm: CompetitionsViewModel by viewModels {
        viewModelFactory
    }

    private val rvCompetitions by lazy {
        binding.rvLeagues
    }

    private lateinit var competitionList: List<CompetitionWithAreaDto>

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

        val adapter = CompetitionsAdapter(this)
        rvCompetitions.adapter = adapter
        rvCompetitions.layoutManager = LinearLayoutManager(requireContext())

        vm.getCompetitions()

        vm.getCompetitionsStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is CompetitionsViewModel.GetCompetitionsState.Error -> {
                    with(binding) {
                        rvCompetitions.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is CompetitionsViewModel.GetCompetitionsState.Loaded -> {
                    with(binding) {
                        rvCompetitions.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.competitions.let { list ->
                        adapter.submitList(list)
                        competitionList = list
                    }
                }
                is CompetitionsViewModel.GetCompetitionsState.Loading -> {
                    with(binding) {
                        rvCompetitions.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getCompetitions()
        }
    }

    override fun onRvItemClick(position: Int) {
        (requireActivity() as MainActivity).launchCompetitionFragment(
            competitionList[position].code ?: throw RuntimeException()
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() = LeaguesFragment()
    }
}