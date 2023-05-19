package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetTeamCalendarUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class TeamCalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val getTeamCalendarUseCase = GetTeamCalendarUseCase(application)

    private val _getTeamCalendarStateLiveData = MutableLiveData<GetTeamCalendarState>()
    val getTeamCalendarStateLiveData: LiveData<GetTeamCalendarState>
        get() = _getTeamCalendarStateLiveData

    sealed interface GetTeamCalendarState {
        object Error : GetTeamCalendarState
        object Loading : GetTeamCalendarState
        class Loaded(val matches: ArrayList<MatchDto>) : GetTeamCalendarState
    }

    private val getTeamCalendarHandler = CoroutineExceptionHandler { _, _ ->
        _getTeamCalendarStateLiveData.value = GetTeamCalendarState.Error
    }

    fun getTeamCalendar(teamId: Int) {
        _getTeamCalendarStateLiveData.value = GetTeamCalendarState.Loading

        viewModelScope.launch(getTeamCalendarHandler) {
            val matches = getTeamCalendarUseCase.invoke(teamId)
            _getTeamCalendarStateLiveData.value = GetTeamCalendarState.Loaded(matches)
        }
    }
}