package br.edu.ifsp.livros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.edu.ifsp.livros.databinding.ActivityLivroBinding;
import br.edu.ifsp.livros.model.Livro;

public class LivroActivity extends AppCompatActivity {

    private ActivityLivroBinding activityLivroBinding;

    private int posicao = -1;
    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLivroBinding = ActivityLivroBinding.inflate(getLayoutInflater());
        setContentView(activityLivroBinding.getRoot());

        activityLivroBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    livro = new Livro(
                            activityLivroBinding.tituloEt.getText().toString(),
                            activityLivroBinding.isbnEt.getText().toString(),
                            activityLivroBinding.primeiroAutorEt.getText().toString(),
                            activityLivroBinding.editoraEt.getText().toString(),
                            Integer.parseInt(activityLivroBinding.edicaoEt.getText().toString()),
                            Integer.parseInt(activityLivroBinding.numPaginasEt.getText().toString())
                    );

                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainActivity.EXTRA_LIVRO, livro);
                    //se for edicao, devolver posicao
                    if (posicao != -1){
                        resultadoIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        //verificando se é uma edição ou consulta e preenchendo os campos
        livro = getIntent().getParcelableExtra(MainActivity.EXTRA_LIVRO);
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        if (livro != null){
            activityLivroBinding.tituloEt.setEnabled(false);
            activityLivroBinding.tituloEt.setText(livro.getTitulo());
            activityLivroBinding.isbnEt.setText(livro.getIsbn());
            activityLivroBinding.primeiroAutorEt.setText(livro.getPrimeiroAutor());
            activityLivroBinding.editoraEt.setText(livro.getEditora());
            activityLivroBinding.edicaoEt.setText(String.valueOf(livro.getEdicao()));
            activityLivroBinding.numPaginasEt.setText(String.valueOf(livro.getPaginas()));
            if (posicao == -1){
                for (int i = 0; i < activityLivroBinding.getRoot().getChildCount(); i++) {
                    activityLivroBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityLivroBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AutenticacaoFirebase.INSTANCE.getFirebaseAuth().getCurrentUser() == null) {
            finish();
        }
    }
}