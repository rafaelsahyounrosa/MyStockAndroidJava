package com.example.mystock;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.example.mystock.model.Categoria;
import com.example.mystock.model.Criticidade;
import com.example.mystock.model.Produto;
import com.example.mystock.persistence.ProdutoDatabase;
import com.example.mystock.utils.UtilsGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListagemActivity extends AppCompatActivity {

    private ListView listViewProdutos;
    private List<Produto> produtos;
    private ProdutoAdapter listaAdapter;
    private ActionMode actionMode;
    private View viewSelecionada;
    private int posicaoSelecionada = -1;
    public static final String ARQUIVO = "com.rafaelrosa.mystock.PREFERENCIAS";

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.principal_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int idMenuItem = item.getItemId();

            if (idMenuItem == R.id.menuItemEditar){
                editarProduto();
                mode.finish();
                return true;
            }
            else if (idMenuItem == R.id.menuItemDeletar){
                deletarProduto(mode);

                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode = null;
            viewSelecionada = null;

            listViewProdutos.setEnabled(true);
        }
    };


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

        listViewProdutos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null){
                    return false;
                }

                posicaoSelecionada = position;
                view.setBackgroundColor(Color.LTGRAY);
                viewSelecionada = view;
                listViewProdutos.setEnabled(false);
                actionMode = startSupportActionMode(actionModeCallback);
                return false;
            }
        });

        listViewProdutos.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        posicaoSelecionada = position;
                        editarProduto();
