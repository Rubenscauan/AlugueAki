package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Usuario;

public class OpcoesActitivy extends AppCompatActivity {

    private static final int MINHAS_PROPOSTAS = 2;
    private static final int CASAS_QUE_ALUGUEI = 4;
    private static final int MINHAS_CASAS = 5;

    private static final int MINHAS_CASAS_ALUGADAS = 6;

    Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcoes_actitivy);
        Log.d("DEBUG", "casas do usuario: " + usuario.getCasasPAluguel());


    }

    public void minhasPropostas(View view){
        Intent intent = new Intent(this, MinhasPropostasActivity.class);
        intent.putExtra("usuario",usuario);
        startActivityForResult(intent,MINHAS_PROPOSTAS);
    }

    public void minhasCasas(View view){
        Intent intent = new Intent(this, MinhasCasasActitivy.class);
        intent.putExtra("usuario",usuario);
        startActivityForResult(intent,MINHAS_CASAS);
    }

    public void minhasCasasAlugadas(View view){
        Intent intent = new Intent(this, CasasAlugadasActitivy.class);
        intent.putExtra("usuario",usuario);
        startActivityForResult(intent,MINHAS_CASAS_ALUGADAS);
    }

    public void casasQueAluguei(View view){
        Intent intent = new Intent(this, CasasQueAlugueiActitivy.class);
        intent.putExtra("usuario",usuario);
        startActivityForResult(intent,CASAS_QUE_ALUGUEI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MINHAS_CASAS && resultCode == RESULT_OK) {
            usuario = (Usuario) data.getSerializableExtra("usuario");
        }

        if (requestCode == MINHAS_PROPOSTAS && resultCode == RESULT_OK) {
            usuario = (Usuario) data.getSerializableExtra("usuario");
    }


        if(requestCode == MINHAS_CASAS_ALUGADAS && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");

        }

        if(requestCode == CASAS_QUE_ALUGUEI && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voltar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.voltar) {
            Intent newintent = new Intent();
            newintent.putExtra("usuario",usuario);
            setResult(RESULT_OK, newintent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void voltar(View view){
        Intent newintent = new Intent();
        setResult(RESULT_OK, newintent);
        finish();
    }
}
