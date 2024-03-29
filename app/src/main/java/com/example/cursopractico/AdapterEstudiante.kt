package com.example.cursopractico

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterEstudiante(private val databaseHelper: DatabaseHelper,private val context: Context ,private val interfaceDialog: InterfaceDialog,private val estudianteList:ArrayList<Estudiante>):RecyclerView.Adapter<AdapterEstudiante.MyViewEstudiante>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewEstudiante {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_estudiante,parent,false)
        return MyViewEstudiante(v)
    }

    override fun getItemCount(): Int {
        return estudianteList.size
    }

    override fun onBindViewHolder(holder: MyViewEstudiante, position: Int) {
        val items = estudianteList[position]
        holder.nombre_estudiante.setText(items.nombre)
        holder.apellidos_estudiante.setText(items.apellidos)
        holder.btnEditar.setOnClickListener {
            interfaceDialog.onclickDialog(position)
        }
        holder.btnEliminar.setOnClickListener {
            openDialog(position,items.nombre)
        }
    }

    private fun openDialog(position: Int, nombre: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirma!!")
        builder.setMessage("Esta seguro que desea eliminar "+nombre +" ?")
        builder.setPositiveButton("Si "){dialog,which->
            eliminarEstudiante(position)
        }
        builder.setNegativeButton("No",null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun eliminarEstudiante(position: Int) {
        databaseHelper.eliminarEstudiante(estudianteList.get(position).id_est)
        estudianteList.removeAt(position)
        notifyDataSetChanged()

    }

    class MyViewEstudiante(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nombre_estudiante:TextView = itemView.findViewById(R.id.id_nombre)
        val apellidos_estudiante:TextView = itemView.findViewById(R.id.id_apellido)
        val btnEditar:Button = itemView.findViewById(R.id.id_btn_editar)
        val btnEliminar:Button = itemView.findViewById(R.id.id_btn_eliminar)
    }
}