package com.pxy.visaz.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pxy.visaz.core.model.ErrorModel

open class BaseViewModel() : ViewModel() {

    internal val _loaderObserver = MutableLiveData<Boolean>()
    val loaderObserver: LiveData<Boolean> = _loaderObserver

    internal val _errorObserver = MutableLiveData<ErrorModel?>()
    val errorObserver: LiveData<ErrorModel?> = _errorObserver
}