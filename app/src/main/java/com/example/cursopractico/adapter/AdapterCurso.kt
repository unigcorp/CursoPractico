package com.example.cursopractico.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.DatabaseHelper
import com.example.cursopractico.interfa.InterfaceDialog
import com.example.cursopractico.R
import com.example.cursopractico.model.Curso

class AdapterCurso(private val context: Context, private val interfaceDialog: InterfaceDialog, private val cursoList: ArrayList<Curso>):RecyclerView.Adapter<AdapterCurso.MyViewCurso>() {


    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewCurso {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_curso,parent,false)
        return  MyViewCurso(view)
    }

    override fun getItemCount(): Int {
       return cursoList.size
    }

    override fun onBindViewHolder(holder: MyViewCurso, position: Int) {
        val currentItem = cursoList[position]
        holder.nombre_curso.setText(currentItem.nombre_curso)
        holder.btn_eliminar.setOnClickListener {
            openDialog(position,currentItem.nombre_curso)
        }
        holder.btn_editar.setOnClickListener {
            interfaceDialog.onClickDialog(position)
        }
    }

    private fun openDialog(position: Int, nombreCurso: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmacion")
        builder.setMessage("Esta seguro que desea eliminar "+nombreCurso+" ?")
        builder.setPositiveButton("Si "){dialog,which->
            eliminarCurso(position)
        }
        builder.setNegativeButton("No ",null)
        val  dialog = builder.create()
        dialog.show()

    }

    private fun eliminarCurso(position: Int) {
        databaseHelper = DatabaseHelper(context)
        databaseHelper.eliminarCurso(cursoList.get(position).id_curso)
        cursoList.removeAt(position)
        notifyDataSetChanged()
    }

    class MyViewCurso(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nombre_curso:TextView = itemView.findViewById(R.id.id_nombre_curso)
        val btn_editar:Button = itemView.findViewById(R.id.id_btn_editar)
        val btn_eliminar:Button = itemView.findViewById(R.id.id_btn_eliminar)
    }
}