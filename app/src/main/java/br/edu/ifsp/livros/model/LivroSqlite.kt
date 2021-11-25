package br.edu.ifsp.livros.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.livros.R
import java.sql.SQLException

class LivroSqlite(contexto: Context): LivroDao {

    companion object {
        private val BD_LIVROS = "livros"
        private val TABELA_LIVRO = "livro"
        private val COLUNA_TITULO = "titulo"
        private val COLUNA_ISBN = "isbn"
        private val COLUNA_PRIMEIRO_AUTOR = "primeiro_autor"
        private val COLUNA_EDITORA = "editora"
        private val COLUNA_EDICAO = "edicao"
        private val COLUNA_PAGINAS = "paginas"

        private val CRIAR_TABELA_LIVRO_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_LIVRO} (" +
                "${COLUNA_TITULO} TEXT NOT NULL PRIMARY KEY," +
                "${COLUNA_ISBN} TEXT NOT NULL," +
                "${COLUNA_PRIMEIRO_AUTOR} TEXT NOT NULL," +
                "${COLUNA_EDITORA} TEXT NOT NULL," +
                "${COLUNA_EDICAO} INTEGER NOT NULL," +
                "${COLUNA_PAGINAS} INTEGER NOT NULL );"

    }

    //Referencia para banco de dados
    private val livrosBd: SQLiteDatabase
    init {
        livrosBd = contexto.openOrCreateDatabase(BD_LIVROS, MODE_PRIVATE, null)
        try {
            livrosBd.execSQL(CRIAR_TABELA_LIVRO_STMT)
        } catch (se: SQLException) {
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }

    }

    override fun criarLivro(livro: Livro): Long {
        val livroCv = converterLivroParaContentValues(livro)
        return livrosBd.insert(TABELA_LIVRO, null, livroCv)
    }

    override fun recuperarLivro(titulo: String): Livro {
        val livroCursor = livrosBd.query(
            true, //distinct
            TABELA_LIVRO, //tabela
            null, //coluna todas
            "${COLUNA_TITULO} = ?", //query where
            arrayOf(titulo), //valor do where
            null,
            null,
            null,
            null
        )

        if (livroCursor.moveToFirst()) {
            with(livroCursor) {
                return Livro (
                    getString(getColumnIndexOrThrow(COLUNA_TITULO)),
                    getString(getColumnIndexOrThrow(COLUNA_ISBN)),
                    getString(getColumnIndexOrThrow(COLUNA_PRIMEIRO_AUTOR)),
                    getString(getColumnIndexOrThrow(COLUNA_EDITORA)),
                    getInt(getColumnIndexOrThrow(COLUNA_EDICAO)),
                    getInt(getColumnIndexOrThrow(COLUNA_PAGINAS))
                )
            }
        } else {
            return Livro();
        }
    }

    override fun recuperarLivros(): MutableList<Livro> {
        val livroCursor = livrosBd.query(
            true, //distinct
            TABELA_LIVRO, //tabela
            null, //coluna todas
            null, //query where
            null, //valor do where
            null,
            null,
            null,
            null
        )
        val listaLivros: MutableList<Livro> = ArrayList()

        while (livroCursor.moveToNext()) {
            listaLivros.add(
                with(livroCursor) {
                    Livro (
                        getString(getColumnIndexOrThrow(COLUNA_TITULO)),
                        getString(getColumnIndexOrThrow(COLUNA_ISBN)),
                        getString(getColumnIndexOrThrow(COLUNA_PRIMEIRO_AUTOR)),
                        getString(getColumnIndexOrThrow(COLUNA_EDITORA)),
                        getInt(getColumnIndexOrThrow(COLUNA_EDICAO)),
                        getInt(getColumnIndexOrThrow(COLUNA_PAGINAS))
                    )
                }
            )
        }
        return listaLivros
    }

    override fun atualizarLivro(livro: Livro): Int {
        val livroCv = converterLivroParaContentValues(livro)
        return livrosBd.update(TABELA_LIVRO, livroCv, "${COLUNA_TITULO} = ?", arrayOf(livro.titulo))
    }

    override fun removerLivro(titulo: String): Int {
        return livrosBd.delete(TABELA_LIVRO, "${COLUNA_TITULO} = ?", arrayOf(titulo))
    }

    private fun converterLivroParaContentValues(livro: Livro) = ContentValues().also {
        with(it) {
            put(COLUNA_TITULO, livro.titulo)
            put(COLUNA_ISBN, livro.isbn)
            put(COLUNA_PRIMEIRO_AUTOR, livro.primeiroAutor)
            put(COLUNA_EDITORA , livro.editora)
            put(COLUNA_EDICAO , livro.edicao)
            put(COLUNA_PAGINAS , livro.paginas)
        }
    }
}