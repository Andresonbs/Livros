package br.edu.ifsp.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.livros.adapter.LivrosAdapter
import br.edu.ifsp.livros.adapter.LivrosRvAdapter
import br.edu.ifsp.livros.controller.LivroController
import br.edu.ifsp.livros.databinding.ActivityMainBinding
import br.edu.ifsp.livros.model.Livro
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnLivroClickListener{

    companion object Extras {
        const val EXTRA_LIVRO = "EXTRA_LIVRO"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var livroActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var editarLivroActivityResultLauncher: ActivityResultLauncher<Intent>
    //data source
    private val livrosList: MutableList<Livro> by lazy {
        livroController.buscarLivros()
    }

    private val livroController: LivroController by lazy {
        LivroController(this)
    }

    //novo adapter
    private val livrosAdapter: LivrosRvAdapter by lazy {
        LivrosRvAdapter(this, livrosList)
    }

    //layout manager
    private val livrosLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //associando adapter e layout manager ao ListView
        activityMainBinding.LivrosRv.adapter = livrosAdapter
        activityMainBinding.LivrosRv.layoutManager = livrosLayoutManager

        livroActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val livro = resultado.data?.getParcelableExtra<Livro>(EXTRA_LIVRO)?.apply {
                    livroController.inserirLivro(this)
                    livrosList.add(this)
                    livrosAdapter.notifyDataSetChanged()
                }
            }
        }

        editarLivroActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Livro>(EXTRA_LIVRO)?.apply {
                    if (posicao != null && posicao != -1) {
                        livroController.modificarLivro(this)
                        livrosList[posicao] = this
                        livrosAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarLivroFab.setOnClickListener {
            livroActivityResultLauncher.launch(Intent(this, LivroActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = livrosAdapter.posicao

        return when(item.itemId) {
            R.id.editarLivroMi -> {
                val livro = livrosList[posicao]
                val editarLivroIntent = Intent(this, LivroActivity::class.java)
                editarLivroIntent.putExtra(EXTRA_LIVRO, livro)
                editarLivroIntent.putExtra(EXTRA_POSICAO, posicao)
                editarLivroActivityResultLauncher.launch(editarLivroIntent)
                true
            }
            R.id.removerLivroMi -> {
                val livro = livrosList[posicao]
                with(AlertDialog.Builder(this)) {
                    title = "Remover Livro"
                    setMessage("Confirma remoção?")
                    setPositiveButton("Sim") {_, _, ->
                        livroController.apagarLivro(livro.titulo)
                        livrosList.removeAt(posicao)
                        livrosAdapter.notifyDataSetChanged()
                    }
                    setNegativeButton("Não") {_,_, ->
                        Snackbar.make(activityMainBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onLivroClick(posicao: Int) {
        val livro = livrosList[posicao]
        val consultarLivroIntent = Intent(this, LivroActivity::class.java)
        consultarLivroIntent.putExtra(EXTRA_LIVRO, livro)
        startActivity(consultarLivroIntent)
    }

    override fun onStart() {
        super.onStart()
        if (AutenticacaoFirebase.firebaseAuth.currentUser == null) {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.sairMi -> {
            AutenticacaoFirebase.firebaseAuth.signOut()
            finish()
            true
        }
        else -> { false }
    }

}