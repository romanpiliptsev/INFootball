package com.example.infootball.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.LeagueResponseDto
import com.example.infootball.domain.usecases.GetLeagueListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LeaguesViewModel: ViewModel() {

    private val getLeagueListUseCase = GetLeagueListUseCase()

    private val _getLeagueListStateLiveData = MutableLiveData<GetLeagueListState>()
    val getLeagueListStateLiveData: LiveData<GetLeagueListState>
        get() = _getLeagueListStateLiveData

    sealed interface GetLeagueListState {
        object Error : GetLeagueListState
        object Loading : GetLeagueListState
        class Loaded(val leagueList: List<LeagueResponseDto>) : GetLeagueListState
    }

    private val getLeagueListHandler = CoroutineExceptionHandler { _, exception ->
        _getLeagueListStateLiveData.value = GetLeagueListState.Error
    }

    fun getLeagueList(idList: List<Int>) {
        _getLeagueListStateLiveData.value = GetLeagueListState.Loading

        viewModelScope.launch(getLeagueListHandler) {
            val list = getLeagueListUseCase.invoke(idList)
            _getLeagueListStateLiveData.value = GetLeagueListState.Loaded(list)
        }
    }
}