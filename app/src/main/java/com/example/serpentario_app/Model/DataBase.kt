package com.example.serpentario_app.Model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Serpiente::class],version = 1)
abstract class DataBase : RoomDatabase(){
    abstract fun SerpienteDao(): SerpienteDao
}