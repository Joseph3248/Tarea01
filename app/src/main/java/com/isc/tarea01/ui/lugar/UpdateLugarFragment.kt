package com.isc.tarea01.ui.lugar

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.GeofencingRequest
import com.isc.tarea01.R
import com.isc.tarea01.databinding.FragmentAddLugarBinding
import com.isc.tarea01.databinding.FragmentLugarBinding
import com.isc.tarea01.databinding.FragmentUpdateLugarBinding
import com.isc.tarea01.model.Lugar
import com.isc.tarea01.viewmodel.LugarViewModel
import kotlinx.coroutines.handleCoroutineException
import android.Manifest
import android.media.MediaPlayer
import android.system.Os
import com.bumptech.glide.Glide

class UpdateLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentUpdateLugarBinding? = null


    private val binding get() = _binding!!

    private val args by navArgs<UpdateLugarFragmentArgs>()

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        //Obtengo la info del lugar y la coloco en las cajas de texto del update
        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)
        binding.tvLatitud.text = args.lugar.latitud.toString()
        binding.tvLongitud.text = args.lugar.longitud.toString()
        binding.tvAltura.text = args.lugar.altura.toString()


        binding.btUpdateLugar.setOnClickListener { actualizarLugar() }
        binding.btEmail.setOnClickListener { enviarCorreo() }
        binding.btPhone.setOnClickListener { hacerLlamda() }
        binding.btWhatsapp.setOnClickListener { enviarWhatsapp() }
        binding.btWeb.setOnClickListener { verWeb() }
        binding.btLocation.setOnClickListener { verMapa() }

        if (args.lugar.rutaAudio?.isNotEmpty()== true ){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.lugar.rutaAudio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled=true
            binding.btPlay.setOnClickListener { mediaPlayer.start() }


        }else{
            binding.btPlay.isEnabled=false
        }
binding.btPlay.setOnClickListener { mediaPlayer.start() }


        if (args.lugar.rutaImagen?.isNotEmpty()== true ){
Glide.with(requireContext())
    .load(args.lugar.rutaImagen)
    .fitCenter()
    .into(binding.imagen)

        }


        setHasOptionsMenu(true) //Este fragmento debe tener un menu adicional

        return binding.root
    }

    private fun verMapa() {
        val latitud = binding.tvLatitud.text.toString().toDouble()//extrae info de la web
        val longitud = binding.tvLongitud.text.toString().toDouble()
        if (latitud.isFinite() && longitud.isFinite()) {//verifican recursos

            val location = Uri.parse("geo:$latitud,$longitud?z=18")

            val intent = Intent(Intent.ACTION_VIEW, location)//Se ve el mapa desde la app

            startActivity(intent)//Se abre el visor de mapas y se ve el lugar

        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun verWeb() {
        val sitio = binding.etWeb.text.toString()//extrae info de la web
        if (sitio.isNotEmpty()) {//verifica que existan datos

            val web = Uri.parse("http://$sitio")

            val intent = Intent(Intent.ACTION_VIEW, web)//Se llama algo desde el app

            startActivity(intent)

        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun enviarWhatsapp() {
        val telefono = binding.etTelefono.text.toString()//extrae info telefono
        if (telefono.isNotEmpty()) {//verifica que existan datos

            val intent = Intent(Intent.ACTION_VIEW)//Se llama algo desde el app
            val uri = "whatsapp://send?phone=506$telefono&text=" +
                    getString(R.string.msg_saludos)

            intent.setPackage("com.whatsapp")//Se establece el app a utilizar
            intent.data = Uri.parse(uri)
            startActivity(intent)

        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun hacerLlamda() {

        val telefono = binding.etTelefono.text.toString()//extrae info telefono
        if (telefono.isNotEmpty()) {//verifica que existan datos

            val intent = Intent(Intent.ACTION_CALL)//Se llama algo desde el app

            intent.data = Uri.parse("tel:$telefono")//indica que va a realizar una llamada

//se valida si hay permisos para realizar la llamada
            if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED
            ) {            //si no tenemos permisos,se solicitan al usuario
                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 105)
            } else {
                requireActivity().startActivity(intent)
            }


        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun enviarCorreo() {
        val correo = binding.etCorreo.text.toString()//extrae info correo
        if (correo.isNotEmpty()) {//verifica que existan datos

            val intent = Intent(Intent.ACTION_SEND)//Se envia algo desde el app
            intent.type = "message/rfc822" //indica que va a enviar un correo electronico

            //se define el destinatario
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))

            //se define el asunto
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.msg_saludos) + "" + binding.etNombre.text
            )

            //se define le cuerpo del correo
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_correo))

            //se solicita el recurso del correo para que ese envie este
            startActivity(intent)


        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG)
                .show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.delete_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu) {
            deleteLugar()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun actualizarLugar() {
        val nombre = binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) {
            val correo = binding.etCorreo.text.toString()
            val telefono = binding.etTelefono.text.toString()
            val web = binding.etWeb.text.toString()
            val lugar = Lugar(args.lugar.id, nombre, correo, telefono, web, args.lugar.latitud, args.lugar.longitud, args.lugar.altura ,
                   args.lugar.rutaAudio, args.lugar.rutaImagen)
            lugarViewModel.updateLugar(lugar)
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_Lugar_update),
                Toast.LENGTH_SHORT
            )
                .show()
            findNavController().navigate(R.id.action_nav_updateLugar_to_nav_lugar)
        }
    }

    private fun deleteLugar() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.menu_delete)
        builder.setMessage(getString(R.string.msg_seguroBorrar) + " ${args.lugar.nombre}?")
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setPositiveButton(getString(R.string.si)) { _, _ ->
            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_nav_updateLugar_to_nav_lugar)
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}