//                        Produto produto = (Produto) listViewProdutos.getItemAtPosition(position);
//
//                        Toast.makeText(getApplicationContext(),
//                                getString(R.string.item_de_nome) + produto.getNome() + getString(R.string.foi_clicado),
//                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        popularLista();
    }

    private void deletarProduto(final ActionMode mode){

        final Produto produto = produtos.get(posicaoSelecionada);
        String mensagem = getString(R.string.realmente_deseja_apagar) + "\n" + "\"" + produto.getNome() + "\"";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        ProdutoDatabase database = ProdutoDatabase.getDatabase(ListagemActivity.this);
                        int quantidadeAfetada = database.getProdutoDao().delete(produto);

                        if(quantidadeAfetada > 0 ){
                            produtos.remove(posicaoSelecionada);
                            listaAdapter.notifyDataSetChanged();
                            mode.finish();
                        }else{
                            UtilsGUI.aviso(ListagemActivity.this, R.string.error_while_trying_to_delete);
                        }

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        UtilsGUI.confirmaçao(this, mensagem, listener);


    }
   private void editarProduto(){
        Produto produto = produtos.get(posicaoSelecionada);

        ProdutoCadastroActivity.editarProduto(this, launcherEditarProduto, produto);
    }

    ActivityResultLauncher<Intent> launcherEditarProduto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == ListagemActivity.RESULT_OK){

                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();

                        if (bundle != null){

                            long id = bundle.getLong(ProdutoCadastroActivity.ID);
                            ProdutoDatabase database = ProdutoDatabase.getDatabase(ListagemActivity.this);
                            Produto produtoEditado = database.getProdutoDao().queryForID(id);

//                            String nome = bundle.getString(ProdutoCadastroActivity.NOME);
//                            String quantidade = bundle.getString(ProdutoCadastroActivity.QUANTIDADE);
//                            Boolean importante = bundle.getBoolean(ProdutoCadastroActivity.IMPORTANTE);
//
//                            //Tratando a criticidade após a internacionalização
//                            //String criticidade = bundle.getString(ProdutoCadastroActivity.CRITICIDADE);
//                            int criticidadeCod = bundle.getInt(ProdutoCadastroActivity.CRITICIDADE_CODIGO);
//                            Criticidade criticidadeTemp;
//                            switch (criticidadeCod){
//                                case 0:
//                                    criticidadeTemp = Criticidade.Baixa;
//                                    break;
//                                case 1:
//                                    criticidadeTemp = Criticidade.Media;
//                                    break;
//                                case 2:
//                                    criticidadeTemp = Criticidade.Alta;
//                                    break;
//                                default:
//                                    criticidadeTemp = Criticidade.Nenhuma;
//                                    break;
//                            }
//                            //Tratando a categoria após a internacionalização
//                            String categoria = bundle.getString(ProdutoCadastroActivity.CATEGORIA);
//                            int categoriaCod = bundle.getInt(ProdutoCadastroActivity.CATEGORIA_CODIGO);
//                            Categoria categoriaTemp;
//                            //TODO Pedir feedback se essa é a solução ideal
//                            if (categoriaCod == R.id.radioButtonHigiene){
//
//                                categoriaTemp = Categoria.Higiene;
//                            } else if (categoriaCod == R.id.radioButtonComida) {
//                                categoriaTemp = Categoria.Comida;
//                            } else {
//                                categoriaTemp = Categoria.Limpeza;
//                            }
//
//                            Produto produto = produtos.get(posicaoSelecionada);
//                            produto.setNome(nome);
//                            produto.setQuantidade(Integer.parseInt(quantidade));
//                            produto.setImportante(importante);
//                            //produto.setCriticidade(Criticidade.valueOf(criticidade));
//                            produto.setCriticidade(criticidadeTemp);
//                            //produto.setCategoria(Categoria.valueOf(categoria));
//                            produto.setCategoria(categoriaTemp);

                            produtos.set(posicaoSelecionada, produtoEditado);

                            posicaoSelecionada = -1;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Collections.sort(produtos, Comparator
                                        .comparing(Produto::getCategoria)
                                        .thenComparing(Produto::getNome));
                            }else {
                                Collections.sort(produtos ,Produto.comparatorApiUnder24);
                            }

                            listaAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

    );
        ActivityResultLauncher<Intent> launcherNovoProduto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result.getResultCode() == ListagemActivity.RESULT_OK){

                            Intent intent = result.getData();
                            Bundle bundle = intent.getExtras();


                            if (bundle != null){

                                long id = bundle.getLong(ProdutoCadastroActivity.ID);
                                ProdutoDatabase database = ProdutoDatabase.getDatabase(ListagemActivity.this);
                                Produto produtoNovo = database.getProdutoDao().queryForID(id);

//                                String nome = bundle.getString(ProdutoCadastroActivity.NOME);
//                                String quantidade = bundle.getString(ProdutoCadastroActivity.QUANTIDADE);
//                                Boolean importante = bundle.getBoolean(ProdutoCadastroActivity.IMPORTANTE);
//
//                                //Tratando a criticidade após a internacionalização
//                                String criticidade = bundle.getString(ProdutoCadastroActivity.CRITICIDADE);
//                                int criticidadeCod = bundle.getInt(ProdutoCadastroActivity.CRITICIDADE_CODIGO);
//                                Criticidade criticidadeTemp;
//                                switch (criticidadeCod){
//                                    case 0:
//                                        criticidadeTemp = Criticidade.Baixa;
//                                        break;
//                                    case 1:
//                                        criticidadeTemp = Criticidade.Media;
//                                        break;
//                                    case 2:
//                                        criticidadeTemp = Criticidade.Alta;
//                                        break;
//                                    default:
//                                        criticidadeTemp = Criticidade.Nenhuma;
//                                        break;
//                                }
//
//
//                                //Tratando a categoria após a internacionalização
//                                String categoria = bundle.getString(ProdutoCadastroActivity.CATEGORIA);
//                                int categoriaCod = bundle.getInt(ProdutoCadastroActivity.CATEGORIA_CODIGO);
//                                Categoria categoriaTemp;
//                                //TODO Pedir feedback se essa é a solução ideal
//                                if (categoriaCod == R.id.radioButtonHigiene){
//
//                                    categoriaTemp = Categoria.Higiene;
//                                } else if (categoriaCod == R.id.radioButtonComida) {
//                                    categoriaTemp = Categoria.Comida;
//                                } else {
//                                    categoriaTemp = Categoria.Limpeza;
//                                }
////                                boolean testeID = categoriaCod == R.id.radioButtonHigiene;
//
//
//                                Produto produto = new Produto(nome,
//                                       Integer.parseInt(quantidade),
//                                       importante,
//                                       //TODO Após tradução parou de funcionar. Tentar pelo ordinal do radio group?
//                                       //Criticidade.valueOf(criticidade),
//                                       //Categoria.valueOf(categoria));
//                                        criticidadeTemp,
//                                        categoriaTemp);


                                produtos.add(produtoNovo);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        Collections.sort(produtos, Comparator
                                                .comparing(Produto::getCategoria)
                                                .thenComparing(Produto::getNome));
                                    }else {
                                        Collections.sort(produtos ,Produto.comparatorApiUnder24);
                                    }


                                listaAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }

        );

    private void popularLista(){

        ProdutoDatabase database = ProdutoDatabase.getDatabase(this);

        produtos = database.getProdutoDao().queryAllAsc();

        listaAdapter = new ProdutoAdapter(this, produtos);
        listViewProdutos.setAdapter(listaAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar){

            ProdutoCadastroActivity.novoProduto(this, launcherNovoProduto);
            return true;
        } else if (idMenuItem == R.id.menuItemSobre) {

            SobreActivity.nova(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}