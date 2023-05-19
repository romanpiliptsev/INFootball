package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.TopscorerDto
import com.example.infootball.domain.usecases.GetCompetitionTopscorersUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CompetitionTopscorersViewModel(application: Application) : AndroidViewModel(application) {

    private val getTopscorersUseCase = GetCompetitionTopscorersUseCase(application)

    private val _getTopscorersStateLiveData = MutableLiveData<GetTopscorersState>()
    val getTopscorersStateLiveData: LiveData<GetTopscorersState>
        get() = _getTopscorersStateLiveData

    sealed interface GetTopscorersState {
        object Error : GetTopscorersState
        object Loading : GetTopscorersState
        class Loaded(val topscorers: ArrayList<TopscorerDto>) : GetTopscorersState
    }

    private val getTopscorersListHandler = CoroutineExceptionHandler { _, _ ->
        _getTopscorersStateLiveData.value = GetTopscorersState.Error
    }

    fun getTopscorerList(leagueCode: String, season: String, limit: Int) {
        _getTopscorersStateLiveData.value = GetTopscorersState.Loading

        viewModelScope.launch(getTopscorersListHandler) {
            val topscorers = getTopscorersUseCase.invoke(leagueCode, limit, season)
            _getTopscorersStateLiveData.value = GetTopscorersState.Loaded(topscorers)
        }
    }
}