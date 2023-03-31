package com.example.infootball.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.infootball.databinding.ActivityMainBinding
import com.example.infootball.presentation.fragments.FavoritesFragment
import com.example.infootball.presentation.fragments.LeaguesFragment
import com.example.infootball.presentation.fragments.MatchesFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.favorites.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, FavoritesFragment.newInstance())
                .commit()
        }

        binding.leagues.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, LeaguesFragment.newInstance())
                .commit()
        }

        binding.matches.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, MatchesFragment.newInstance())
                .commit()
        }
    }
}