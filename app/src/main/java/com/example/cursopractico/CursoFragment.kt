package com.example.cursopractico

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CursoFragment : Fragment() {
    private  lateinit var btnFloat: FloatingActionButton
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var cursoList:ArrayList<Curso>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_curso, container, false)
        return  view

    }


}