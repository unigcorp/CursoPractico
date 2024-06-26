package com.example.cursopractico.Views.fragments

import android.app.Dialog
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.repository.db.DatabaseHelper
import com.example.cursopractico.R
import com.example.cursopractico.Views.adapter.AdapterCurso
import com.example.cursopractico.Views.interfaces.InterfaceDialog
import com.example.cursopractico.models.Curso
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CursoFragment : Fragment(), InterfaceDialog {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cursoList: ArrayList<Curso>
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var btnFloat:FloatingActionButton
    private lateinit var referencia_curso: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_curso, container, false)
        referencia_curso = FirebaseDatabase.getInstance().getReference("curso")
        recyclerView = view.findViewById(R.id.id_recyclerView)
        btnFloat = view.findViewById(R.id.idfloatingActionButton)
        databaseHelper = DatabaseHelper(requireContext())
        recyclerView.layoutManager = GridLayoutManager(context,2)
        cursoList = arrayListOf()
      //


        btnFloat.setOnClickListener {
            openDialog()
           // databaseHelper.deleteDatabase(requireContext(),"myBase.db")
        }
        //LISTAR CURSOS CON FIREBASE
        mostrarCursosFirebase()
        //LISTAR CURSOS CONSQLITE
       // listarCursos()
        return  view
    }

    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_curso)
        val ancho = ViewGroup.LayoutParams.MATCH_PARENT
        val alto = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(ancho,alto)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnCancelar:Button = dialog.findViewById(R.id.id_btn_cancelar)
        val btnGuardar:Button = dialog.findViewById(R.id.id_btn_guardar)
        val nombreCurso:EditText= dialog.findViewById(R.id.id_nombre_curso)
        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        btnGuardar.setOnClickListener {
            if(nombreCurso.text.toString().isEmpty()){
                Toast.makeText(context, "DEBE COMPLETAR LOS CAMPOS", Toast.LENGTH_LONG).show()
            }else{
                databaseHelper.insertarCurso(nombreCurso.text.toString())
                dialog.dismiss()
                Toast.makeText(context, "DATOS INSERTADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show()
                listarCursos()
            }
        }
        dialog.show()
    }



    private fun listarCursos() {
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        cursoList.clear()
        val cursor = db.rawQuery("SELECT * FROM curso",null)
        if (cursor.moveToFirst()){
            do {
                cursoList.add(Curso(cursor.getInt(0).toString(),cursor.getString(1).toString()))

            }while (cursor.moveToNext())
            setupList(cursoList)
        }
    }

    private fun setupList(cursoList: ArrayList<Curso>) {
        recyclerView.adapter = AdapterCurso(requireContext(),this,cursoList)
    }

    override fun onClickDialog(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_curso)
        val ancho = ViewGroup.LayoutParams.MATCH_PARENT
        val alto = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(ancho,alto)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnCancelar:Button = dialog.findViewById(R.id.id_btn_cancelar)
        val btnGuardar:Button = dialog.findViewById(R.id.id_btn_guardar)
        val nombreCurso:EditText= dialog.findViewById(R.id.id_nombre_curso)
        nombreCurso.setText(cursoList.get(position).nombre_curso)
        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        btnGuardar.setOnClickListener {
            if(nombreCurso.text.toString().isEmpty()){
                Toast.makeText(context, "DEBE COMPLETAR LOS CAMPOS", Toast.LENGTH_LONG).show()
            }else{
                editarConFirebase()

//                databaseHelper.actualizarCurso(cursoList.get(position).id_curso,nombreCurso.text.toString())
//                dialog.dismiss()
//                Toast.makeText(context, "DATOS ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show()
//                listarCursos()
            }
        }
        dialog.show()
    }

    private fun editarConFirebase() {
        TODO("Not yet implemented")
    }

    private fun mostrarCursosFirebase() {
        referencia_curso.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    cursoList.clear()
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(Curso::class.java)
                        cursoList.add(data!!)
                    }
                    setupList(cursoList)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}