package br.edu.ifsp.livros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.livros.databinding.ActivityCadastrarUsuarioBinding

class CadastrarUsuarioActivity : AppCompatActivity() {
    private val activityCadastrarUsuarioBinding: ActivityCadastrarUsuarioBinding by lazy {
        ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityCadastrarUsuarioBinding.root)
        supportActionBar?.subtitle = "Cadastrar usuário"

        with(activityCadastrarUsuarioBinding) {
            cadastrarBt.setOnClickListener {
                val email = emailEt.text.toString()
                val senha = senhaEt.text.toString()
                val repetirSenha = repetirSenhaEt.text.toString()
                if (senha == repetirSenha) {
                    AutenticacaoFirebase.firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener {
                        //usuario cadastrado com sucesso
                        Toast.makeText(this@CadastrarUsuarioActivity, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        //falha no cadastro do usuario

                    }
                } else {
                    Toast.makeText(this@CadastrarUsuarioActivity, "Senhas não coincidem", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}