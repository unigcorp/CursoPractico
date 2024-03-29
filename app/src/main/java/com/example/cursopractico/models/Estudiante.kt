package com.example.cursopractico.models

class Estudiante(val id_est:String, val nombre:String, val apellido:String="",val carnet:String="") {
    override fun toString() = nombre
}