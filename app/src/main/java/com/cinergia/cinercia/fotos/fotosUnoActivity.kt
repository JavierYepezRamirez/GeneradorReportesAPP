package com.cinergia.cinercia.fotos

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinergia.cinercia.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class fotosUnoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FotoAdapter
    private lateinit var fotoUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_SELECT_IMAGES = 2
    private val PERMISSION_REQUEST_CODE = 100
    private val fotosTomadas = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fotos_uno)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nodoId = intent.getStringExtra("nodoId")
        val nodoNombre = intent.getStringExtra("nodoNombre")
        val nodoMunicipio = intent.getStringExtra("nodoMunicipio")
        val nodoComunidad = intent.getStringExtra("nodoComunidad")
        val nodoDireccion = intent.getStringExtra("nodoDireccion")
        val nodoTipo = intent.getStringExtra("nodoTipo")
        val nodoLatitud = intent.getStringExtra("nodoLatitud")
        val nodoLongitud = intent.getStringExtra("nodoLongitud")
        val nodoTelefono = intent.getStringExtra("nodoTelefono")
        val nodoCorreo = intent.getStringExtra("nodoCorreo")
        val nodoClaveNodo = intent.getStringExtra("nodoClaveNodo")
        val nodoCodigoPostal = intent.getStringExtra("nodoCodigoPostal")
        val nodoNomenclatura = intent.getStringExtra("nodoNomenclatura")

        val empleado = intent.getStringExtra("empleado")
        val area = intent.getStringExtra("area")
        val entidad = intent.getStringExtra("entidad")
        val servicio = intent.getStringExtra("servicio")
        val mantenimiento = intent.getStringExtra("mantenimiento")
        val hora = intent.getStringExtra("hora")
        val fechaEmision = intent.getStringExtra("fechaEmision")
        val fechaApertura = intent.getStringExtra("fechaApertura")
        val fechaLlegada = intent.getStringExtra("fechaLlegada")
        val fechaCierre = intent.getStringExtra("fechaCierre")

        val btnTomarFoto = findViewById<Button>(R.id.btnTomarFoto)
        val btnEnviar = findViewById<Button>(R.id.btnEnviarFotos)
        val btnSeleccionar = findViewById<Button>(R.id.btnSeleccionarFotos)

        recyclerView = findViewById(R.id.recyclerFotos)
        adapter = FotoAdapter(fotosTomadas)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = FotoAdapter(fotosTomadas)
        recyclerView.adapter = adapter

        adapter.onEliminarClick = { pos ->
            fotosTomadas.removeAt(pos)
            adapter.notifyItemRemoved(pos)
        }

        btnSeleccionar.setOnClickListener {
            if (checkStoragePermission()) {
                seleccionarFotosGaleria()
            } else {
                requestStoragePermission()
            }
        }

        btnTomarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                tomarFoto()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
            }
        }

        btnEnviar.setOnClickListener {
            val intent = Intent(this, fotoDosActivity::class.java).apply {
                putParcelableArrayListExtra("fotos", ArrayList(fotosTomadas))

                putExtra("nodoId", nodoId)
                putExtra("nodoNombre", nodoNombre)
                putExtra("nodoMunicipio", nodoMunicipio)
                putExtra("nodoComunidad", nodoComunidad)
                putExtra("nodoDireccion", nodoDireccion)
                putExtra("nodoTipo", nodoTipo)
                putExtra("nodoLatitud", nodoLatitud)
                putExtra("nodoLongitud", nodoLongitud)
                putExtra("nodoTelefono", nodoTelefono)
                putExtra("nodoCorreo", nodoCorreo)
                putExtra("nodoClaveNodo", nodoClaveNodo)
                putExtra("nodoCodigoPostal", nodoCodigoPostal)
                putExtra("nodoNomenclatura", nodoNomenclatura)

                putExtra("empleado", empleado)
                putExtra("area", area)
                putExtra("entidad", entidad)
                putExtra("servicio", servicio)
                putExtra("mantenimiento", mantenimiento)
                putExtra("hora", hora)
                putExtra("fechaEmision", fechaEmision)
                putExtra("fechaApertura", fechaApertura)
                putExtra("fechaLlegada", fechaLlegada)
                putExtra("fechaCierre", fechaCierre)
            }
            startActivity(intent)
        }
    }

    private fun seleccionarFotosGaleria() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, "Selecciona imágenes"), REQUEST_SELECT_IMAGES)
    }

    private fun tomarFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val fotoFile = crearArchivoImagen()
        fotoUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            fotoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun crearArchivoImagen(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    fotosTomadas.add(fotoUri)
                    adapter.notifyItemInserted(fotosTomadas.size - 1)
                }
                REQUEST_SELECT_IMAGES -> {
                    data?.let {
                        if (it.clipData != null) {
                            val count = it.clipData!!.itemCount
                            for (i in 0 until count) {
                                val imageUri = it.clipData!!.getItemAt(i).uri
                                fotosTomadas.add(imageUri)
                            }
                            adapter.notifyDataSetChanged()
                        } else if (it.data != null) {
                            val imageUri = it.data!!
                            fotosTomadas.add(imageUri)
                            adapter.notifyItemInserted(fotosTomadas.size - 1)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (permissions.contains(android.Manifest.permission.CAMERA)) {
                    tomarFoto()
                } else if (permissions.contains(android.Manifest.permission.READ_EXTERNAL_STORAGE) || permissions.contains(android.Manifest.permission.READ_MEDIA_IMAGES)) {
                    seleccionarFotosGaleria()
                }
            } else {
                Toast.makeText(this, "Se requiere permiso para continuar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}