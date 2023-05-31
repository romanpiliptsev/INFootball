package com.example.infootball.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.usecases.GetAllFavoriteMatchesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteMatchesViewModel @Inject constructor(private val getFavoriteMatchesUseCase: GetAllFavoriteMatchesUseCase) :
    ViewModel() {

    private val _getFavoriteMatchesStateLiveData = MutableLiveData<GetFavoriteMatchesState>()
    val getFavoriteMatchesStateLiveData: LiveData<GetFavoriteMatchesState>
        get() = _getFavoriteMatchesStateLiveData

    sealed interface GetFavoriteMatchesState {
        object Error : GetFavoriteMatchesState
        object Loading : GetFavoriteMatchesState
        class Loaded(val matches: ArrayList<MatchDto>) : GetFavoriteMatchesState
    }

    private val getFavoriteMatchesHandler = CoroutineExceptionHandler { _, th ->
        _getFavoriteMatchesStateLiveData.postValue(GetFavoriteMatchesState.Error)
        Log.e("VM throw", th.toString())
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