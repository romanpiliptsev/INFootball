package com.example.infootball.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetAllFavoriteMatchesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteMatchesViewModel(application: Application) : AndroidViewModel(application) {

    private val getFavoriteMatchesUseCase = GetAllFavoriteMatchesUseCase(application)

    private val _getFavoriteMatchesStateLiveData = MutableLiveData<GetFavoriteMatchesState>()
    val getFavoriteMatchesStateLiveData: LiveData<GetFavoriteMatchesState>
        get() = _getFavoriteMatchesStateLiveData

    sealed interface GetFavoriteMatchesState {
        object Error : GetFavoriteMatchesState
        object Loading : GetFavoriteMatchesState
        class Loaded(val matches: ArrayList<MatchDto>) : GetFavoriteMatchesState
    }

    private val getFavoriteMatchesHandler = CoroutineExceptionHandler { _, _ ->
        _getFavoriteMatchesStateLiveData.postValue(GetFavoriteMatchesState.Error)
    }

    fun getFavoriteMatches() {
        _getFavoriteMatchesStateLiveData.value = GetFavoriteMatchesState.Loading

        viewModelScope.launch(getFavoriteMatchesHandler + Dispatchers.IO) {
            val matches = getFavoriteMatchesUseCase.invoke()
            _getFavoriteMatchesStateLiveData.postValue(
                GetFavoriteMatchesState.Loaded(
                    ArrayList(matches)
                )
            )
        }
    }
}