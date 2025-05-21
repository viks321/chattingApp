package com.example.onetoone.homeScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.models.LoginModel
import com.example.onetoone.repositary.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(val repository: Repository): ViewModel() {

    val allMemberLiveData : StateFlow<List<LoginModel>>
        get() = repository.allMemberMutableLiveData

    fun getAllMembers(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllMemberFromFirebase()
        }
    }

}