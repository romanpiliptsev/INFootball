package com.example.infootball.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.infootball.databinding.ActivityMainBinding
import com.example.infootball.presentation.fragments.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.favorites.setOnClickListener {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, FavoritesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.leagues.setOnClickListener {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, LeaguesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.matches.setOnClickListener {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, MatchesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.lives.setOnClickListener {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, LiveFragment.newInstance())
                .addToBackStack(null)
                .commit()
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
}