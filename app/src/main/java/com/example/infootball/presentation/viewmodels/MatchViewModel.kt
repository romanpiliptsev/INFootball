package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetMatchByIdUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val getMatchUseCase: GetMatchByIdUseCase) :
    ViewModel() {

    private val _getMatchStateLiveData = MutableLiveData<GetMatchState>()
    val getMatchStateLiveData: LiveData<GetMatchState>
        get() = _getMatchStateLiveData

    sealed interface GetMatchState {
        object Error : GetMatchState
        object Loading : GetMatchState
        class Loaded(val match: MatchDto) : GetMatchState
    }

    private val getMatchHandler = CoroutineExceptionHandler { _, th ->
        _getMatchStateLiveData.value = GetMatchState.Error
        Log.e("VM throw", th.toString())
    }

    fun getMatch(matchId: Int) {
        _getMatchStateLiveData.value = GetMatchState.Loading

        viewModelScope.launch(getMatchHandler) {
            val match = getMatchUseCase.invoke(matchId)
            _getMatchStateLiveData.value = GetMatchState.Loaded(match)
        }
    }
}