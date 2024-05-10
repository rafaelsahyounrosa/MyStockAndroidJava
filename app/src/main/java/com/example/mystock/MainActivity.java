package com.example.mystock;

import static com.example.mystock.R.string.dados_limpos;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNome, editTextQuantidade;
    private CheckBox checkBoxImportante;
    private RadioGroup radioGroupCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextNome = findViewById(R.id.editTextNome);
        editTextQuantidade = findViewById(R.id.editTextQuantidade);
        checkBoxImportante = findViewById(R.id.checkBoxImportante);
        radioGroupCategoria = findViewById(R.id.radioGroupCategoria);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }

    public void limparDados (View view){
        editTextNome.setText(null);
        editTextQuantidade.setText(null);
        checkBoxImportante.setChecked(false);
        radioGroupCategoria.clearCheck();



        editTextNome.requestFocus();
        Toast.makeText(this, dados_limpos, Toast.LENGTH_LONG).show();
    }
}