package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetMatchesOfLeagueDayUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchesOfLeagueViewModel @Inject constructor(private val getMatchesUseCase: GetMatchesOfLeagueDayUseCase) :
    ViewModel() {

    private val _getMatchesStateLiveData = MutableLiveData<GetMatchesState>()
    val getMatchesStateLiveData: LiveData<GetMatchesState>
        get() = _getMatchesStateLiveData

    sealed interface GetMatchesState {
        object Error : GetMatchesState
        object Loading : GetMatchesState
        class Loaded(val matches: ArrayList<MatchDto>) : GetMatchesState
    }

    private val getMatchesOfLeagueListHandler = CoroutineExceptionHandler { _, th ->
        _getMatchesStateLiveData.value = GetMatchesState.Error
        Log.e("VM throw", th.toString())
    }

    fun getMatchList(competition: String, date: String) {
        _getMatchesStateLiveData.value = GetMatchesState.Loading

        viewModelScope.launch(getMatchesOfLeagueListHandler) {
            val list = getMatchesUseCase.invoke(competition, date)
            _getMatchesStateLiveData.value = GetMatchesState.Loaded(list)
        }
    }
}