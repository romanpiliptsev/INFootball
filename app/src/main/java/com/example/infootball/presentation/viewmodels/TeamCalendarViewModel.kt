package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetTeamCalendarUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeamCalendarViewModel @Inject constructor(private val getTeamCalendarUseCase: GetTeamCalendarUseCase) :
    ViewModel() {

    private val _getTeamCalendarStateLiveData = MutableLiveData<GetTeamCalendarState>()
    val getTeamCalendarStateLiveData: LiveData<GetTeamCalendarState>
        get() = _getTeamCalendarStateLiveData

    sealed interface GetTeamCalendarState {
        object Error : GetTeamCalendarState
        object Loading : GetTeamCalendarState
        class Loaded(val matches: ArrayList<MatchDto>) : GetTeamCalendarState
    }

    private val getTeamCalendarHandler = CoroutineExceptionHandler { _, th ->
        _getTeamCalendarStateLiveData.value = GetTeamCalendarState.Error
        Log.e("VM throw", th.toString())
    }

    fun getTeamCalendar(teamId: Int) {
        _getTeamCalendarStateLiveData.value = GetTeamCalendarState.Loading

        viewModelScope.launch(getTeamCalendarHandler) {
            val matches = getTeamCalendarUseCase.invoke(teamId)
            _getTeamCalendarStateLiveData.value = GetTeamCalendarState.Loaded(matches)
        }
    }
}