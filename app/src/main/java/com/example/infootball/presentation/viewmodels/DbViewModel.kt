package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.domain.usecases.*
import kotlinx.coroutines.launch

class DbViewModel(application: Application) : AndroidViewModel(application) {

    private val addFavoriteMatchUseCase = AddFavoriteMatchUseCase(application)
    private val addFavoriteTeamUseCase = AddFavoriteTeamUseCase(application)
    private val deleteFavoriteMatchUseCase = DeleteFavoriteMatchUseCase(application)
    private val deleteFavoriteTeamUseCase = DeleteFavoriteTeamUseCase(application)
    private val getIsFavoriteMatchUseCase = GetIsFavoriteMatchUseCase(application)
    private val getIsFavoriteTeamUseCase = GetIsFavoriteTeamUseCase(application)

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