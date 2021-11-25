package br.edu.ifsp.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.livros.databinding.ActivityAutenticacaoBinding
import com.google.firebase.auth.FirebaseAuth

class AutenticacaoActivity : AppCompatActivity() {

    private val activityAutenticacaoBinding: ActivityAutenticacaoBinding by lazy {
        ActivityAutenticacaoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityAutenticacaoBinding.root)
        supportActionBar?.subtitle = "Autenticação"

        activityAutenticacaoBinding.cadastrarUsuarioBt.setOnClickListener {
            startActivity(Intent(this, CadastrarUsuarioActivity::class.java))
        }

        activityAutenticacaoBinding.recuperarSenhaBt.setOnClickListener {
            startActivity(Intent(this, RecuperarSenhaActivity::class.java))
        }

        with(activityAutenticacaoBinding) {
            entrarBt.setOnClickListener {
                val email = emailEt.text.toString()
                val senha = senhaEt.text.toString()
                AutenticacaoFirebase.firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                    Toast.makeText(this@AutenticacaoActivity, "Usuário autenticado com sucesso", Toast.LENGTH_SHORT).show()
                    iniciarMainActivity()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@AutenticacaoActivity, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (AutenticacaoFirebase.firebaseAuth.currentUser != null) {
            iniciarMainActivity()
        }
    }

    private fun iniciarMainActivity() {
        startActivity(Intent(this@AutenticacaoActivity, MainActivity::class.java))

    }
}