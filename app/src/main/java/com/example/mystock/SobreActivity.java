package com.example.mystock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SobreActivity extends AppCompatActivity {

    public static void nova(AppCompatActivity activity){

        Intent intent = new Intent(activity, SobreActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sobre);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }


    //TODO Link do Linkedin Funcional
    public void abreLinkedinAutor(View view){
//        abreSite(String.valueOf(R.string.linkedinAutor));
        abreSite("https://www.linkedin.com/in/rafael-sahyoun/");
    }

    private void abreSite(String endereco){

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse(endereco));

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
        else {
            Toast.makeText(this,
                    R.string.n_o_foi_poss_vel_abrir_o_site,
                    Toast.LENGTH_LONG).show();
        }
    }
}