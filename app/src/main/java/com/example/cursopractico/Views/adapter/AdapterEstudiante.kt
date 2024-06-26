package com.example.cursopractico.Views.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cursopractico.Constantes
import com.example.cursopractico.repository.db.DatabaseHelper
import com.example.cursopractico.R
import com.example.cursopractico.Views.interfaces.InterfaceDialog
import com.example.cursopractico.models.Estudiante
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdapterEstudiante(private var databaseHelper: DatabaseHelper, private var context: Context, private var interfaceDialog: InterfaceDialog, private var estudianteList:ArrayList<Estudiante>):RecyclerView.Adapter<AdapterEstudiante.MyViewEstudiante>() {



    private lateinit var referencia:DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewEstudiante {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estudiante,parent,false)
        return MyViewEstudiante(view)
    }



    override fun onBindViewHolder(holder: MyViewEstudiante, position: Int) {
        val currenItem = estudianteList[position]
        holder.nombreEstudiante.text = currenItem.nombre
        holder.apellidoEstudiante.text = currenItem.apellido
        holder.btnEditar.setOnClickListener {
            interfaceDialog.onClickDialog(position)
        }
        holder.btnEminar.setOnClickListener {
            openDialogDelete(position,currenItem.nombre)
        }
    }

    private fun openDialogDelete(position: Int,nombre:String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmacion")
        builder.setMessage("Esta seguro que desea eliminar a "+nombre +"?")
        builder.setPositiveButton("Si"){dialog,which->
            eliminaEstudiante(position)
        }
        builder.setNegativeButton("No",null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun eliminaEstudiante(position: Int) {

        //ELIMINAR CON PETICIONES HTTP
        val request = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,Constantes.DIRECCION_REMOTA+"el_estudiantes.php",Response.Listener {
            response->
        },Response.ErrorListener {
            error->
        }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros.put("id_est",estudianteList.get(position).id_est)
                return parametros
            }
        }
        request.add(stringRequest)
        estudianteList.removeAt(position)
        //ELIMINAR ESTUDIANTE CON FIREBASE
        /*referencia = FirebaseDatabase.getInstance().getReference("estudiante")
        referencia.child(estudianteList.get(position).id_est).removeValue().addOnSuccessListener {
            Toast.makeText(context, "SE ELIMINO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            error->
            Toast.makeText(context, "ERROR "+error.message, Toast.LENGTH_SHORT).show()
        }*/
        //ELIMINAR ESTUDIANTE CON SQLITE
//        databaseHelper.eliminarEstudiante(estudianteList.get(position).id_est)
//        estudianteList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return  estudianteList.size
    }
    class MyViewEstudiante(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nombreEstudiante:TextView = itemView.findViewById(R.id.id_nombre)
        val apellidoEstudiante:TextView = itemView.findViewById(R.id.id_apellidos)
        val btnEditar:Button = itemView.findViewById(R.id.id_btn_editar)
        val btnEminar:Button=itemView.findViewById(R.id.id_btn_eliminar)

    }
}