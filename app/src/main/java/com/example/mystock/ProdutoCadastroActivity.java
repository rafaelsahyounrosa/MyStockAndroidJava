package com.example.mystock;

import static com.example.mystock.R.string.dados_limpos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mystock.utils.UtilsGUI;

import java.util.ArrayList;

public class ProdutoCadastroActivity extends AppCompatActivity {

    public static final String NOME = "NOME";
    public static final String QUANTIDADE = "QUANTIDADE";
    public static final String IMPORTANTE = "IMPORTANTE";
    public static final String CRITICIDADE = "CRITICIDADE";
    public static final String CRITICIDADE_CODIGO = "CRITICIDADE_CODIGO";
    public static final String CATEGORIA = "CATEGORIA";
    public static final String CATEGORIA_CODIGO = "CATEGORIA_CODIGO";
    public static final String SUGERIR_PREENCHIMENTO = "SUGERIR_PREENCHIMENTO";
    public static final String ULTIMA_CATEGORIA = "ULTIMA_CATEGORIA";
    private boolean sugerirPreenchimento = false;
    private int ultimaCategoria = 0;

    public static final String MODO = "MODO";
    public static final int NOVO = 1;
    public static final int EDITAR = 2;
    private int modo;
    private String nomeOriginal;
    private int quantidadeOriginal;
    private boolean importanteOriginal;
    private Criticidade criticidadeOriginal;
    private Categoria categoriaOriginal;

    private EditText editTextNome, editTextQuantidade;
    private CheckBox checkBoxImportante;
    private RadioGroup radioGroupCategoria;
    private Spinner spinnerCriticidade;

    public static void novoProduto(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher){
        Intent intent = new Intent(activity, ProdutoCadastroActivity.class);

        intent.putExtra(MODO, NOVO);

        launcher.launch(intent);
    }
    public static void editarProduto(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher, Produto produto){
        Intent intent = new Intent(activity, ProdutoCadastroActivity.class);

        intent.putExtra(MODO, EDITAR);
        intent.putExtra(NOME, produto.getNome());
        intent.putExtra(QUANTIDADE, produto.getQuantidade());
        intent.putExtra(IMPORTANTE, produto.isImportante());
        intent.putExtra(CRITICIDADE, produto.getCriticidade().name());
        intent.putExtra(CATEGORIA, produto.getCategoria().name());

        launcher.launch(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_produto_cadastro);

        ActionBar actionBar = getSupportActionBar();
        lerSugerirPreenchimento();
        lerUltimaCategoria();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextNome = findViewById(R.id.editTextNome);
        editTextQuantidade = findViewById(R.id.editTextQuantidade);
        checkBoxImportante = findViewById(R.id.checkBoxImportante);
        radioGroupCategoria = findViewById(R.id.radioGroupCategoria);
        spinnerCriticidade = findViewById(R.id.spinnerCriticidade);
        spinnerCriticidade.setEnabled(false);

        popularSpinner();

        checkBoxImportante.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerCriticidade.setEnabled(isChecked);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            modo = bundle.getInt(MODO, NOVO);

            if (modo == NOVO){
                setTitle(getString(R.string.novo_produto));

                if(sugerirPreenchimento){

                    radioGroupCategoria.check(ultimaCategoria);
                }

            } else if (modo == EDITAR) {
                setTitle(getString(R.string.editar_produto));

                nomeOriginal = bundle.getString(NOME);
                quantidadeOriginal = bundle.getInt(QUANTIDADE);
                importanteOriginal = bundle.getBoolean(IMPORTANTE);
                criticidadeOriginal = Criticidade.valueOf(bundle.getString(CRITICIDADE));
                categoriaOriginal = Categoria.valueOf(bundle.getString(CATEGORIA));

                editTextNome.setText(nomeOriginal);
                editTextQuantidade.setText(String.valueOf(quantidadeOriginal));
                checkBoxImportante.setChecked(importanteOriginal);
                //TODO Melhorar tratamento para setar criticidade quando não for importante (marcar qualquer uma?)
                if (criticidadeOriginal == Criticidade.Nenhuma){
                    spinnerCriticidade.setSelection(0);
                }else {
                    spinnerCriticidade.setSelection(criticidadeOriginal.ordinal());

                }

                //TODO Melhorar tratamento para recuperar o radioID
                //radioGroupCategoria.check(categoriaOriginal.ordinal());
                if (categoriaOriginal == Categoria.Comida){
                    radioGroupCategoria.check(R.id.radioButtonComida);
                } else if (categoriaOriginal == Categoria.Limpeza) {
                    radioGroupCategoria.check(R.id.radioButtonLimpeza);
                }else {
                    radioGroupCategoria.check(R.id.radioButtonHigiene);
                }


                //editTextNome.setSelection(editTextNome.getText().length());
                if (editTextNome.getText() != null && editTextNome.getText().length() > 0) {
                    editTextNome.setSelection(editTextNome.getText().length());
                }

            }
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//
//        });


    }

//    public CompoundButton.OnCheckedChangeListener setSpinnerCriticidadeEnable(){
//
//        spinnerCriticidade.setEnabled(checkBoxImportante.isChecked());
//        return null;
//    }


    private void popularSpinner(){
        ArrayList<String> listaCriticidade = new ArrayList<>();

        listaCriticidade.add(getString(R.string.baixa));
        listaCriticidade.add(getString(R.string.media));
        listaCriticidade.add(getString(R.string.alta));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                                            android.R.layout.simple_list_item_1,
                                                            listaCriticidade);

