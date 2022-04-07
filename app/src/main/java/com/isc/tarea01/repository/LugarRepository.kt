package com.isc.tarea01.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.isc.tarea01.data.LugarDao
import com.isc.tarea01.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    val getAllData : MutableLiveData<List<Lugar>> = lugarDao.getLugares()


    suspend fun addLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }


    suspend fun updateLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }


    suspend fun deleteLugar(lugar: Lugar){
        lugarDao.deleteLugar(lugar)
    }
}