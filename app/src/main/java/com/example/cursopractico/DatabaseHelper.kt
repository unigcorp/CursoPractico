package com.example.cursopractico

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context:Context):SQLiteOpenHelper(context,NOMBRE_BASEDEDATOS,null, VERSIONBASEDATOS) {
    companion object{
        private const val NOMBRE_BASEDEDATOS="myBase.db"
        private const val VERSIONBASEDATOS=1
        private const val NOMBRE_TABLA="estudiante"
        private const val ID_ESTUDIANTE="id_est"
        private const val NOMBRE_ESTUDIANTE="nombre_est"
        private const val APELLIDO_ESTUDIANTE="apellido_est"
        private const val CARNET_ESTUDIANTE="carnet_est"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table_student = ("CREATE TABLE $NOMBRE_TABLA ( $ID_ESTUDIANTE INTEGER PRIMARY KEY AUTOINCREMENT, $NOMBRE_ESTUDIANTE TEXT, $APELLIDO_ESTUDIANTE TEXT, $CARNET_ESTUDIANTE TEXT")
        //val create_table_student = ("CREATE TABLE estudiante ( id_est INTEGER PRIMARY KEY AUTOINCREMENT, nombre_est TEXT,apellido_est TEXT,carnet_est TEXT)")
        db!!.execSQL(create_table_student)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
    }
    fun insertarEstudiante(nombreEst:String,apellidoEst:String,carnetEst:String): Long {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_ESTUDIANTE,nombreEst)
        valores.put(APELLIDO_ESTUDIANTE,apellidoEst)
        valores.put(CARNET_ESTUDIANTE,carnetEst)
        val result = db.insert(NOMBRE_TABLA,null,valores)
        return result

    }

}