        spinnerCriticidade.setAdapter(adapter);
    }

    public void salvar(){

        String nome = editTextNome.getText().toString();
        String quantidade = editTextQuantidade.getText().toString();
        Boolean importante = checkBoxImportante.isChecked();
        String criticidade = spinnerCriticidade.getSelectedItem().toString();

        //TODO Teste para recuperar o texto que está no radioGroup
        int categoria = radioGroupCategoria.getCheckedRadioButtonId();
        int criticidadeCod = spinnerCriticidade.getSelectedItemPosition();


//        validaDados(view, nome, quantidade, importante, criticidade, categoria);
        int dadosCorretos = validaDados(nome, quantidade, importante, criticidade, categoria);

        if (dadosCorretos == 1){

            salvarUltimaCategoria(categoria);

            String categoriaString = ((RadioButton) findViewById(categoria)).getText().toString();

            Intent intent = new Intent();

            intent.putExtra(NOME, nome);
            intent.putExtra(QUANTIDADE, quantidade);
            intent.putExtra(IMPORTANTE, importante);
            intent.putExtra(CRITICIDADE, criticidade);
            intent.putExtra(CATEGORIA, categoriaString);
            intent.putExtra(CATEGORIA_CODIGO, categoria);
            intent.putExtra(CRITICIDADE_CODIGO, criticidadeCod);


            setResult(Activity.RESULT_OK, intent);
            finish();
        }

    }

    public int validaDados(String nome, String quantidade, Boolean importante, String criticidade, int categoria){

//        String nome = editTextNome.getText().toString();
//        String quantidade = editTextQuantidade.getText().toString();
//        Boolean importante = checkBoxImportante.isChecked();
//        String criticidade = spinnerCriticidade.getSelectedItem().toString();
//        int categoria = radioGroupCategoria.getCheckedRadioButtonId();

        if(nome == null || nome.trim().isEmpty()){
            UtilsGUI.aviso(this, R.string.preciso_preencher_o_campo_nome_para_cadastrar);
            editTextNome.requestFocus();
            return 0;
        } else if (quantidade == null || quantidade.trim().isEmpty()) {
            UtilsGUI.aviso(this, R.string.preciso_preecher_o_campo_de_quantidade_para_cadastrar);
            editTextQuantidade.requestFocus();
            return 0;
        } else if (/*categoria = null ||*/ categoria < 0) {
            UtilsGUI.aviso(this, R.string.preciso_escolher_a_categoria_para_cadastrar);
            radioGroupCategoria.requestFocus();
            return 0;

        } else if (importante) {
            //Lógica pois o checkbox será opcional
            if (criticidade == null || criticidade.trim().isEmpty()){
                UtilsGUI.aviso(this, R.string.checkbox_importante_marcado_erro);
                return 0;
            }
        }

        Toast.makeText(this,
                getString(R.string.dados_selecionados) +
                        "\n" + getString(R.string.nomeLabel) + " " + nome +
                        "\n" + getString(R.string.quantidadeLabel) + " " + quantidade +
                        "\n" + getString(R.string.checkBoxImportante) + " " + importante +
                        "\n" + getString(R.string.criticidadeLabel) + " " + criticidade +
                        "\n" + getString(R.string.textViewCategoria) + " " + categoria
                ,
                Toast.LENGTH_LONG).show();
        return 1;
    }

    public void limparDados (){
        editTextNome.setText(null);
        editTextQuantidade.setText(null);
        checkBoxImportante.setChecked(false);
        radioGroupCategoria.clearCheck();
        spinnerCriticidade.setSelection(0);



        editTextNome.requestFocus();
        Toast.makeText(this, dados_limpos, Toast.LENGTH_LONG).show();
    }

    public void cancelar(){
        setResult(ProdutoCadastroActivity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.produto_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.sugerirCampos);
        item.setChecked(sugerirPreenchimento);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemSalvar){
            salvar();
            return true;
        } else if (idMenuItem == R.id.menuItemLimpar) {
            limparDados();
            return true;

        } else if (idMenuItem == R.id.sugerirCampos) {
            boolean valor = !item.isChecked();
            salvarSugerirPreenchimento(valor);
            item.setChecked(valor);

            if (sugerirPreenchimento){
                radioGroupCategoria.check(ultimaCategoria);
            }

            return true;

        } else if (idMenuItem == android.R.id.home) {
            cancelar();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void lerSugerirPreenchimento(){

        SharedPreferences shared = getSharedPreferences(ListagemActivity.ARQUIVO, Context.MODE_PRIVATE);

        sugerirPreenchimento = shared.getBoolean(SUGERIR_PREENCHIMENTO, sugerirPreenchimento);
    }

    private void salvarSugerirPreenchimento(boolean novoValor){

        SharedPreferences shared = getSharedPreferences(ListagemActivity.ARQUIVO, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(SUGERIR_PREENCHIMENTO, novoValor);

        editor.commit();

        sugerirPreenchimento = novoValor;

    }

    private void lerUltimaCategoria(){

        SharedPreferences shared = getSharedPreferences(ListagemActivity.ARQUIVO, Context.MODE_PRIVATE);

        ultimaCategoria = shared.getInt(ULTIMA_CATEGORIA, ultimaCategoria);
    }

    private void salvarUltimaCategoria(int novoValor){

        SharedPreferences shared = getSharedPreferences(ListagemActivity.ARQUIVO, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(ULTIMA_CATEGORIA, novoValor);

        editor.commit();

        ultimaCategoria = novoValor;

    }
}