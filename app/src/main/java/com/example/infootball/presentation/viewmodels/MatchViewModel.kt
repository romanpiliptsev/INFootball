package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetMatchByIdUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MatchViewModel(application: Application) : AndroidViewModel(application) {

    private val getMatchUseCase = GetMatchByIdUseCase(application)

    private val _getMatchStateLiveData = MutableLiveData<GetMatchState>()
    val getMatchStateLiveData: LiveData<GetMatchState>
        get() = _getMatchStateLiveData

    sealed interface GetMatchState {
        object Error : GetMatchState
        object Loading : GetMatchState
        class Loaded(val match: MatchDto) : GetMatchState
    }

    private val getMatchHandler = CoroutineExceptionHandler { _, _ ->
        _getMatchStateLiveData.value = GetMatchState.Error
    }

    fun getMatch(matchId: Int) {
        _getMatchStateLiveData.value = GetMatchState.Loading

        viewModelScope.launch(getMatchHandler) {
            val match = getMatchUseCase.invoke(matchId)
            _getMatchStateLiveData.value = GetMatchState.Loaded(match)
        }
    }
}