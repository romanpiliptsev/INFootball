package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.domain.entities.ExtendedTeamEntity
import com.example.infootball.domain.usecases.GetTeamByIdUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeamViewModel @Inject constructor(private val getTeamUseCase: GetTeamByIdUseCase) :
    ViewModel() {

    private val _getTeamStateLiveData = MutableLiveData<GetTeamState>()
    val getTeamStateLiveData: LiveData<GetTeamState>
        get() = _getTeamStateLiveData

    sealed interface GetTeamState {
        object Error : GetTeamState
        object Loading : GetTeamState
        class Loaded(val team: ExtendedTeamEntity) : GetTeamState
    }

    private val getTeamHandler = CoroutineExceptionHandler { _, th ->
        _getTeamStateLiveData.value = GetTeamState.Error
        Log.e("VM throw", th.toString())
    }

    fun getTeam(teamId: Int) {
        _getTeamStateLiveData.value = GetTeamState.Loading

        viewModelScope.launch(getTeamHandler) {
            val match = getTeamUseCase.invoke(teamId)
            _getTeamStateLiveData.value = GetTeamState.Loaded(match)
        }
    }
}