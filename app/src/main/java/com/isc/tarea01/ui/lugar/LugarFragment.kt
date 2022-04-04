package com.isc.tarea01.ui.lugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isc.tarea01.R
import com.isc.tarea01.adapter.LugarAdapter
import com.isc.tarea01.databinding.FragmentLugarBinding
import com.isc.tarea01.viewmodel.LugarViewModel

class LugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentLugarBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentLugarBinding.inflate(inflater, container, false)
binding.floatingActionButton.setOnClickListener {
    findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
}
//activar el recycler view
        var lugarAdapter = LugarAdapter() //se crea un objeto adapter
        var reciclador = binding.reciclador //se recupera el recycler view de la vista

        reciclador.adapter = lugarAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())


        //Se crea un observador que me muestra toda la info de la tabla lugares
        //se actualiza cuando cambio el set de datos
        lugarViewModel.getAllData.observe(viewLifecycleOwner){ lugares ->

            lugarAdapter.setData(lugares)
        }







        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}