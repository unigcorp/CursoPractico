package com.example.cursopractico

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context:Context):SQLiteOpenHelper(context,NOMBRE_BASEDEDATOS,null, VERSIONBASEDATOS) {
    companion object{
        //TABLA ESTUDIANTE
        private const val NOMBRE_BASEDEDATOS="myBase.db"
        private const val VERSIONBASEDATOS=1
        private const val NOMBRE_TABLA="estudiante"
        private const val ID_ESTUDIANTE="id_est"
        private const val NOMBRE_ESTUDIANTE="nombre_est"
        private const val APELLIDO_ESTUDIANTE="apellido_est"
        private const val CARNET_ESTUDIANTE="carnet_est"

        //TABLA CURSO

        private const val NOMBRE_TABLA2="curso"
        private const val ID_CURSO="id_curso"
        private const val NOMBRE_CURSO="nombre_curso"
        //TABLA MATRICULA
        private const val NOMBRE_TABLA3="matricula"
        private const val ID_MATRICULA="id_matricula"
        private const val ID_ESTUDIANTE_FK="id_est"
        private const val ID_CURSO_FK="id_curso"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table_student = ("CREATE TABLE $NOMBRE_TABLA ( $ID_ESTUDIANTE INTEGER PRIMARY KEY AUTOINCREMENT, $NOMBRE_ESTUDIANTE TEXT, $APELLIDO_ESTUDIANTE TEXT, $CARNET_ESTUDIANTE TEXT")
        val crete_table_course = ("CREATE TABLE $NOMBRE_TABLA2 ( $ID_CURSO INTEGER PRIMARY KEY AUTOINCREMENT , $NOMBRE_CURSO TEXT)")
        val create_table_student_course=("CREATE TABLA $NOMBRE_TABLA3 ( $ID_MATRICULA INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$ID_ESTUDIANTE_FK INTEGER," +
                "$ID_CURSO_FK INTEGER)," +
                "FOREIGN KEY($ID_ESTUDIANTE_FK) REFERENCE $NOMBRE_TABLA($ID_ESTUDIANTE)," +
                "FOREIGN KEY($ID_CURSO_FK) REFERENCE $NOMBRE_TABLA2($ID_CURSO)")
        //val create_table_student = ("CREATE TABLE estudiante ( id_est INTEGER PRIMARY KEY AUTOINCREMENT, nombre_est TEXT,apellido_est TEXT,carnet_est TEXT)")
        db!!.execSQL(create_table_student)
        db!!.execSQL(crete_table_course)
        db.execSQL(create_table_student_course)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
        db!!.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA2")
    }
    //MATRICULA
    fun insertarMatricula(id_matricula:String,idEst:String,id_curso: String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(ID_ESTUDIANTE_FK,idEst)
        valores.put(ID_CURSO_FK,id_curso)
        db.update(NOMBRE_TABLA3,valores,"id_matricula=?", arrayOf(id_matricula))
        db.close()
    }
    fun actualizarMatricula( id_matricula: String,id_est:String,id_curso:String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(ID_ESTUDIANTE_FK,id_est)
        valores.put(ID_CURSO_FK,id_curso)
        db.update(NOMBRE_TABLA3,valores,"id_matricula=?", arrayOf(id_matricula))
        db.close()
    }
    fun eliminarMatricula(id_matricula:String){
        val db = writableDatabase
        db.delete(NOMBRE_TABLA3,"id_curso=?", arrayOf(id_matricula))
        db.close()
    }
   // FUNCIONES PARA CURSO
    fun insertarCurso(id_curso:String,nombreCurso: String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_ESTUDIANTE,nombreCurso)
        db.update(NOMBRE_TABLA2,valores,"id_curso=?", arrayOf(id_curso))
        db.close()
    }
    fun actualizarCurso(id_curso:String,nombreCurso: String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_ESTUDIANTE,nombreCurso)
        db.update(NOMBRE_TABLA2,valores,"id_curso=?", arrayOf(id_curso))
        db.close()
    }
    fun eliminarCurso(id_curso:String){
        val db = writableDatabase
        db.delete(NOMBRE_TABLA2,"id_curso=?", arrayOf(id_curso))
        db.close()
    }
    //FUNCIONES PARA ESTUDIANTE
    fun insertarEstudiante(nombreEst:String,apellidoEst:String,carnetEst:String): Long {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_ESTUDIANTE,nombreEst)
        valores.put(APELLIDO_ESTUDIANTE,apellidoEst)
        valores.put(CARNET_ESTUDIANTE,carnetEst)
        val result = db.insert(NOMBRE_TABLA,null,valores)
        return result

    }

    fun actualizarEstudinate(idEst:String,nombreEst: String,apellidoEst: String,carnetEst: String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_ESTUDIANTE,nombreEst)
        valores.put(APELLIDO_ESTUDIANTE,apellidoEst)
        valores.put(CARNET_ESTUDIANTE,carnetEst)
        db.update(NOMBRE_TABLA,valores,"id_est=?", arrayOf(idEst))
        db.close()
    }
    fun eliminarEstudiante(idEst:String){
        val db = writableDatabase
        db.delete(NOMBRE_TABLA,"id_est=?", arrayOf(idEst))
        db.close()
    }
    //FNCION PARA ELIMINAR BASE DE DATOS
    fun eliminaBaseDeDatos(context: Context,nombreBaseDatos:String){
        val ruta = context.getDatabasePath(nombreBaseDatos)
        if(ruta.exists()){
            context.deleteDatabase(nombreBaseDatos)
        }
    }

}