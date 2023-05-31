package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.StandingsResponseDto
import com.example.infootball.domain.usecases.GetCompetitionStandingsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class StandingsViewModel @Inject constructor(private val getStandingsUseCase: GetCompetitionStandingsUseCase) :
    ViewModel() {

    private val _getStandingsStateLiveData = MutableLiveData<GetStandingsState>()
    val getStandingsStateLiveData: LiveData<GetStandingsState>
        get() = _getStandingsStateLiveData

    sealed interface GetStandingsState {
        object Error : GetStandingsState
        object Loading : GetStandingsState
        class Loaded(val standings: StandingsResponseDto) : GetStandingsState
    }

    private val getStandingsHandler = CoroutineExceptionHandler { _, th ->
        _getStandingsStateLiveData.value = GetStandingsState.Error
        Log.e("VM throw", th.toString())
    }

    fun getMatchStandings(code: String, season: String) {
        _getStandingsStateLiveData.value = GetStandingsState.Loading

        viewModelScope.launch(getStandingsHandler) {
            val standings = getStandingsUseCase.invoke(code, season)
            _getStandingsStateLiveData.value = GetStandingsState.Loaded(standings)
        }
    }
}