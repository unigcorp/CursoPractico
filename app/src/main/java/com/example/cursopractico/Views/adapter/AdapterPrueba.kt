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
import com.example.cursopractico.models.Matricula
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdapterPrueba(val context: Context, val pruebalista:ArrayList<Matricula>):RecyclerView.Adapter<AdapterPrueba.MyViewPrueba>() {

    private lateinit var referencia_eliminar :DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewPrueba {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matricula,parent,false)
        return MyViewPrueba(view)
    }

    override fun getItemCount(): Int {
        return pruebalista.size
    }

    override fun onBindViewHolder(holder: MyViewPrueba, position: Int) {
        referencia_eliminar = FirebaseDatabase.getInstance().getReference("estudiante")
        val items = pruebalista[position]
        holder.nombre_estudiante.text = items.nombre
        holder.nombre_curso.setText(items.nombre_curso)
        holder.btn_eliminar.setOnClickListener {
            Toast.makeText(context, "Hola desde eliminar $position" , Toast.LENGTH_SHORT).show()
            referencia_eliminar.child(items.id_estudiante).child("curso").child(items.id_curso).removeValue()
            pruebalista.removeAt(position)
            notifyDataSetChanged()

        }
    }
    class MyViewPrueba(item: View):RecyclerView.ViewHolder(item) {
        val nombre_estudiante:TextView = item.findViewById(R.id.id_nombre)
        val nombre_curso:TextView = item.findViewById(R.id.id_curso)
        val btn_eliminar:Button = item.findViewById(R.id.id_btn_eliminar)
    }

}