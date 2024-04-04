package com.example.cursopractico.model

class Estudiante(
    val id_est:String="", val nombre:String="", val apellido:String="",
    val carnet:String="",
    val curso: HashMap<String,Boolean>? = null) {


    override fun toString() = nombre
}