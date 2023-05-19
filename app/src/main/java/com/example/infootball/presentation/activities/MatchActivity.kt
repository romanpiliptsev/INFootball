package com.example.infootball.presentation.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.infootball.R
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.databinding.ActivityMatchBinding
import com.example.infootball.presentation.fragments.H2HFragment
import com.example.infootball.presentation.fragments.MatchInfoFragment
import com.example.infootball.presentation.fragments.TableFragment
import com.example.infootball.presentation.viewmodels.DbViewModel
import com.example.infootball.presentation.viewmodels.MatchViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso

class MatchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMatchBinding.inflate(layoutInflater)
    }

    private lateinit var match: MatchDto
    private var isFav: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val matchId = intent.getIntExtra(EXTRA_MATCH_ID, -1)

        val dbVm = ViewModelProvider(this)[DbViewModel::class.java]
        dbVm.isFavoriteMatch(matchId)
        dbVm.getIsFavoriteMatchLiveData.observe(this) {
            isFav = it
            if (it) {
                binding.matchStar.setImageResource(R.drawable.ic_yellow_star)
            } else {
                binding.matchStar.setImageResource(R.drawable.ic_empty_star)
            }
        }

        binding.matchStar.setOnClickListener {
            if (isFav) {
                dbVm.deleteFavoriteMatch(matchId)
                binding.matchStar.setImageResource(R.drawable.ic_empty_star)
            } else {
                dbVm.addFavoriteMatch(matchId)
                binding.matchStar.setImageResource(R.drawable.ic_yellow_star)
            }
            isFav = !isFav
        }

        val vm = ViewModelProvider(this)[MatchViewModel::class.java]
        vm.getMatch(matchId)

        vm.getMatchStateLiveData.observe(this) {
            when (it) {
                is MatchViewModel.GetMatchState.Error -> {
                    with(binding) {
                        loadedGroup.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.VISIBLE
                    }
                }
                is MatchViewModel.GetMatchState.Loaded -> {
                    with(binding) {
                        loadedGroup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        errorGroup.visibility = View.GONE

                        with(it.match) {
                            match = this
                            homeTeamGoals.text = (score?.fullTime?.home ?: "").toString()
                            awayTeamGoals.text = (score?.fullTime?.away ?: "").toString()
                            homeTeamName.text = homeTeam?.name
                            awayTeamName.text = awayTeam?.name
                            dateAndTime.text = utcDate?.replace("T", " ")?.slice(0..15)
                            leagueDescription.text =
                                "${area?.name}: ${competition?.name}"
                            competitionStage.text = when {
                                matchday != null -> "$matchday tour"
                                stage != null -> "${stage?.replace("_", " ")?.lowercase()}"
                                else -> ""
                            }
                            arrayListOf(
                                leagueLogo,
                                homeTeamLogo,
                                awayTeamLogo,
                                flag
                            ).forEach { imageView ->
                                setImage(
                                    when (imageView) {
                                        leagueLogo -> competition?.emblem ?: ""
                                        homeTeamLogo -> homeTeam?.crest ?: ""
                                        awayTeamLogo -> awayTeam?.crest ?: ""
                                        flag -> area?.flag ?: ""
                                        else -> ""
                                    },
                                    imageView,
                                    this@MatchActivity
                                )
                            }
                            homeTeamLogo.setOnClickListener {
                                launchTeamActivity(homeTeam?.id ?: -1)
                            }
                            awayTeamLogo.setOnClickListener {
                                launchTeamActivity(awayTeam?.id ?: -1)
                            }
                        }
                    }
                    launchMatchInfoFragment(
                        match.venue ?: "",
                        ArrayList(match.referees.map { referee -> referee.name ?: "" })
                    )
                }
                is MatchViewModel.GetMatchState.Loading -> {
                    with(binding) {
                        loadedGroup.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorGroup.visibility = View.GONE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener {
            vm.getMatch(matchId)
        }

        binding.h2h.setOnClickListener {
            launchH2HFragment(match.id ?: -1)
            setColorsToFragmentSwitchers(it as TextView)
        }
        binding.table.setOnClickListener {
            launchTableFragment(
                match.competition?.code ?: "",
                match.season?.startDate?.slice(0..3) ?: ""
            )
            setColorsToFragmentSwitchers(it as TextView)
        }
        binding.matchInfo.setOnClickListener {
            launchMatchInfoFragment(
                match.venue ?: "",
                ArrayList(match.referees.map { referee -> referee.name ?: "" })
            )
            setColorsToFragmentSwitchers(it as TextView)
        }
        arrayListOf(
            binding.leagueLogo,
            binding.leagueDescription,
            binding.competitionStage,
            binding.flag
        ).forEach { view ->
            view.setOnClickListener {
                startActivity(MainActivity.newIntent(this, match.competition?.code ?: ""))
            }
        }
    }

    private fun setColorsToFragmentSwitchers(tv: TextView) {
        arrayListOf(binding.matchInfo, binding.h2h, binding.table).forEach {
            it.setBackgroundColor(
                resources.getColor(
                    when (it) {
                        tv -> R.color.red
                        else -> R.color.white_or_black
                    },
                    theme
                )
            )
        }
    }

    private fun launchMatchInfoFragment(venue: String, referees: ArrayList<String>) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                MatchInfoFragment.newInstance(venue, referees)
            )
            .commit()
    }

    private fun launchH2HFragment(matchId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                H2HFragment.newInstance(matchId)
            )
            .commit()
    }

    private fun launchTableFragment(leagueCode: String, season: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                TableFragment.newInstance(leagueCode, season)
            )
            .commit()
    }

    private fun setImage(imgLink: String, imageView: ImageView, context: Context) {
        if (imgLink.endsWith(".png")) {
            Picasso.get().load(imgLink)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        } else {
            GlideToVectorYou
                .init()
                .with(context)
                .load(Uri.parse(imgLink), imageView)
        }
    }

    private fun launchTeamActivity(teamId: Int) {
        startActivity(TeamActivity.newIntent(this, teamId))
    }

    companion object {
        private const val EXTRA_MATCH_ID = "matchId"

        fun newIntent(context: Context, matchId: Int): Intent {
            val intent = Intent(context, MatchActivity::class.java)
            intent.putExtra(EXTRA_MATCH_ID, matchId)
            return intent
        }
    }
}