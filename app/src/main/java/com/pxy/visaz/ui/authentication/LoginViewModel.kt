package com.pxy.visaz.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pxy.visaz.core.model.CreatePasswordModel
import com.pxy.visaz.core.model.ErrorModel
import com.pxy.visaz.core.model.LoginModel
import com.pxy.visaz.core.model.SignUpModel
import com.pxy.visaz.domain.interactors.AuthUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val useCase: AuthUseCase) : ViewModel() {

    val loginObserver: MutableLiveData<LoginModel?> = MutableLiveData()
    val signUpObserver: MutableLiveData<SignUpModel?> = MutableLiveData()
    val createPasswordObserver: MutableLiveData<CreatePasswordModel?> = MutableLiveData()
    val errorObserver: MutableLiveData<ErrorModel?> = MutableLiveData()
    val loaderObserver: MutableLiveData<Boolean> = MutableLiveData(false)

    fun doLogin(userName: String, password: String) {
        loaderObserver.value = true
        viewModelScope.launch {
            val result = useCase.login(userName, password)
            loaderObserver.value = false
            if (result.isSuccessful) {
                loginObserver.value = result.model
            } else {
                loginObserver.value = result.model
                errorObserver.value = result.errorModel
            }

        }
    }

    fun doSignUp(name: String, userEmail: String, userPhone: String) {
        loaderObserver.value = true
        viewModelScope.launch {
            val result = useCase.signUp(name, userEmail, userPhone)
            loaderObserver.value = false
            if (result.isSuccessful) {
                signUpObserver.value = result.model
            } else {
                signUpObserver.value = result.model
                errorObserver.value = result.errorModel
            }
        }
    }

    fun createPassword(userEmail: String, otp: String, password: String) {
        loaderObserver.value = true
        viewModelScope.launch {
            val result = useCase.createPassword(userEmail, otp, password)
            loaderObserver.value = false
            if (result.isSuccessful) {
                createPasswordObserver.value = result.model
            } else {
                createPasswordObserver.value = result.model
                errorObserver.value = result.errorModel
            }
        }
    }

    fun clearSignUpObserver() {
        signUpObserver.value = null
    }
}