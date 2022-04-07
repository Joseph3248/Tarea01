package com.isc.tarea01.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.isc.tarea01.model.Lugar
import java.util.ArrayList


class LugarDao {

    private val coleccion1 = "lugaresAPP"
    private val coleccion2 = "misLugares"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    private var firestore: FirebaseFirestore

    init {
        firestore= FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //Para obtener la lista de lugares
    fun getLugares(): MutableLiveData<List<Lugar>> {
        val listaLugares = MutableLiveData<List<Lugar>>()


        firestore.collection(coleccion1).document(usuario).collection(coleccion2)
            .addSnapshotListener{instantanea, e ->


                if (e != null){//si da un error recuperando la informacion
                    return@addSnapshotListener
                }

                if (instantanea != null){//si hay informacion recuperada
              val lista = ArrayList<Lugar>()

                    //recorro todos los documentos de la ruta
                    instantanea.documents.forEach{
                        val lugar = it.toObject(Lugar::class.java)

                        if (lugar != null){//si se pudo convertir el documento en un lugar

                            lista.add(lugar)
                        }
                    }
                    listaLugares.value = lista
                }
            }

        return listaLugares
    }

    suspend fun saveLugar(lugar: Lugar){
        val documento: DocumentReference
        if (lugar.id.isEmpty()){//si esta vacio es un documento nuevo
            documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document()
            lugar.id = documento.id

        }else{//El documento ya existe
            documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id)

        }
        documento.set(lugar)
            .addOnSuccessListener { Log.d("saveLugar","Lugar agregado/modificado") }
            .addOnCanceledListener {  Log.e("saveLugar","Error : Lugar NO agregado") }


    }


    suspend fun deleteLugar(lugar: Lugar){
//validamos si en documento existe

        if (lugar.id.isNotEmpty()){
            firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id).delete()

                .addOnSuccessListener { Log.d("deleteLugar","Lugar eliminado") }
                .addOnCanceledListener {  Log.e("deleteLugar","Error : Lugar NO eliminado") }


        }
    }

}