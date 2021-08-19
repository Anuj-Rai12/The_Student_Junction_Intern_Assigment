package com.example.cargo.repo

import com.example.cargo.utils.MySealed
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {
    private val authInstance by lazy {
        FirebaseAuth.getInstance()
    }
    fun createUserAccount(email:String,password:String)= flow {
        emit(MySealed.Loading("User Account Is Creating..."))
        val data=try {
            authInstance.createUserWithEmailAndPassword(email, password).await()
            MySealed.Success(null)
        }catch (e:Exception){
            MySealed.Error(null,e)
        }
        emit(data)
    }.flowOn(IO)

    fun userLogin(email: String, password: String)=flow {
        emit(MySealed.Loading("Checking User Info..."))
        val data=try {
            authInstance.signInWithEmailAndPassword(email, password).await()
            MySealed.Success(null)
        }catch (e:Exception){
            MySealed.Error(null,e)
        }
        emit(data)
    }.flowOn(IO)
}