package com.example.cursopractico

import android.app.Dialog
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.adapter.AdapterMatricula
import com.example.cursopractico.model.Curso
import com.example.cursopractico.model.Estudiante
import com.example.cursopractico.model.Matricula
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EstudianteCursoFragment : Fragment() {

   private lateinit var recyclerView: RecyclerView
   private lateinit var databaseHelper: DatabaseHelper
   private lateinit var matriculaList:ArrayList<Matricula>
   private lateinit var btn_float:FloatingActionButton
   private lateinit var estudianteList:ArrayList<Estudiante>
   private lateinit var cursoList:ArrayList<Curso>

   private var id_estudiante:String=""
    private var id_curso:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_estudiante_curso, container, false)

        recyclerView = view.findViewById(R.id.idRecycler)
        btn_float = view.findViewById(R.id.idfloatingActionButton)
        databaseHelper = DatabaseHelper(requireContext())
        recyclerView.layoutManager = GridLayoutManager(context,2)
        matriculaList = arrayListOf()
        estudianteList = arrayListOf()
        cursoList = arrayListOf()
        btn_float.setOnClickListener {
            openDialog()

        }
        mostrarEstudiantes()
        mostrarCursos()
        mostrarMatriculados()
        return  view
    }

    private fun mostrarMatriculados() {
        matriculaList.clear()
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT estudiante.nombre_est,curso.nombre_curso FROM estudiante,curso,matricula WHERE estudiante.id_est = matricula.id_est AND matricula.id_curso = curso.id_curso",null)
        if (cursor.moveToFirst()){
            do {
                matriculaList.add(Matricula("","","",cursor.getString(0).toString(),cursor.getString(1).toString()))
            }while (cursor.moveToNext())
            setup(matriculaList)
        }
    }

    private fun setup(matriculaList: ArrayList<Matricula>) {
        recyclerView.adapter = AdapterMatricula(matriculaList)
    }

    private fun mostrarCursos() {
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM estudiante",null)
        if(cursor.moveToFirst()){
            do {
                estudianteList.add(Estudiante(cursor.getInt(0).toString(),cursor.getString(1).toString()))
            }while (cursor.moveToNext())
        }
    }

    private fun mostrarEstudiantes() {
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM curso",null)
        if (cursor.moveToFirst()){
            do {
                cursoList.add(Curso(cursor.getInt(0).toString(),cursor.getString(1).toString()))
            }while (cursor.moveToNext())
        }
    }

    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_estudiante_curso)
        val ancho = ViewGroup.LayoutParams.MATCH_PARENT
        val alto = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(ancho,alto)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val id_estu_spinner:Spinner = dialog.findViewById(R.id.id_spinner_est)
        val id_curso_spinner:Spinner = dialog.findViewById(R.id.id_spinner_curso)
        val btnAgregar:Button = dialog.findViewById(R.id.id_btn_guardar)
        val btnCerrar:Button = dialog.findViewById(R.id.id_btn_cancelar)
        val adaptador_est = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,estudianteList)
        id_estu_spinner.adapter = adaptador_est
        val adaptador_curso = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,cursoList)
        id_curso_spinner.adapter=adaptador_curso
        id_estu_spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "ID "+estudianteList.get(position).id_est, Toast.LENGTH_SHORT).show()
                id_estudiante = estudianteList.get(position).id_est
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        id_curso_spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                id_curso = cursoList.get(position).id_curso
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }
        btnAgregar.setOnClickListener {
            if (id_curso.isEmpty() && id_estudiante.isEmpty()){
                Toast.makeText(context, "NO SE PUDO MATRICULAR", Toast.LENGTH_SHORT).show()
            }else{
                databaseHelper.insertarMatricula(id_estudiante,id_curso)
                Toast.makeText(context, "SE MATRICULO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
                mostrarMatriculados()
                dialog.dismiss()
            }
        }


        dialog.show()
    }


}