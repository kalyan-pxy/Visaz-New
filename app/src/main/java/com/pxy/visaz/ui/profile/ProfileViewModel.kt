package com.pxy.visaz.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pxy.visaz.core.BaseViewModel
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.domain.interactors.VisaUseCase

class ProfileViewModel(private val useCase: VisaUseCase) : BaseViewModel() {

    private val _logoutObserver = MutableLiveData<Boolean>()
    val logoutObserver: LiveData<Boolean> = _logoutObserver

    private val _updateProfileObserver = MutableLiveData<Boolean>()
    val updateProfileObserver: LiveData<Boolean> = _updateProfileObserver

    fun logout() {
        AppPreferenceHelper.logout()
        _logoutObserver.value = true
    }

    /*fun updateProfile(
        name: String,
        phone: String,
        biography: String,
        gender: String,
        websiteUrl: String,
        twitter: String,
        facebook: String,
        linkedin: String,
        youtube: String,
        uriToFilePath: String?
    ) {
        AppPreferenceHelper.student?.let {
            val student = Student(
                studentId = it.studentId,
                email = it.email,
                name = name,
                phone = phone,
                biography = biography,
                gender = gender,
                websiteUrl = websiteUrl,
                twitter = twitter,
                facebook = facebook,
                linkedin = linkedin,
                youtube = youtube,
                profilePicUrl = uriToFilePath
            )
            AppPreferenceHelper.student = student
            _updateProfileObserver.value = true
        }
    }*/
}