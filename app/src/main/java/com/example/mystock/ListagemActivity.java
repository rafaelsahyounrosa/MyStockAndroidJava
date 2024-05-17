package com.example.mystock;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListagemActivity extends AppCompatActivity {

    private ListView listViewProdutos;
    private ArrayList<Produto> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listagem);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        listViewProdutos = findViewById(R.id.listViewProdutos);
        listViewProdutos.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Produto produto = (Produto) listViewProdutos.getItemAtPosition(position);

                        Toast.makeText(getApplicationContext(),
                                getString(R.string.item_de_nome) + produto.getNome() + getString(R.string.foi_clicado),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        popularLista();
    }

    private void popularLista(){

        String[] nomes = getResources().getStringArray(R.array.nomes);
        int[] quantidades = getResources().getIntArray(R.array.quantidades);
        int[] importantes = getResources().getIntArray(R.array.importantes);
        int[] criticidades = getResources().getIntArray(R.array.criticidades);
        int[] categorias = getResources().getIntArray(R.array.categorias);

        produtos = new ArrayList<>();

        Produto produto;
        Categoria[] categoriasList = Categoria.values();
        Criticidade[] criticidadesList = Criticidade.values();

        for (int i = 0; i < nomes.length; i++){
            produto = new Produto();
            produto.setNome(nomes[i]);
            produto.setQuantidade(quantidades[i]);
            produto.setCategoria(categoriasList[categorias[i]]);
            if (importantes[i] == 0){
                produto.setImportante(false);
                produto.setCriticidade(criticidadesList[3]);
            } else {
                produto.setImportante(true);
                produto.setCriticidade(criticidadesList[criticidades[i]]);
            }

            produtos.add(produto);

        }

        ProdutoAdapter produtoAdapter = new ProdutoAdapter(this, produtos);
        listViewProdutos.setAdapter(produtoAdapter);

    }
}