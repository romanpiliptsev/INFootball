package com.example.infootball.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.infootball.R
import com.example.infootball.databinding.ActivityMainBinding
import com.example.infootball.presentation.fragments.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_COMPETITION_CODE).let {
            if (it != null) {
                launchCompetitionFragment(it)
            }
        }

        binding.favorites.setOnClickListener {
            setColorsToFragmentSwitchers(it as TextView)
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, FavoritesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.leagues.setOnClickListener {
            setColorsToFragmentSwitchers(it as TextView)
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, LeaguesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.matches.setOnClickListener {
            setColorsToFragmentSwitchers(it as TextView)
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, MatchesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.lives.setOnClickListener {
            setColorsToFragmentSwitchers(it as TextView)
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, LiveFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setColorsToFragmentSwitchers(tv: TextView) {
        arrayListOf(binding.matches, binding.lives, binding.leagues, binding.favorites).forEach {
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

    fun launchMatchesOfLeagueFragment(competition: String, date: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                MatchesOfLeagueFragment.newInstance(competition, date)
            )
            .addToBackStack(null)
            .commit()
    }

    fun launchCompetitionFragment(competitionCode: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                CompetitionFragment.newInstance(competitionCode)
            )
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val EXTRA_COMPETITION_CODE = "competitionCode"

        fun newIntent(context: Context, competitionCode: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_COMPETITION_CODE, competitionCode)
            return intent
        }
    }
}