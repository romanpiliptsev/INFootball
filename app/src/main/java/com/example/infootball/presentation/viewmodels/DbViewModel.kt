package com.example.infootball.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.domain.usecases.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DbViewModel @Inject constructor(
    private val addFavoriteMatchUseCase: AddFavoriteMatchUseCase,
    private val addFavoriteTeamUseCase: AddFavoriteTeamUseCase,
    private val deleteFavoriteMatchUseCase: DeleteFavoriteMatchUseCase,
    private val deleteFavoriteTeamUseCase: DeleteFavoriteTeamUseCase,
    private val getIsFavoriteMatchUseCase: GetIsFavoriteMatchUseCase,
    private val getIsFavoriteTeamUseCase: GetIsFavoriteTeamUseCase
) : ViewModel() {

    private val _getIsFavoriteMatchLiveData = MutableLiveData<Boolean>()
    val getIsFavoriteMatchLiveData: LiveData<Boolean>
        get() = _getIsFavoriteMatchLiveData

    private val _getIsFavoriteTeamLiveData = MutableLiveData<Boolean>()
    val getIsFavoriteTeamLiveData: LiveData<Boolean>
        get() = _getIsFavoriteTeamLiveData

    fun isFavoriteMatch(matchId: Int) {
        viewModelScope.launch {
            _getIsFavoriteMatchLiveData.value = getIsFavoriteMatchUseCase.invoke(matchId)
        }
    }

    fun isFavoriteTeam(teamId: Int) {
        viewModelScope.launch {
            _getIsFavoriteTeamLiveData.value = getIsFavoriteTeamUseCase.invoke(teamId)
        }
    }

    fun addFavoriteMatch(matchId: Int) {
        viewModelScope.launch {
            addFavoriteMatchUseCase.invoke(matchId)
        }
    }

    fun addFavoriteTeam(teamId: Int) {
        viewModelScope.launch {
            addFavoriteTeamUseCase.invoke(teamId)
        }
    }

    fun deleteFavoriteMatch(matchId: Int) {
        viewModelScope.launch {
            deleteFavoriteMatchUseCase.invoke(matchId)
        }
    }

    fun deleteFavoriteTeam(teamId: Int) {
        viewModelScope.launch {
            deleteFavoriteTeamUseCase.invoke(teamId)
        }
    }
}