package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.domain.entities.LeagueOfMatchesEntity
import com.example.infootball.domain.usecases.GetLeaguesOfMatchesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class LeaguesOfMatchesViewModel @Inject constructor(private val getLeaguesOfMatchesUseCase: GetLeaguesOfMatchesUseCase) :
    ViewModel() {

    private val _getLeaguesOfMatchesStateLiveData = MutableLiveData<GetMatchesState>()
    val getLeaguesOfMatchesStateLiveData: LiveData<GetMatchesState>
        get() = _getLeaguesOfMatchesStateLiveData

    sealed interface GetMatchesState {
        object Error : GetMatchesState
        object Loading : GetMatchesState
        class Loaded(val matches: List<LeagueOfMatchesEntity>) : GetMatchesState
    }

    private val getLeagueListHandler = CoroutineExceptionHandler { _, th ->
        _getLeaguesOfMatchesStateLiveData.value = GetMatchesState.Error
        Log.e("VM throw", th.toString())
    }

    fun getLeagueList(date: LocalDate) {
        _getLeaguesOfMatchesStateLiveData.value = GetMatchesState.Loading

        viewModelScope.launch(getLeagueListHandler) {
            val list = getLeaguesOfMatchesUseCase.invoke(date)
            _getLeaguesOfMatchesStateLiveData.value = GetMatchesState.Loaded(list)
        }
    }
}