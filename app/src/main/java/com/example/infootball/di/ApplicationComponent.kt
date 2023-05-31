package com.example.infootball.di

import android.app.Application
import com.example.infootball.presentation.activities.MainActivity
import com.example.infootball.presentation.activities.MatchActivity
import com.example.infootball.presentation.activities.TeamActivity
import com.example.infootball.presentation.fragments.*
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [Module::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: MatchActivity)
    fun inject(activity: TeamActivity)

    fun inject(fragment: CompetitionFragment)
    fun inject(fragment: CompetitionResultsOrCalendarFragment)
    fun inject(fragment: CompetitionTopscorersFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: H2HFragment)
    fun inject(fragment: LeaguesFragment)
    fun inject(fragment: LiveFragment)
    fun inject(fragment: MatchesFragment)
    fun inject(fragment: MatchesOfLeagueFragment)
    fun inject(fragment: MatchInfoFragment)
    fun inject(fragment: TableFragment)
    fun inject(fragment: TeamCalendarFragment)
    fun inject(fragment: TeamInfoFragment)
    fun inject(fragment: TeamResultsFragment)
    fun inject(fragment: TeamSquadFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}