package com.cinergia.cinercia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.cinergia.cinercia.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnGoToMain = findViewById<Button>(R.id.btnGoToMain)
        val etUsuario = findViewById<EditText>(R.id.EtUsuario)
        val etContrasena = findViewById<EditText>(R.id.EtContraseña)


        btnGoToMain.setOnClickListener {
            val usuario = etUsuario.text.toString()
            val contasena  = etContrasena.text.toString()

            if (usuario.isEmpty() || contasena.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese usuario y  contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val exit = withContext(Dispatchers.IO) {
                        AuthService.login(usuario,  contasena)
                    }
                    if (exit) {
                        Toast.makeText(this@Inicio, "Login exitoso", Toast.LENGTH_SHORT).show()
                        goToMain()
                    } else {
                        Toast.makeText(this@Inicio, "Usuario o Contraseña Incorrecto", Toast.LENGTH_SHORT).show()
                    }
                 } catch (e: Exception){
                    Toast.makeText(this@Inicio, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                 }
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this,MainReportes::class.java)
        startActivity(intent)
    }
}