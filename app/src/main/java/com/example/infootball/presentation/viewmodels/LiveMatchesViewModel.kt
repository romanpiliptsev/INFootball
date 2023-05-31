package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetLiveMatchesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class LiveMatchesViewModel @Inject constructor(private val getMatchesUseCase: GetLiveMatchesUseCase) :
    ViewModel() {

    private val _getMatchesStateLiveData = MutableLiveData<GetMatchesState>()
    val getMatchesStateLiveData: LiveData<GetMatchesState>
        get() = _getMatchesStateLiveData

    sealed interface GetMatchesState {
        object Error : GetMatchesState
        object Loading : GetMatchesState
        class Loaded(val matches: ArrayList<MatchDto>) : GetMatchesState
    }

    private val getLiveMatchesListHandler = CoroutineExceptionHandler { _, th ->
        _getMatchesStateLiveData.value = GetMatchesState.Error
        Log.e("VM throw", th.toString())
    }

    fun getMatchList() {
        _getMatchesStateLiveData.value = GetMatchesState.Loading

        viewModelScope.launch(getLiveMatchesListHandler) {
            val list = getMatchesUseCase.invoke()
            _getMatchesStateLiveData.value = GetMatchesState.Loaded(list)
        }
    }
}