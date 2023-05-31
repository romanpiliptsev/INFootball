package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.TopscorerDto
import com.example.infootball.domain.usecases.GetCompetitionTopscorersUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompetitionTopscorersViewModel @Inject constructor(private val getTopscorersUseCase: GetCompetitionTopscorersUseCase) :
    ViewModel() {

    private val _getTopscorersStateLiveData = MutableLiveData<GetTopscorersState>()
    val getTopscorersStateLiveData: LiveData<GetTopscorersState>
        get() = _getTopscorersStateLiveData

    sealed interface GetTopscorersState {
        object Error : GetTopscorersState
        object Loading : GetTopscorersState
        class Loaded(val topscorers: ArrayList<TopscorerDto>) : GetTopscorersState
    }

    private val getTopscorersListHandler = CoroutineExceptionHandler { _, th ->
        _getTopscorersStateLiveData.value = GetTopscorersState.Error
        Log.e("VM throw", th.toString())
    }

    fun getTopscorerList(leagueCode: String, season: String, limit: Int) {
        _getTopscorersStateLiveData.value = GetTopscorersState.Loading

        viewModelScope.launch(getTopscorersListHandler) {
            val topscorers = getTopscorersUseCase.invoke(leagueCode, limit, season)
            _getTopscorersStateLiveData.value = GetTopscorersState.Loaded(topscorers)
        }
    }
}