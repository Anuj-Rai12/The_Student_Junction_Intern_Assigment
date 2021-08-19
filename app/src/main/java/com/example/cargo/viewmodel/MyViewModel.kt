package com.example.cargo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.cargo.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun createUserAccount(email: String, password: String) =
        authRepository.createUserAccount(email, password).asLiveData()
    fun userLogin(email: String,password: String)=authRepository.userLogin(email,password).asLiveData()
}