package com.example.serpentario_app.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "tabla_serpiente")
class Serpiente(
    //@PrimaryKey @ColumnInfo(name = "id") val id: int = "",
    @ColumnInfo(name = "Nombre") val nombre: String = "",
    @ColumnInfo(name = "Tamaño") val tamano: String = "",
    @ColumnInfo(name = "foto") val urlFoto: String= "",
    @ColumnInfo(name = "Tipo") val tipo: String= "",
    @ColumnInfo(name = "Estado") val estado: String = "",
    @ColumnInfo(name = "Ambiente") val ambiente: String = "",
    @ColumnInfo(name = "Lugar") val lugar: String = "",
    @ColumnInfo(name = "Observaciones") val observaciones: String= "",
    @ColumnInfo(name = "DateTime") val date_time:String = "",
    @ColumnInfo(name = "Latitud") val latitud: String = "",
    @ColumnInfo(name = "Longitud") val longitud: String = ""

)