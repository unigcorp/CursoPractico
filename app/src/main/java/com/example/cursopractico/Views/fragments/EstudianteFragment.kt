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
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.repository.db.DatabaseHelper
import com.example.cursopractico.R
import com.example.cursopractico.Views.adapters.AdapterEstudiante
import com.example.cursopractico.Views.interfaces.InterfaceDialog
import com.example.cursopractico.models.Estudiante
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EstudianteFragment : Fragment(), InterfaceDialog {

    private  lateinit var btnFloat:FloatingActionButton
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var txtPrueba:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var estudianteArratList:ArrayList<Estudiante>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_estudiante, container, false)
        btnFloat = view.findViewById(R.id.idfloatingActionButton)
        txtPrueba = view.findViewById(R.id.txtprueba)
        recyclerView = view.findViewById(R.id.idrecycler)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        estudianteArratList = arrayListOf()

        databaseHelper = DatabaseHelper(requireContext())
        btnFloat.setOnClickListener {
            openDialog()
          //  databaseHelper.deleteDatabase(requireContext(),"myBase.db")
        }
        mostrarEstudiantes()
        return view
    }

    private fun mostrarEstudiantes() {
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        var datos:String=""
        estudianteArratList.clear()
        val cursor = db.rawQuery("SELECT * FROM estudiante",null)
        if(cursor.moveToFirst()){
            do {
                /*txtPrueba.text = cursor.getInt(0).toString()+":  "
                txtPrueba.text = cursor.getString(1).toString()+" :"
                txtPrueba.text = cursor.getString(2).toString()+"\n"*/
                datos = datos+cursor.getInt(0).toString()+":  "+cursor.getString(1).toString()+" :"+ cursor.getString(2).toString()+"\n"
                estudianteArratList.add(Estudiante(cursor.getInt(0).toString(),cursor.getString(1).toString(),cursor.getString(2).toString(),cursor.getString(3).toString()))

            }while (cursor.moveToNext())
           // txtPrueba.setText(datos)
            setupList(estudianteArratList)
        }

    }

    private fun setupList(estudianteArratList: ArrayList<Estudiante>) {
        recyclerView.adapter = AdapterEstudiante(databaseHelper,requireContext(),this,estudianteArratList)
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

    override fun onClickDialog(position: Int) {
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
        nombreEst.setText(estudianteArratList.get(position).nombre)
        apellidoEst.setText(estudianteArratList.get(position).apellido)
        carnetEst.setText(estudianteArratList.get(position).carnet)

        btm_cancelat.setOnClickListener {
            dialog.dismiss()
        }
        btn_guardar.setOnClickListener {
            if(nombreEst.text.toString().isEmpty() && apellidoEst.text.toString().isEmpty() && carnetEst.text.toString().isEmpty()){
                Toast.makeText(context, "DEBE COMPLETAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show()
            }else{
                databaseHelper.actualizarEstudiante(estudianteArratList.get(position).id_est,nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString())
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