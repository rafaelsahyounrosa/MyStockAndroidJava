package com.example.mystock;

import static com.example.mystock.R.string.dados_limpos;
import static com.example.mystock.R.string.nome;
import static com.example.mystock.R.string.quantidade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProdutoCadastroActivity extends AppCompatActivity {

    public static final String NOME = "NOME";
    public static final String QUANTIDADE = "QUANTIDADE";
    public static final String IMPORTANTE = "IMPORTANTE";
    public static final String CRITICIDADE = "CRITICIDADE";
    public static final String CATEGORIA = "CATEGORIA";

    private EditText editTextNome, editTextQuantidade;
    private CheckBox checkBoxImportante;
    private RadioGroup radioGroupCategoria;
    private Spinner spinnerCriticidade;

    public static void novoProduto(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher){
        Intent intent = new Intent(activity, ProdutoCadastroActivity.class);

        launcher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_produto_cadastro);

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

    public void salvar(View view){

        String nome = editTextNome.getText().toString();
        String quantidade = editTextQuantidade.getText().toString();
        Boolean importante = checkBoxImportante.isChecked();
        String criticidade = spinnerCriticidade.getSelectedItem().toString();

        //TODO Teste para recuperar o texto que está no radioGroup
        int categoria = radioGroupCategoria.getCheckedRadioButtonId();
        String categoriaString = ((RadioButton) findViewById(categoria)).getText().toString();

//        validaDados(view, nome, quantidade, importante, criticidade, categoria);
        int dadosCorretos = validaDados(view, nome, quantidade, importante, criticidade, categoria);

        if (dadosCorretos == 1){

            Intent intent = new Intent();

            intent.putExtra(NOME, nome);
            intent.putExtra(QUANTIDADE, quantidade);
            intent.putExtra(IMPORTANTE, importante);
            intent.putExtra(CRITICIDADE, criticidade);
            intent.putExtra(CATEGORIA, categoriaString);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        return;

    }

    public int validaDados(View view, String nome, String quantidade, Boolean importante, String criticidade, int categoria){

//        String nome = editTextNome.getText().toString();
//        String quantidade = editTextQuantidade.getText().toString();
//        Boolean importante = checkBoxImportante.isChecked();
//        String criticidade = spinnerCriticidade.getSelectedItem().toString();
//        int categoria = radioGroupCategoria.getCheckedRadioButtonId();

        if(nome == null || nome.trim().isEmpty()){
            Toast.makeText(this,
                    getString(R.string.preciso_preencher_o_campo_nome_para_cadastrar),
                    Toast.LENGTH_LONG).show();
            editTextNome.requestFocus();
            return 0;
        } else if (quantidade == null || quantidade.trim().isEmpty()) {
            Toast.makeText(this,
                    getString(R.string.preciso_preecher_o_campo_de_quantidade_para_cadastrar),
                    Toast.LENGTH_LONG).show();
            editTextQuantidade.requestFocus();
            return 0;
        } else if (categoria < 0) {
            Toast.makeText(this,
                    getString(R.string.preciso_escolher_a_categoria_para_cadastrar),
                    Toast.LENGTH_LONG).show();
            radioGroupCategoria.requestFocus();
            return 0;

        } else if (importante) {
            //Lógica pois o checkbox será opcional
            if (criticidade == null || criticidade.trim().isEmpty()){
                Toast.makeText(this,
                        R.string.checkbox_importante_marcado_erro,
                        Toast.LENGTH_LONG).show();
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

    public void limparDados (View view){
        editTextNome.setText(null);
        editTextQuantidade.setText(null);
        checkBoxImportante.setChecked(false);
        radioGroupCategoria.clearCheck();
        spinnerCriticidade.setSelection(0);



        editTextNome.requestFocus();
        Toast.makeText(this, dados_limpos, Toast.LENGTH_LONG).show();
    }

    public void cancelar(View view){
        setResult(ProdutoCadastroActivity.RESULT_CANCELED);
        finish();
    }

}