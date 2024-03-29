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


        private const val NOMBRE_TABLA1="curso"
        private const val ID_CURSO = "id_curso"
        private const val NOMBRE_CURSO="nombre_curso"

        private const val NOMBRE_TABLA2="matricula"
        private const val MATRICULA_ID="id_matricula"
        private const val ID_ESTUDIANTE_FK="id_est"
        private const val ID_CURSO_FK = "id_curso"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val create_table_student = ("CREATE TABLE $NOMBRE_TABLA ( $ID_ESTUDIANTE INTEGER PRIMARY KEY AUTOINCREMENT, $NOMBRE_ESTUDIANTE TEXT, $APELLIDO_ESTUDIANTE TEXT, $CARNET_ESTUDIANTE TEXT)")
        val create_table_course = ("CREATE TABLE $NOMBRE_TABLA1 ( $ID_CURSO INTEGER PRIMARY KEY AUTOINCREMENT, $NOMBRE_CURSO TEXT)")
        val create_table_studen_course=("CREATE TABLE $NOMBRE_TABLA2 " +
                "($MATRICULA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$ID_ESTUDIANTE_FK INTEGER," +
                "$ID_CURSO_FK INTEGER," +
                "FOREIGN KEY($ID_ESTUDIANTE_FK) REFERENCES $NOMBRE_TABLA($ID_ESTUDIANTE)," +
                "FOREIGN KEY ($ID_CURSO_FK) REFERENCES $NOMBRE_TABLA1($ID_CURSO))")
        //val create_table_student = ("CREATE TABLE estudiante ( id_est INTEGER PRIMARY KEY AUTOINCREMENT, nombre_est TEXT,apellido_est TEXT,carnet_est TEXT)")
        db.execSQL(create_table_student)
        db.execSQL(create_table_course)
        db.execSQL(create_table_studen_course)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
        db.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA1")
        db.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA2")
        onCreate(db)
    }

    fun insertarMatricula(id_estudiante:String,id_curso: String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(ID_ESTUDIANTE_FK,id_estudiante)
        valores.put(ID_CURSO_FK,id_curso)
        db.insert(NOMBRE_TABLA2,null,valores)
        db.close()
    }
    fun eliminarMatricula(id_matricula:String){
        val db = writableDatabase
        db.delete(NOMBRE_TABLA2,"id_matricula=?", arrayOf(id_matricula))
        db.close()
    }
    fun actualizarMatricula(id_matricula: String,id_estudiante: String,id_curso: String){
        val db = writableDatabase
        val valores= ContentValues()
        valores.put(ID_ESTUDIANTE_FK,id_estudiante)
        valores.put(ID_CURSO_FK,id_curso)
        db.update(NOMBRE_TABLA2,valores,"id_matricula=?", arrayOf(id_matricula))
        db.close()
    }
    fun insertarEstudiante(nombreEst:String,apellidoEst:String,carnetEst:String) {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_ESTUDIANTE,nombreEst)
        valores.put(APELLIDO_ESTUDIANTE,apellidoEst)
        valores.put(CARNET_ESTUDIANTE,carnetEst)
        db.insert(NOMBRE_TABLA,null,valores)
        db.close()

    }


    fun actualizarEstudiante(id:String,nombreEst:String,apellidoEst:String,carnetEst:String) {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_ESTUDIANTE,nombreEst)
        valores.put(APELLIDO_ESTUDIANTE,apellidoEst)
        valores.put(CARNET_ESTUDIANTE,carnetEst)
        db.update(NOMBRE_TABLA,valores,"id_est=?", arrayOf(id.toString()))
        db.close()

    }
    fun eliminarEstudiante(id:String) {
        val db = writableDatabase

        db.delete(NOMBRE_TABLA,"id_est=?", arrayOf(id.toString()))
        db.close()


    }
    fun insertarCurso(nombreCurso:String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_CURSO,nombreCurso)
        db.insert(NOMBRE_TABLA1,null,valores)
    }
    fun actualizarCurso(id_curso:String,nombre:String){
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(NOMBRE_CURSO,nombre)
        db.update(NOMBRE_TABLA1,valores,"id_curso=?", arrayOf(id_curso.toString()))
        db.close()
    }
    fun eliminarCurso(id_curso:String){
        val db = writableDatabase
        db.delete(NOMBRE_TABLA1,"id_curso=?", arrayOf(id_curso.toString()))
        db.close()
    }
    fun deleteDatabase(context: Context, databaseName: String) {
        val dbFile = context.getDatabasePath(databaseName)
        if (dbFile.exists()) {
            context.deleteDatabase(databaseName)
        }
    }
}