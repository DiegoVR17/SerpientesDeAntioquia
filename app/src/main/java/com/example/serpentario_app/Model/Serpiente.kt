package com.example.serpentario_app.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "tabla_serpiente")
class Serpiente(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "especie") val especie: String = "",
    @ColumnInfo(name = "tipo") val tipo: String = "",
    @ColumnInfo(name = "foto") val urlFoto: URL
)