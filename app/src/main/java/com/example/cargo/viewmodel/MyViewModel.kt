package com.example.cargo.viewmodel


import androidx.lifecycle.*
import androidx.paging.*
import com.example.cargo.api.ShoppingApi
import com.example.cargo.paginate.ShoppingPaginate
import com.example.cargo.repo.AuthRepository
import com.example.cargo.utils.ExtraFile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val shoppingApi: ShoppingApi
) : ViewModel() {
    fun createUserAccount(email: String, password: String) =
        authRepository.createUserAccount(email, password).asLiveData()

    fun userLogin(email: String, password: String) =
        authRepository.userLogin(email, password).asLiveData()

     val flow = Pager(
        PagingConfig(
            pageSize = ExtraFile.Load_size,
            enablePlaceholders = false
        )
    ) {
        ShoppingPaginate(shoppingApi)
    }.flow.cachedIn(viewModelScope)

}