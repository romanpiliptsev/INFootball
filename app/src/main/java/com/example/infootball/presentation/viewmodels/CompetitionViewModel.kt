package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.CompetitionWithAreaDto
import com.example.infootball.domain.usecases.GetCompetitionUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompetitionViewModel @Inject constructor(private val getCompetitionUseCase: GetCompetitionUseCase) :
    ViewModel() {

    private val _getCompetitionStateLiveData = MutableLiveData<GetCompetitionState>()
    val getCompetitionStateLiveData: LiveData<GetCompetitionState>
        get() = _getCompetitionStateLiveData

    sealed interface GetCompetitionState {
        object Error : GetCompetitionState
        object Loading : GetCompetitionState
        class Loaded(val competition: CompetitionWithAreaDto) : GetCompetitionState
    }

    private val getCompetitionHandler = CoroutineExceptionHandler { _, th ->
        _getCompetitionStateLiveData.value = GetCompetitionState.Error
        Log.e("VM throw", th.toString())
    }

    fun getCompetition(competitionCode: String) {
        _getCompetitionStateLiveData.value = GetCompetitionState.Loading

        viewModelScope.launch(getCompetitionHandler) {
            val competition = getCompetitionUseCase.invoke(competitionCode)
            _getCompetitionStateLiveData.value = GetCompetitionState.Loaded(competition)
        }
    }
}