package br.edu.ifsp.livros.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.livros.OnLivroClickListener
import br.edu.ifsp.livros.R
import br.edu.ifsp.livros.databinding.LayoutLivroBinding
import br.edu.ifsp.livros.model.Livro

class LivrosRvAdapter(
    private val onLivroClickListener: OnLivroClickListener,
    private val livrosList: MutableList<Livro>
): RecyclerView.Adapter<LivrosRvAdapter.LivroLayoutHolder>() {

    //poscao que sera recuperada pelo menu de contexto
    var posicao: Int = -1

    //ViewHolder
    inner class LivroLayoutHolder(layoutLivroBinding: LayoutLivroBinding): RecyclerView.ViewHolder(layoutLivroBinding.root), View.OnCreateContextMenuListener {
        val tituloTv: TextView = layoutLivroBinding.tituloTv
        val primeiroAutorTv: TextView = layoutLivroBinding.primeiroAutorTv
        val editoraTv: TextView = layoutLivroBinding.editoraTv

        init{
            itemView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_main, menu)
        }
    }

    //quando uma nova celula precisar ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroLayoutHolder {
        //criar uma nova celula
        val layoutLivroBinding = LayoutLivroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //criar um viewholder associado a nova celula
        val viewHolder: LivroLayoutHolder = LivroLayoutHolder(layoutLivroBinding)
        return viewHolder
    }

    //atualizar os valores de uma celula
    override fun onBindViewHolder(holder: LivroLayoutHolder, position: Int) {
        //busca livro
        val livro = livrosList[position]

        with(holder){
            tituloTv.text = livro.titulo
            primeiroAutorTv.text = livro.primeiroAutor
            editoraTv.text = livro.editora
            itemView.setOnClickListener {
                onLivroClickListener.onLivroClick(position)
            }
            itemView.setOnLongClickListener {
                posicao = position
                false
            }
        }

    }

    override fun getItemCount(): Int {
        return livrosList.size
    }

}