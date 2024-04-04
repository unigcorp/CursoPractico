package com.example.cursopractico.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.R
import com.example.cursopractico.interfa.InterfaceDialog
import com.example.cursopractico.model.Matricula
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdapterMatricula(private val interfaceDialog: InterfaceDialog,private val matriculaList:ArrayList<Matricula>):RecyclerView.Adapter<AdapterMatricula.MyViewMatricula>() {

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
            referencia_est.child(currenItem.id_estudiante).child("curso").child(currenItem.id_curso).removeValue()
            matriculaList.removeAt(position)
            notifyDataSetChanged()
        }
    }
    class MyViewMatricula(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nombreEstudiante:TextView = itemView.findViewById(R.id.id_nombre)
        val cursoEstudiante:TextView = itemView.findViewById(R.id.id_curso)
        val id_elimina:Button = itemView.findViewById(R.id.id_btn_eliminar)
    }
}