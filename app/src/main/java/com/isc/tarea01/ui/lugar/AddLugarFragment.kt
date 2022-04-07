package com.isc.tarea01.ui.lugar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.isc.tarea01.R
import com.isc.tarea01.databinding.FragmentAddLugarBinding
import com.isc.tarea01.model.Lugar
import com.isc.tarea01.viewmodel.LugarViewModel
import com.lugares.utiles.AudioUtiles
import com.lugares.utiles.ImagenUtiles

class AddLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentAddLugarBinding? = null


    private val binding get() = _binding!!


    private lateinit var audioUtiles: AudioUtiles
    private lateinit var imagenUtiles: ImagenUtiles
    private lateinit var tomarFotoActivity : ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)
        binding.btAddLugar.setOnClickListener {
            binding.progressBar.visibility = ProgressBar.VISIBLE
        binding.msgMensaje.text = getString(R.string.msg_subiendo_audio)
        binding.msgMensaje.visibility = TextView.VISIBLE
            subeAudioNube()

        }


        audioUtiles = AudioUtiles(
            requireActivity(),
            requireContext(),
            binding.btAccion,
            binding.btPlay,
            binding.btDelete,
            getString(R.string.msg_graba_audio),
            getString(R.string.msg_graba_pausa)

        )

        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){

                imagenUtiles.actualizaFoto()
            }


        }

        imagenUtiles = ImagenUtiles(
            requireContext(),
        binding.btPhoto,
        binding.btRotaL,
        binding.btRotaR,
        binding.imagen,
        tomarFotoActivity)


        ubicaGPS()

        return binding.root
    }

    private fun subeAudioNube() {

        val audioFile = audioUtiles.audioFile
        if (audioFile.exists()&& audioFile.isFile && audioFile.canRead()){

val ruta = Uri.fromFile(audioFile)
            val rutaNube="lugaresApp/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"

            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaAudio = it.toString()
                            subeImagenNube(rutaAudio)

                        }
                }

                .addOnFailureListener{
                    subeImagenNube("")
                }

        }else{
            subeImagenNube("")


        }

    }

    private fun subeImagenNube(rutaAudio: String) {


        val imagenFile = imagenUtiles.imagenFile
        if (imagenFile.exists()&& imagenFile.isFile && imagenFile.canRead()){

            val ruta = Uri.fromFile(imagenFile)
            val rutaNube="lugaresApp/${Firebase.auth.currentUser?.email}/imagenes/${imagenFile.name}"

            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaImagen = it.toString()
                            agregarLugar(rutaAudio,rutaImagen)

                        }
                }

                .addOnFailureListener{
                    agregarLugar(rutaAudio,"")
                }

        }else{
            agregarLugar(rutaAudio,"")


        }


    }

    private var conPermisos: Boolean = true
    private fun ubicaGPS() {

        val ubicacion: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())//obtiene las cordenadas


        //se validan los permisos
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {

//se solicita los permisos de usuario
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 105
            )

        }

        if (conPermisos) {
            ubicacion.lastLocation.addOnSuccessListener { location: Location? ->

                if (location != null) {

                    binding.tvLatitud.text = "${location.latitude}"
                    binding.tvLongitud.text = "${location.longitude}"
                    binding.tvAltura.text = "${location.altitude}"
                } else {
                    binding.tvLatitud.text = "9.9358"
                    binding.tvLongitud.text = "-84.0360"
                    binding.tvAltura.text = "0.0"

                }

            }


        }


    }

    private fun agregarLugar(rutaAudio: String, rutaImagen: String) {
        val nombre = binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) {
            val correo = binding.etCorreo.text.toString()
            val telefono = binding.etTelefono.text.toString()
            val web = binding.etWeb.text.toString()
            val latitud = binding.tvLatitud.text.toString().toDouble()
            val longitud = binding.tvLongitud.text.toString().toDouble()
            val altura = binding.tvAltura.text.toString().toDouble()
            val lugar = Lugar("", nombre, correo, telefono, web, latitud, longitud, altura, rutaAudio, rutaImagen)
            lugarViewModel.addLugar(lugar)
            Toast.makeText(requireContext(), getString(R.string.msg_Lugar_add), Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}