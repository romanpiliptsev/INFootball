package com.example.infootball.presentation.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.infootball.InfootballApp
import com.example.infootball.R
import com.example.infootball.data.network.model.PlayerOfTeamDto
import com.example.infootball.databinding.ActivityTeamBinding
import com.example.infootball.domain.entities.ExtendedTeamEntity
import com.example.infootball.presentation.fragments.*
import com.example.infootball.presentation.viewmodels.DbViewModel
import com.example.infootball.presentation.viewmodels.TeamViewModel
import com.example.infootball.presentation.viewmodels.ViewModelFactory
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso
import javax.inject.Inject

class TeamActivity : AppCompatActivity() {

    private val component by lazy {
        (application as InfootballApp).component
    }

    private val binding by lazy {
        ActivityTeamBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val vm: TeamViewModel by viewModels {
        viewModelFactory
    }

    private val dbVm: DbViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var team: ExtendedTeamEntity
    private var isFav: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val teamId = intent.getIntExtra(EXTRA_TEAM_ID, -1)

        dbVm.isFavoriteTeam(teamId)
        dbVm.getIsFavoriteTeamLiveData.observe(this) {
            isFav = it
            if (it) {
                binding.star.setImageResource(R.drawable.ic_yellow_star)
            } else {
                binding.star.setImageResource(R.drawable.ic_empty_star)
            }
        }

        binding.star.setOnClickListener {
            if (isFav) {
                dbVm.deleteFavoriteTeam(teamId)
                binding.star.setImageResource(R.drawable.ic_empty_star)
            } else {
                dbVm.addFavoriteTeam(teamId)
                binding.star.setImageResource(R.drawable.ic_yellow_star)
            }
            isFav = !isFav
        }

        vm.getTeam(teamId)
        vm.getTeamStateLiveData.observe(this) {
            when (it) {
                is TeamViewModel.GetTeamState.Error -> {
                    with(binding) {
                        okGroup.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is TeamViewModel.GetTeamState.Loaded -> {
                    with(binding) {
                        okGroup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE

                        with(it.team) {
                            team = this
                            teamName.text = name
                            country.text = area?.name

                            if (crest?.endsWith(".png") == true) {
                                Picasso.get().load(crest)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(teamLogo)
                            } else {
                                GlideToVectorYou
                                    .init()
                                    .with(this@TeamActivity)
                                    .load(Uri.parse(crest), teamLogo)
                            }
                            if (area?.flag?.endsWith(".png") == true) {
                                Picasso.get().load(area?.flag)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(flag)
                            } else {
                                GlideToVectorYou
                                    .init()
                                    .with(this@TeamActivity)
                                    .load(Uri.parse(area?.flag), flag)
                            }

                            binding.info.setOnClickListener { view ->
                                launchTeamInfoFragment(
                                    address ?: "",
                                    website ?: "",
                                    founded ?: -1,
                                    clubColors ?: "",
                                    venue ?: ""
                                )
                                setColorsToFragmentSwitchers(view as TextView)
                            }
                            binding.info.performClick()

                            binding.calendar.setOnClickListener { view ->
                                launchTeamCalendarFragment(id ?: -1)
                                setColorsToFragmentSwitchers(view as TextView)
                            }
                            binding.results.setOnClickListener { view ->
                                launchTeamResultsFragment(id ?: -1)
                                setColorsToFragmentSwitchers(view as TextView)
                            }
                            binding.squad.setOnClickListener { view ->
                                launchTeamSquadFragment(team.squad)
                                setColorsToFragmentSwitchers(view as TextView)
                            }
                            binding.table.setOnClickListener { view ->
                                launchTeamTableFragment(leagueCode)
                                setColorsToFragmentSwitchers(view as TextView)
                            }
                        }
                    }
                }
                is TeamViewModel.GetTeamState.Loading -> {
                    with(binding) {
                        okGroup.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getTeam(teamId)
        }
    }

    private fun launchTeamInfoFragment(
        address: String,
        website: String,
        founded: Int,
        colors: String,
        venue: String
    ) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                TeamInfoFragment.newInstance(address, website, founded, colors, venue)
            )
            .commit()
    }

    private fun launchTeamResultsFragment(teamId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                TeamResultsFragment.newInstance(teamId)
            )
            .commit()
    }

    private fun launchTeamCalendarFragment(teamId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                TeamCalendarFragment.newInstance(teamId)
            )
            .commit()
    }

    private fun launchTeamTableFragment(leagueCode: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                TableFragment.newInstance(leagueCode)
            )
            .commit()
    }

    private fun launchTeamSquadFragment(players: ArrayList<PlayerOfTeamDto>) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                TeamSquadFragment.newInstance(players)
            )
            .commit()
    }

    private fun setColorsToFragmentSwitchers(tv: TextView) {
        arrayListOf(
            binding.info,
            binding.results,
            binding.calendar,
            binding.squad,
            binding.table
        ).forEach {
            it.setBackgroundColor(
                resources.getColor(
                    when (it) {
                        tv -> R.color.red
                        else -> {
                            if (resources.configuration.uiMode and
                                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
                            ) {
                                R.color.black
                            } else {
                                R.color.white
                            }
                        }
                    },
                    theme
                )
            )
        }
    }

    companion object {
        private const val EXTRA_TEAM_ID = "teamId"

        fun newIntent(context: Context, teamId: Int): Intent {
            return Intent(context, TeamActivity::class.java).apply {
                putExtra(EXTRA_TEAM_ID, teamId)
            }
        }
    }
}