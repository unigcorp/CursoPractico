package com.example.cursopractico.Views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.R
import com.example.cursopractico.models.Matricula

class AdapterMatricula(private val matriculaList:ArrayList<Matricula>):RecyclerView.Adapter<AdapterMatricula.MyViewMatricula>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewMatricula {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matricula,parent,false)
        return  MyViewMatricula(view)
    }

    override fun getItemCount(): Int {
        return matriculaList.size
    }

    override fun onBindViewHolder(holder: MyViewMatricula, position: Int) {
        val currenItem = matriculaList[position]
        holder.nombreEstudiante.text = currenItem.nombreEst
        holder.cursoEstudiante.text = currenItem.nombreCurso
    }
    class MyViewMatricula(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nombreEstudiante:TextView = itemView.findViewById(R.id.id_nombre)
        val cursoEstudiante:TextView = itemView.findViewById(R.id.id_curso)
    }
}