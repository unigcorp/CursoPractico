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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EstudianteFragment : Fragment() {

    private  lateinit var btnFloat:FloatingActionButton
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var txtPrueba:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_estudiante, container, false)
        btnFloat = view.findViewById(R.id.idfloatingActionButton)

        databaseHelper = DatabaseHelper(requireContext())
        btnFloat.setOnClickListener {
            openDialog()
        }
        mostrarEstudiantes()
        return view
    }

    private fun mostrarEstudiantes() {
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM estudiante",null)
        if(cursor.moveToFirst()){
            do {
                txtPrueba.setText(cursor.getInt(0).toString()+" "+cursor.getString(1).toString()+"\n")

            }while (cursor.moveToNext())
        }

    }

    private fun openDialog() {
         val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_estudiante)
        val ancho = ViewGroup.LayoutParams.MATCH_PARENT
        val alto = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(ancho,alto)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nombreEst:EditText = dialog.findViewById(R.id.id_nombre)
        val apellidoEst:EditText = dialog.findViewById(R.id.id_apellido)
        val carnetEst:EditText = dialog.findViewById(R.id.id_carnet)
        val btn_guardar:Button = dialog.findViewById(R.id.id_btn_guardar)
        val btm_cancelat:Button = dialog.findViewById(R.id.id_btn_cancelar)
        btm_cancelat.setOnClickListener {
            dialog.dismiss()
        }
        btn_guardar.setOnClickListener {
            if(nombreEst.text.toString().isEmpty() && apellidoEst.text.toString().isEmpty() && carnetEst.text.toString().isEmpty()){
                Toast.makeText(context, "DEBE COMPLETAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show()
            }else{
                databaseHelper.insertarEstudiante(nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString())
                nombreEst.text.clear()
                apellidoEst.text.clear()
                carnetEst.text.clear()
                dialog.dismiss()
                mostrarEstudiantes()
            }
        }

        
        dialog.show()
    }


}