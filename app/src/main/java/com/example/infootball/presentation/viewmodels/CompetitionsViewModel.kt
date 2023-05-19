package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.CompetitionWithAreaDto
import com.example.infootball.domain.usecases.GetCompetitionsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CompetitionsViewModel(application: Application) : AndroidViewModel(application) {

    private val getCompetitionsUseCase = GetCompetitionsUseCase(application)

    private val _getCompetitionsStateLiveData = MutableLiveData<GetCompetitionsState>()
    val getCompetitionsStateLiveData: LiveData<GetCompetitionsState>
        get() = _getCompetitionsStateLiveData

    sealed interface GetCompetitionsState {
        object Error : GetCompetitionsState
        object Loading : GetCompetitionsState
        class Loaded(val competitions: ArrayList<CompetitionWithAreaDto>) : GetCompetitionsState
    }

    private val getCompetitionsHandler = CoroutineExceptionHandler { _, _ ->
        _getCompetitionsStateLiveData.value = GetCompetitionsState.Error
    }

    fun getCompetitions() {
        _getCompetitionsStateLiveData.value = GetCompetitionsState.Loading

        viewModelScope.launch(getCompetitionsHandler) {
            val competitions = getCompetitionsUseCase.invoke()
            _getCompetitionsStateLiveData.value = GetCompetitionsState.Loaded(competitions)
        }
    }
}