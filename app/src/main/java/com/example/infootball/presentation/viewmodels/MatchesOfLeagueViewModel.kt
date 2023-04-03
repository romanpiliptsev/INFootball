package com.example.infootball.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetMatchesOfLeagueDayUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MatchesOfLeagueViewModel: ViewModel() {

    private val getMatchesUseCase = GetMatchesOfLeagueDayUseCase()

    private val _getMatchesStateLiveData = MutableLiveData<GetMatchesState>()
    val getMatchesStateLiveData: LiveData<GetMatchesState>
        get() = _getMatchesStateLiveData

    sealed interface GetMatchesState {
        object Error : GetMatchesState
        object Loading : GetMatchesState
        class Loaded(val matches: ArrayList<MatchDto>) : GetMatchesState
    }

    private val getLeagueListHandler = CoroutineExceptionHandler { _, _ ->
        _getMatchesStateLiveData.value = GetMatchesState.Error
    }

    fun getMatchList(competition: String, date: String) {
        _getMatchesStateLiveData.value = GetMatchesState.Loading

        viewModelScope.launch(getLeagueListHandler) {
            val list = getMatchesUseCase.invoke(competition, date)
            _getMatchesStateLiveData.value = GetMatchesState.Loaded(list)
        }
    }
}