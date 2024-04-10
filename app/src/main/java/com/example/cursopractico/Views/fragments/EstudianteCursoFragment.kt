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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cursopractico.repository.db.DatabaseHelper
import com.example.cursopractico.R
import com.example.cursopractico.Views.MainActivity
import com.example.cursopractico.Views.adapter.AdapterMatricula
import com.example.cursopractico.Views.adapter.AdapterPrueba
import com.example.cursopractico.Views.interfaces.InterfaceDialog
import com.example.cursopractico.models.Curso
import com.example.cursopractico.models.Estudiante
import com.example.cursopractico.models.Matricula
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EstudianteCursoFragment( private val mainActivity: MainActivity) : Fragment(),
    InterfaceDialog {

   private lateinit var recyclerView: RecyclerView
   private lateinit var databaseHelper: DatabaseHelper
   private lateinit var matriculaList:ArrayList<Matricula>
   private lateinit var btn_float:FloatingActionButton
   private lateinit var estudianteList:ArrayList<Estudiante>
   private lateinit var cursoList:ArrayList<Curso>
   private lateinit var adapter : AdapterMatricula
   private lateinit var referencia_est: DatabaseReference
    private lateinit var referencia_curso: DatabaseReference
    private lateinit var referencia_matricula: DatabaseReference
   private var id_estudiante:String=""
    private var id_curso:String=""
    var nombre:String=""
    var nombreCurso:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_estudiante_curso, container, false)

        recyclerView = view.findViewById(R.id.idRecycler)
        btn_float = view.findViewById(R.id.idfloatingActionButton)
        referencia_est = FirebaseDatabase.getInstance().getReference("estudiante")
        referencia_curso = FirebaseDatabase.getInstance().getReference("curso")
        referencia_matricula = FirebaseDatabase.getInstance().getReference("matricula")
        databaseHelper = DatabaseHelper(requireContext())
        recyclerView.layoutManager = GridLayoutManager(mainActivity,2)
        matriculaList = arrayListOf()
        estudianteList = arrayListOf()
        cursoList = arrayListOf()
        btn_float.setOnClickListener {
            openDialog()

        }
        //FIREBASE
        mostrarEstudiantesFirebase()
        mostrarCursosFirebase()
        mostrarMatriculadosFirebase()
        //SQLITE
        /*mostrarEstudiantes()
        mostrarCursos()
        mostrarMatriculados()*/
        return  view
    }

    private fun mostrarMatriculadosFirebase() {
        referencia_est.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    matriculaList.clear()
                    for (estudianteSnapshop in snapshot.children){
                        if (estudianteSnapshop.child("curso").getValue() !=null){
                            val nombre_estudiante = estudianteSnapshop.child("nombre").getValue(String::class.java)
                            val id_estudiante = estudianteSnapshop.key
                            val cursoSnapshot = estudianteSnapshop.child("curso")
                            for (cursoSnap in cursoSnapshot.children){
                                val id_curso = cursoSnap.key
                                referencia_curso.child(id_curso.toString()).addValueEventListener(object:ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val nombre_curso = snapshot.child("nombre_curso").getValue(String::class.java)
                                        val matricula = Matricula(id_estudiante+id_curso,id_estudiante.toString(),id_curso.toString(),nombre_estudiante,nombre_curso.toString())
                                        matriculaList.add(matricula)
                                        setup(matriculaList)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setup(matriculaList: ArrayList<Matricula>) {
       // recyclerView.adapter = AdapterMatricula(mainActivity,this,matriculaList)
        recyclerView.adapter = AdapterPrueba(requireContext(),matriculaList)
    }


    private fun mostrarCursosFirebase() {
        referencia_curso.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    cursoList.clear()
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(Curso::class.java)
                        cursoList.add(data!!)
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun mostrarEstudiantesFirebase() {
        referencia_est.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    estudianteList.clear()
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(Estudiante::class.java)
                        estudianteList.add(data!!)
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun mostrarMatriculados() {
        matriculaList.clear()
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT estudiante.nombre_est,curso.nombre_curso FROM estudiante,curso,matricula WHERE estudiante.id_est = matricula.id_est AND matricula.id_curso = curso.id_curso",null)
        if (cursor.moveToFirst()){
            do {
                matriculaList.add(Matricula("","","",cursor.getString(0).toString(),cursor.getString(1).toString()))
            }while (cursor.moveToNext())
            setup(matriculaList)
        }
    }



    private fun mostrarCursos() {
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM estudiante",null)
        if(cursor.moveToFirst()){
            do {
                estudianteList.add(Estudiante(cursor.getInt(0).toString(),cursor.getString(1).toString()))
            }while (cursor.moveToNext())
        }
    }

    private fun mostrarEstudiantes() {
        val db:SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM curso",null)
        if (cursor.moveToFirst()){
            do {
                cursoList.add(Curso(cursor.getInt(0).toString(),cursor.getString(1).toString()))
            }while (cursor.moveToNext())
        }
    }

    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_estudiante_curso)
        val ancho = ViewGroup.LayoutParams.MATCH_PARENT
        val alto = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(ancho,alto)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val id_estu_spinner:Spinner = dialog.findViewById(R.id.id_spinner_est)
        val id_curso_spinner:Spinner = dialog.findViewById(R.id.id_spinner_curso)
        val btnAgregar:Button = dialog.findViewById(R.id.id_btn_guardar)
        val btnCerrar:Button = dialog.findViewById(R.id.id_btn_cancelar)
        val adaptador_est = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,estudianteList)
        id_estu_spinner.adapter = adaptador_est
        val adaptador_curso = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,cursoList)
        id_curso_spinner.adapter=adaptador_curso
        id_estu_spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(context, "ID "+estudianteList.get(position).id_est, Toast.LENGTH_SHORT).show()
                id_estudiante = estudianteList.get(position).id_est
                Toast.makeText(context, "ID_EST "+id_estudiante, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        id_curso_spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                id_curso = cursoList.get(position).id_curso
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }
        btnAgregar.setOnClickListener {
            if (id_curso.isEmpty() && id_estudiante.isEmpty()){
                Toast.makeText(context, "NO SE PUDO MATRICULAR", Toast.LENGTH_SHORT).show()
            }else{

                //FIREBASE INSERTAR DATOS
                insertaFirebase(id_estudiante,id_curso)
                dialog.dismiss()
                //SQLITE INSERTAR
                /*databaseHelper.insertarMatricula(id_estudiante,id_curso)
                Toast.makeText(context, "SE MATRICULO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
                mostrarMatriculados()
                dialog.dismiss()*/
            }
        }


        dialog.show()
    }

    private fun insertaFirebase(id_est:String,id_curso:String) {
        val hashMap = HashMap<String,Boolean>()
        //hashMap.put(id_curso,true)
//        referencia_est.child(id_est).child("curso").child(id_curso) .setValue(true).addOnSuccessListener {
//            Toast.makeText(context, "SE INSERTO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
        referencia_est.child(id_est).child("curso").child(id_curso).setValue(true).addOnSuccessListener {
            Toast.makeText(context, "SE INSERTO CORRECTAMENTE", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, "OCURRIO UN ERROR", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onClickDialog(position: Int) {
        TODO("Not yet implemented")
    }


}