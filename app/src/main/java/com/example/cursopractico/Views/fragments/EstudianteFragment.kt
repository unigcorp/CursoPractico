package com.example.cursopractico.Views.fragments

import android.app.Dialog
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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cursopractico.Constantes
import com.example.cursopractico.R
import com.example.cursopractico.Views.MainActivity
import com.example.cursopractico.Views.adapter.AdapterEstudiante
import com.example.cursopractico.Views.interfaces.InterfaceDialog
import com.example.cursopractico.models.Estudiante
import com.example.cursopractico.repository.db.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.locks.LockSupport


class EstudianteFragment(private val mainActivity: MainActivity) : Fragment(), InterfaceDialog {

    private  lateinit var btnFloat:FloatingActionButton
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var txtPrueba:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var estudianteArratList:ArrayList<Estudiante>
    private lateinit var adapter : AdapterEstudiante


    private lateinit var referencia:DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_estudiante, container, false)
        referencia = FirebaseDatabase.getInstance().getReference("estudiante")


        btnFloat = view.findViewById(R.id.idfloatingActionButton)
        txtPrueba = view.findViewById(R.id.txtprueba)
        recyclerView = view.findViewById(R.id.idrecycler)
        recyclerView.layoutManager = GridLayoutManager(mainActivity,2)
        estudianteArratList = arrayListOf()
        databaseHelper = DatabaseHelper(requireContext())
        btnFloat.setOnClickListener {
            openDialog()
            //databaseHelper.deleteDatabase(requireContext(),"myBase.db")
        }

        //LISTAR CON PETICIONES HTTP
        mostrarEstudiantesHttp()
        //LISTAR CON FIREBASE
     //   mostrarEstudiantesFirebase()
        //LISTAR ESTTUDIANTES EN SQLITE
        //mostrarEstudiantes()
        return view
    }
    private fun mostrarEstudiantesHttp(){
        val request = Volley.newRequestQueue(context)
        val stringRequest  = object : StringRequest(Request.Method.GET,Constantes.DIRECCION_REMOTA+"mostrar_estudiantes.php",Response.Listener {
            response->
            Toast.makeText(context, "RESPONSE "+response, Toast.LENGTH_SHORT).show()
            try {
                estudianteArratList.clear()
                val objeto = JSONObject(response)
                val arrays = objeto.getJSONArray("results")
                for (i in 0 until arrays.length()){
                    val item = arrays.getJSONObject(i)
                    val estudiante = Estudiante(item.getString("id_est"),item.getString("nombre"),item.getString("apellido"),item.getString("carnet"))
                    estudianteArratList.add(estudiante)
                }
                setupList(estudianteArratList)


            }catch (e:JSONException){
                Toast.makeText(context, "exception "+e.message, Toast.LENGTH_SHORT).show()
            }
        },Response.ErrorListener {
            error->
        })
        {}
        request.add(stringRequest)
    }

    private fun mostrarEstudiantesFirebase() {
        referencia.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    estudianteArratList.clear()
                    for(dataSnap in snapshot.children){
                        val estudiante = dataSnap.getValue(Estudiante::class.java)
//                        val hashMap = dataSnap.child("curso").getValue()
//                        val id_est = dataSnap.child("id_est").getValue()
//                        val nombre = dataSnap.child("nombre").getValue()
//                        val apellido = dataSnap.child("apellido").getValue()
//                        val carnet = dataSnap.child("carnet").getValue()
//                        val estudiante = Estudiante(id_est.toString(),nombre.toString(),apellido.toString(),carnet.toString(),hashMap)
                        estudianteArratList.add(estudiante!!)
//                        Toast.makeText(context, "DATA "+hashMap, Toast.LENGTH_SHORT).show()
                    }
                    //recyclerView.adapter = AdapterEstudiante(databaseHelper,requireContext(),this@EstudianteFragment,estudianteArratList)
                    setupList(estudianteArratList)

//                    adapter = AdapterEstudiante(databaseHelper,requireContext(),this@EstudianteFragment,estudianteArratList)
//                    recyclerView.adapter = adapter
//                    adapter.notifyDataSetChanged()


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setupList(estudianteArratList: ArrayList<Estudiante>) {
        adapter = AdapterEstudiante(databaseHelper,mainActivity,this,estudianteArratList)


        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    /*private fun mostrarEstudiantes() {
       // databaseHelper.deleteDatabase(requireContext(),"myBase.db")
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

    }*/



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

                //INSERTAR PARA PETICIONES HTTP
                insertarHttp(nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString())
                //INSERTAR PARA FIREBASE
               // insertarFirebase(nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString())
                nombreEst.text.clear()
                apellidoEst.text.clear()
                carnetEst.text.clear()
                dialog.dismiss()
                //INSERTAR PARA SQLITE
                /*databaseHelper.insertarEstudiante(nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString())
                nombreEst.text.clear()
                apellidoEst.text.clear()
                carnetEst.text.clear()
                dialog.dismiss()*/
              //  mostrarEstudiantes()
            }
        }

        
        dialog.show()
    }
    private  fun insertarHttp(nombre:String,apellido: String,carnet: String){
        val request = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,Constantes.DIRECCION_REMOTA+"ad_estudiantes.php",Response.Listener {
            response->
            Toast.makeText(context, "SE INSERTARON LOS DATOS CORRECTAMENTE", Toast.LENGTH_SHORT).show()
            mostrarEstudiantesHttp()
        },Response.ErrorListener {
            error->
            Toast.makeText(context, "OCURRIO ALGUN ERROR", Toast.LENGTH_SHORT).show()
        }){
            override fun getParams(): Map<String, String>? {
                val parametros = HashMap<String,String>()
                parametros.put("nombre",nombre)
                parametros.put("apellido",apellido)
                parametros.put("carnet",carnet)
                return parametros
            }
        }
        request.add(stringRequest)
    }
    private fun insertarFirebase(nombre: String, apellido: String, carnet: String) {
        val id_est = referencia.push().key!!
        val estudiante = Estudiante(id_est,nombre,apellido,carnet)
        referencia.child(id_est).setValue(estudiante).addOnSuccessListener {
            Toast.makeText(context, "SE INSERTO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            error->
            Toast.makeText(context, "error "+error.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onClickDialog(position: Int) {
        val dialog = Dialog(mainActivity)
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
                // ACTUALIZAR HTTP
                actualizarHttp(estudianteArratList.get(position).id_est,nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString())
                //ACTUALIZAR EN FIREBASE
               // actualizarFirebase(estudianteArratList.get(position).id_est,nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString(),estudianteArratList.get(position).curso)
                //ACTUALIZAR EN SQLITE
               //databaseHelper.actualizarEstudiante(estudianteArratList.get(position).id_est,nombreEst.text.toString(),apellidoEst.text.toString(),carnetEst.text.toString())
                nombreEst.text.clear()
                apellidoEst.text.clear()
                carnetEst.text.clear()
                dialog.dismiss()
                //mostrarEstudiantes()
            }
        }


        dialog.show()
    }
    private fun actualizarHttp(id_est:String,nombre: String,apellido: String,carnet: String){
        val request = Volley.newRequestQueue(context)
        val stringRequest = object :StringRequest(Request.Method.POST,Constantes.DIRECCION_REMOTA+"ed_estudiantes.php",Response.Listener {
            response->
            Toast.makeText(context, "SE ACTUALIZARON LOS DATOS CORRECTAMENTE", Toast.LENGTH_SHORT).show()
            mostrarEstudiantesHttp()
        },Response.ErrorListener {
            error->
            Toast.makeText(context, "ALGO PASO "+error.message, Toast.LENGTH_SHORT).show()
        })
        {
            override fun getParams(): Map<String, String>? {
                val parametros = HashMap<String,String>()
                parametros.put("id_est",id_est)
                parametros.put("nombre",nombre)
                parametros.put("apellido",apellido)
                parametros.put("carnet",carnet)
                return parametros
            }
        }
        request.add(stringRequest)
    }

    private fun actualizarFirebase(
        idEst: String,
        nombre: String,
        apellido: String,
        carnet: String,
        curso: HashMap<String, Boolean>?
    ) {
        val  estudiante = Estudiante(idEst,nombre,apellido,carnet,curso)
        referencia.child(idEst).setValue(estudiante).addOnSuccessListener {
            Toast.makeText(context, "SE ACTUALIZO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            error->
            Toast.makeText(context, "ERROR "+error.message, Toast.LENGTH_SHORT).show()

        }
    }


}