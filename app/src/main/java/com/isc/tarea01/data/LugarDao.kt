package com.isc.tarea01.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.isc.tarea01.model.Lugar


@Dao
interface LugarDao {

    //Para obtener la lista de lugares
    @Query("select * from lugar")
    fun getAllData(): LiveData<List<Lugar>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugar: Lugar)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateLugar(lugar: Lugar)

    @Delete
    suspend fun deleteLugar(lugar: Lugar)

}