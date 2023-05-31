package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.CompetitionWithAreaDto
import com.example.infootball.domain.usecases.GetCompetitionsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompetitionsViewModel @Inject constructor(private val getCompetitionsUseCase: GetCompetitionsUseCase) :
    ViewModel() {

    private val _getCompetitionsStateLiveData = MutableLiveData<GetCompetitionsState>()
    val getCompetitionsStateLiveData: LiveData<GetCompetitionsState>
        get() = _getCompetitionsStateLiveData

    sealed interface GetCompetitionsState {
        object Error : GetCompetitionsState
        object Loading : GetCompetitionsState
        class Loaded(val competitions: ArrayList<CompetitionWithAreaDto>) : GetCompetitionsState
    }

    private val getCompetitionsHandler = CoroutineExceptionHandler { _, th ->
        _getCompetitionsStateLiveData.value = GetCompetitionsState.Error
        Log.e("VM throw", th.toString())
    }

    fun getCompetitions() {
        _getCompetitionsStateLiveData.value = GetCompetitionsState.Loading

        viewModelScope.launch(getCompetitionsHandler) {
            val competitions = getCompetitionsUseCase.invoke()
            _getCompetitionsStateLiveData.value = GetCompetitionsState.Loaded(competitions)
        }
    }
}