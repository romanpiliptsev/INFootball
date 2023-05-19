package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.H2HDto
import com.example.infootball.domain.usecases.GetMatchH2HUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class H2HViewModel(application: Application) : AndroidViewModel(application) {

    private val getH2HUseCase = GetMatchH2HUseCase(application)

    private val _getH2HStateLiveData = MutableLiveData<GetH2HState>()
    val getH2HStateLiveData: LiveData<GetH2HState>
        get() = _getH2HStateLiveData

    sealed interface GetH2HState {
        object Error : GetH2HState
        object Loading : GetH2HState
        class Loaded(val h2h: H2HDto) : GetH2HState
    }

    private val getH2HHandler = CoroutineExceptionHandler { _, _ ->
        _getH2HStateLiveData.value = GetH2HState.Error
    }

    fun getH2H(matchId: Int) {
        _getH2HStateLiveData.value = GetH2HState.Loading

        viewModelScope.launch(getH2HHandler) {
            val h2h = getH2HUseCase.invoke(matchId)
            _getH2HStateLiveData.value = GetH2HState.Loaded(h2h)
        }
    }
}