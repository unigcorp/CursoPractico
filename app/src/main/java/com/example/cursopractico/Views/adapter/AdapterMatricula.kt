package com.example.cursopractico.Views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.R
import com.example.cursopractico.Views.interfaces.InterfaceDialog
import com.example.cursopractico.models.Matricula
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdapterMatricula(private val context: Context, private val interfaceDialog: InterfaceDialog, private val matriculaList:ArrayList<Matricula>):RecyclerView.Adapter<AdapterMatricula.MyViewMatricula>() {

    private lateinit var referencia_est: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewMatricula {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matricula,parent,false)
        return  MyViewMatricula(view)
    }

    override fun getItemCount(): Int {
        return matriculaList.size
    }

    override fun onBindViewHolder(holder: MyViewMatricula, position: Int) {
        referencia_est = FirebaseDatabase.getInstance().getReference("estudiante")
        val currenItem = matriculaList[position]
        holder.nombreEstudiante.text = currenItem.nombre
        holder.cursoEstudiante.text = currenItem.nombre_curso
        holder.id_elimina.setOnClickListener {
//            referencia_est.child(currenItem.id_estudiante).child("curso").child(currenItem.id_curso).removeValue()
//            matriculaList.removeAt(position)
//            notifyDataSetChanged()
            Toast.makeText(context, "hola desde eliminar", Toast.LENGTH_SHORT).show()
        }
    }
    class MyViewMatricula(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nombreEstudiante:TextView = itemView.findViewById(R.id.id_nombre)
        val cursoEstudiante:TextView = itemView.findViewById(R.id.id_curso)
        val id_elimina:Button = itemView.findViewById(R.id.id_btn_eliminar)
    }
}