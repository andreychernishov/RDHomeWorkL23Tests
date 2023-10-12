package com.example.rdhomeworkl23testapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(val repository: Repository): ViewModel() {
    private val _uiState = MutableLiveData<UIState>(UIState.Empty)
    val uiState: LiveData<UIState> = _uiState

    fun getData() {
        _uiState.value = UIState.Processing
        viewModelScope.launch {
            withContext(MyDispatchers.io) {
                try{
                    val bitcoin = repository.getCurrencyByName("bitcoin")
                    if (bitcoin.isSuccessful){
                        val data = bitcoin.body()?.data
                        withContext(MyDispatchers.main){
                            _uiState.value = UIState.Result("${data?.id} ${data?.rateUsd}")
                        }
//                        _uiState.postValue(UIState.Result("${data?.id} ${data?.rateUsd}"))
                    }else{
                        withContext(MyDispatchers.main){
                            _uiState.value = UIState.Error("Error code ${bitcoin.code()}")
                        }
//                        _uiState.postValue(UIState.Error("Error code ${bitcoin.code()}"))
                    }
                }catch (e: Exception){
                    withContext(MyDispatchers.main){
                        _uiState.value = UIState.Error(e.localizedMessage)
                    }
//                    _uiState.postValue(UIState.Error(e.localizedMessage))
                }
//                try {
//                    val response = repo.getCurrencyByName("bitcoin")
//                    withContext(Dispatchers.Main) {
//                        _uiState.postValue(
//                            UIState.Result(response))
//                    }
//                }catch (e: Throwable){
//                    withContext(Dispatchers.Main){
//                        _uiState.postValue(UIState.Error(e.localizedMessage))
//                    }
//                }
            }
        }
    }
    sealed class UIState {
        object Empty:UIState()
        object Processing:UIState()
        data class Result(val title: String): UIState()
        //        class Result(val title:Response<BitcoinResponce>):UIState()
        data class Error(val description: String) : UIState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApplication).repo
                MyViewModel(myRepository)
            }
        }
    }
}