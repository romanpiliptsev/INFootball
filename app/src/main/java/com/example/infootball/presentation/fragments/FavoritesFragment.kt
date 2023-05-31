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
import com.example.infootball.databinding.FragmentFavoritesBinding
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.adapters.ResultsAndCalendarAdapter
import com.example.infootball.presentation.viewmodels.FavoriteMatchesViewModel
import com.example.infootball.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class FavoritesFragment : Fragment(), ResultsAndCalendarAdapter.OnRvItemClickListener {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private val binding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val vm: FavoriteMatchesViewModel by viewModels {
        viewModelFactory
    }

    private val rvMatches by lazy {
        binding.rvMatches
    }

    private lateinit var matches: ArrayList<MatchDto>

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

        val adapter = ResultsAndCalendarAdapter(this)
        rvMatches.adapter = adapter
        rvMatches.layoutManager = LinearLayoutManager(requireContext())

        vm.getFavoriteMatches()

        vm.getFavoriteMatchesStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteMatchesViewModel.GetFavoriteMatchesState.Error -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is FavoriteMatchesViewModel.GetFavoriteMatchesState.Loaded -> {
                    with(binding) {
                        rvMatches.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE
                    }

                    it.matches.let { list ->
                        adapter.submitList(list)
                        matches = list
                    }
                }
                is FavoriteMatchesViewModel.GetFavoriteMatchesState.Loading -> {
                    with(binding) {
                        rvMatches.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getFavoriteMatches()
        }
    }

    override fun onRvItemClick(position: Int) {
        startActivity(MatchActivity.newIntent(requireContext(), matches[position].id ?: -1))
    }

    companion object {

        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }
}