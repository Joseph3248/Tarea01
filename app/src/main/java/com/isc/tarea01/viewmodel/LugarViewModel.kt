package com.isc.tarea01.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.isc.tarea01.data.LugarDao
import com.isc.tarea01.model.Lugar
import com.isc.tarea01.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {
    val getAllData: MutableLiveData<List<Lugar>>
    private val repository: LugarRepository = LugarRepository(LugarDao())

    init {

        getAllData = repository.getAllData
    }


    fun addLugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) { repository.addLugar(lugar) }
    }

    fun updateLugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) { repository.updateLugar(lugar) }

    }

    fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteLugar(lugar) }

    }


}