package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetLiveMatchesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LiveMatchesViewModel(application: Application) : AndroidViewModel(application) {

    private val getMatchesUseCase = GetLiveMatchesUseCase(application)

    private val _getMatchesStateLiveData = MutableLiveData<GetMatchesState>()
    val getMatchesStateLiveData: LiveData<GetMatchesState>
        get() = _getMatchesStateLiveData

    sealed interface GetMatchesState {
        object Error : GetMatchesState
        object Loading : GetMatchesState
        class Loaded(val matches: ArrayList<MatchDto>) : GetMatchesState
    }

    private val getLiveMatchesListHandler = CoroutineExceptionHandler { _, _ ->
        _getMatchesStateLiveData.value = GetMatchesState.Error
    }

    fun getMatchList() {
        _getMatchesStateLiveData.value = GetMatchesState.Loading

        viewModelScope.launch(getLiveMatchesListHandler) {
            val list = getMatchesUseCase.invoke()
            _getMatchesStateLiveData.value = GetMatchesState.Loaded(list)
        }
    }
}