package com.cinergia.cinercia.fotos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cinergia.cinercia.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class fotosUnoActivity : AppCompatActivity() {

    private lateinit var fotoUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 1
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


        val btnTomarFoto = findViewById<Button>(R.id.btnTomarFoto)
        val btnEnviar = findViewById<Button>(R.id.btnEnviarFotos)

        btnTomarFoto.setOnClickListener {
            tomarFoto()
        }

        btnEnviar.setOnClickListener {
            val intent = Intent(this, fotoDosActivity::class.java).apply {
                putParcelableArrayListExtra("fotos", ArrayList(fotosTomadas))
            }
            startActivity(intent)
        }


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
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
            Date()
        )
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            fotosTomadas.add(fotoUri)
        }
    }
}

//val nodoId = intent.getStringExtra("nodoId")
//val nodoNombre = intent.getStringExtra("nodoNombre")
//val nodoMunicipio = intent.getStringExtra("nodoMunicipio")
//val nodoComunidad = intent.getStringExtra("nodoComunidad")
//val nodoDireccion = intent.getStringExtra("nodoDireccion")
//val nodoTipo = intent.getStringExtra("nodoTipo")
//val nodoLatitud = intent.getStringExtra("nodoLatitud")
//val nodoLongitud = intent.getStringExtra("nodoLongitud")
//val nodoTelefono = intent.getStringExtra("nodoTelefono")
//val nodoCorreo = intent.getStringExtra("nodoCorreo")
//val nodoClaveNodo = intent.getStringExtra("nodoClaveNodo")
//val nodoCodigoPostal = intent.getStringExtra("nodoCodigoPostal")
//val nodoNomenclatura = intent.getStringExtra("nodoNomenclatura")
//
//val empleado = intent.getStringExtra("empleado")
//val area = intent.getStringExtra("area")
//val entidad = intent.getStringExtra("entidad")
//val servicio = intent.getStringExtra("servicio")
//val mantenimineto = intent.getStringExtra("mantenimiento")
//val hora = intent.getStringExtra("hora")
//val fechaEmision = intent.getStringExtra("fechaEmision")
//val fechaApertura = intent.getStringExtra("fechaApertura")
//val fechaLlegada = intent.getStringExtra("fechaLlegada")
//val fechaCierre = intent.getStringExtra("fechaCierre")