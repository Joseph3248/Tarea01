package com.isc.tarea01.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isc.tarea01.databinding.FragmentAddLugarBinding
import com.isc.tarea01.databinding.LugarFilaBinding
import com.isc.tarea01.model.Lugar
import com.isc.tarea01.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    private var listaLugares = emptyList<Lugar>()


    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
            fun bind(lugar: Lugar){
                itemBinding.tvNombre.text = lugar.nombre
                itemBinding.tvCorreo.text = lugar.correo
                itemBinding.tvTelefono.text = lugar.telefono
                itemBinding.tvWeb.text = lugar.web

                Glide.with(itemBinding.root.context)
                    .load(lugar.rutaImagen)
                    .circleCrop()
                    .into(itemBinding.imageView3)


                itemBinding.vistaFila.setOnClickListener {
                    val accion = LugarFragmentDirections.actionNavLugarToNavUpdateLugar(lugar)
                    itemView.findNavController().navigate(accion)
                }

            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {

        //Crea una tarjeta
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LugarViewHolder(itemBinding)//devuelvo la card para pintar
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
       val lugar = listaLugares[position]//devuelvo el lugar donde pintar
        holder.bind(lugar) //se invoca la funcion del inner class
    }

    override fun getItemCount(): Int {
        return listaLugares.size //Para saber la cantidad de cards que debe de realizar
    }

    fun setData(lugares : List<Lugar>){
        this.listaLugares = lugares
        notifyDataSetChanged()
    }

}