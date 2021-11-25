package br.edu.ifsp.livros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.livros.databinding.ActivityRecuperarSenhaBinding

class RecuperarSenhaActivity : AppCompatActivity() {

    private val activityRecuperarSenhaBinding: ActivityRecuperarSenhaBinding by lazy {
        ActivityRecuperarSenhaBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityRecuperarSenhaBinding.root)
        supportActionBar?.subtitle = "Recuperar senha"

        with(activityRecuperarSenhaBinding) {
            enviarEmailBt.setOnClickListener {
                val email = emailEt.text.toString()
                if (email.isNotEmpty()) {
                    AutenticacaoFirebase.firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { envio ->
                        if (envio.isSuccessful) {
                            Toast.makeText(this@RecuperarSenhaActivity, "E-mail de recuperação enviado", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@RecuperarSenhaActivity, "Falha no envio do e-mail de recuperação", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }
}