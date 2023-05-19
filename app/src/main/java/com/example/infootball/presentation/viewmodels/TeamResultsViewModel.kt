package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetTeamResultsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class TeamResultsViewModel(application: Application) : AndroidViewModel(application) {
    private val getTeamResultsUseCase = GetTeamResultsUseCase(application)

    private val _getTeamResultsStateLiveData = MutableLiveData<GetTeamResultsState>()
    val getTeamResultsStateLiveData: LiveData<GetTeamResultsState>
        get() = _getTeamResultsStateLiveData

    sealed interface GetTeamResultsState {
        object Error : GetTeamResultsState
        object Loading : GetTeamResultsState
        class Loaded(val matches: ArrayList<MatchDto>) : GetTeamResultsState
    }

    private val getTeamResultsHandler = CoroutineExceptionHandler { _, _ ->
        _getTeamResultsStateLiveData.value = GetTeamResultsState.Error
    }

    fun getTeamResults(teamId: Int) {
        _getTeamResultsStateLiveData.value = GetTeamResultsState.Loading

        viewModelScope.launch(getTeamResultsHandler) {
            val matches = getTeamResultsUseCase.invoke(teamId)
            _getTeamResultsStateLiveData.value = GetTeamResultsState.Loaded(matches)
        }
    }
}