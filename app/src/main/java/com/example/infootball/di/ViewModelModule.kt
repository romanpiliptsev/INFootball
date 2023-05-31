package com.example.infootball.di

import androidx.lifecycle.ViewModel
import com.example.infootball.presentation.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CompetitionMatchesViewModel::class)
    fun bindCompetitionMatchesViewModel(viewModel: CompetitionMatchesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompetitionsViewModel::class)
    fun bindCompetitionsViewModel(viewModel: CompetitionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompetitionTopscorersViewModel::class)
    fun bindCompetitionTopscorersViewModel(viewModel: CompetitionTopscorersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompetitionViewModel::class)
    fun bindCompetitionViewModel(viewModel: CompetitionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DbViewModel::class)
    fun bindDbViewModel(viewModel: DbViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMatchesViewModel::class)
    fun bindFavoriteMatchesViewModel(viewModel: FavoriteMatchesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(H2HViewModel::class)
    fun bindH2HViewModel(viewModel: H2HViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LeaguesOfMatchesViewModel::class)
    fun bindLeaguesOfMatchesViewModel(viewModel: LeaguesOfMatchesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LiveMatchesViewModel::class)
    fun bindLiveMatchesViewModel(viewModel: LiveMatchesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MatchesOfLeagueViewModel::class)
    fun bindMatchesOfLeagueViewModel(viewModel: MatchesOfLeagueViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    fun bindMatchViewModel(viewModel: MatchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StandingsViewModel::class)
    fun bindStandingsViewModel(viewModel: StandingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TeamCalendarViewModel::class)
    fun bindTeamCalendarViewModel(viewModel: TeamCalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TeamResultsViewModel::class)
    fun bindTeamResultsViewModel(viewModel: TeamResultsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TeamViewModel::class)
    fun bindTeamViewModel(viewModel: TeamViewModel): ViewModel
}