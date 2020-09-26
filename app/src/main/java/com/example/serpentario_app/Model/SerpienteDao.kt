package com.example.serpentario_app.Model

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface SerpienteDao {
    @Query("SELECT * FROM tabla_serpiente")
    fun getAll():List<Serpiente>

    @Query("SELECT * FROM tabla_serpiente WHERE nombre= :nombre")
    fun findByName(nombre:String): Serpiente

    @Insert
    fun insertAllUser(vararg Serpientes: Serpiente)

    @Insert
    fun insertUser(serpiente: Serpiente)

    @Delete
    fun deleteUser(serpiente: Serpiente)

    @Update
    fun updateUser(serpiente: Serpiente)
}