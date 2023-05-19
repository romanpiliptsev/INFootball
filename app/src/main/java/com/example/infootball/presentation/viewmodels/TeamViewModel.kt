package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.domain.entities.ExtendedTeamEntity
import com.example.infootball.domain.usecases.GetTeamByIdUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class TeamViewModel(application: Application) : AndroidViewModel(application) {
    private val getTeamUseCase = GetTeamByIdUseCase(application)

    private val _getTeamStateLiveData = MutableLiveData<GetTeamState>()
    val getTeamStateLiveData: LiveData<GetTeamState>
        get() = _getTeamStateLiveData

    sealed interface GetTeamState {
        object Error : GetTeamState
        object Loading : GetTeamState
        class Loaded(val team: ExtendedTeamEntity) : GetTeamState
    }

    private val getTeamHandler = CoroutineExceptionHandler { _, _ ->
        _getTeamStateLiveData.value = GetTeamState.Error
    }

    fun getTeam(teamId: Int) {
        _getTeamStateLiveData.value = GetTeamState.Loading

        viewModelScope.launch(getTeamHandler) {
            val match = getTeamUseCase.invoke(teamId)
            _getTeamStateLiveData.value = GetTeamState.Loaded(match)
        }
    }
}