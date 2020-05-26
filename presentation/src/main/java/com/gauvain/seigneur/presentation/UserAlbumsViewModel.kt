package com.gauvain.seigneur.presentation

import android.util.Log
import androidx.lifecycle.*
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UserAlbumsViewModel(
    private val useCase: GetUserAlbumsUseCase
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
    }

    fun getAlbums() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                useCase.invoke("2529", 0)
            }
            when (result) {
                is Outcome.Success -> {
                    Log.d("requestOutcome", "success  ${result.data.next}")
                }
                is Outcome.Error -> {
                    Log.d("requestOutcome", "failure $result")
                }
            }
        }
    }
}