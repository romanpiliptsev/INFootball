package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetTeamResultsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeamResultsViewModel @Inject constructor(private val getTeamResultsUseCase: GetTeamResultsUseCase) :
    ViewModel() {

    private val _getTeamResultsStateLiveData = MutableLiveData<GetTeamResultsState>()
    val getTeamResultsStateLiveData: LiveData<GetTeamResultsState>
        get() = _getTeamResultsStateLiveData

    sealed interface GetTeamResultsState {
        object Error : GetTeamResultsState
        object Loading : GetTeamResultsState
        class Loaded(val matches: ArrayList<MatchDto>) : GetTeamResultsState
    }

    private val getTeamResultsHandler = CoroutineExceptionHandler { _, th ->
        _getTeamResultsStateLiveData.value = GetTeamResultsState.Error
        Log.e("VM throw", th.toString())
    }

    fun getTeamResults(teamId: Int) {
        _getTeamResultsStateLiveData.value = GetTeamResultsState.Loading

        viewModelScope.launch(getTeamResultsHandler) {
            val matches = getTeamResultsUseCase.invoke(teamId)
            _getTeamResultsStateLiveData.value = GetTeamResultsState.Loaded(matches)
        }
    }
}