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

    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcoes_actitivy);

    }

    public void minhasPropostas(View view){
        Intent intent = new Intent(this, MinhasPropostasActivity.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        startActivityForResult(intent,MINHAS_PROPOSTAS);
    }

    public void minhasCasas(View view){
        Intent intent = new Intent(this, MinhasCasasActitivy.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        startActivityForResult(intent,MINHAS_CASAS);
    }

    public void minhasCasasAlugadas(View view){
        Intent intent = new Intent(this, CasasAlugadasActitivy.class);
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        intent.putExtra("usuario",usuario);
        startActivityForResult(intent,MINHAS_CASAS_ALUGADAS);
    }

    public void casasQueAluguei(View view){
        Intent intent = new Intent(this, CasasQueAlugueiActitivy.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        startActivityForResult(intent,CASAS_QUE_ALUGUEI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MINHAS_PROPOSTAS && resultCode == RESULT_OK) {
            ListaDeUsuarios novalista = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            Usuario userATT = novalista.getDeterminadoUsuario(usuario.getId());
            Log.d("DEBUG", ""+ usuario);
            Log.d("DEBUG", ""+ userATT);


            usuario = userATT;

            Log.d("DEBUG", ""+ usuario);


            listaDeUsuarios.setUsersList(novalista.getUsersList());

            Log.d("DEBUG", "" + novalista.getUsersList());

            Log.d("DEBUG", "" + listaDeUsuarios.getUsersList());

        }
        if(requestCode == MINHAS_CASAS && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
            listaDeUsuarios.setUsuario(usuario.getId(),usuario);
            Log.d("Debug", "" + listaDeUsuarios);
            Log.d("Debug", "" + usuario);
        }

        if(requestCode == MINHAS_CASAS_ALUGADAS && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            Log.d("Debug", "" + listaDeUsuarios);
            Log.d("Debug", "" + usuario);
        }

        if(requestCode == CASAS_QUE_ALUGUEI && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            Log.d("Debug", "" + listaDeUsuarios);
            Log.d("Debug", "" + usuario);
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
            Log.d("DEBUG", ""+listaDeUsuarios.getUsersList());
            newintent.putExtra("listaDeUsuarios", listaDeUsuarios);
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
        Log.d("DEBUG", ""+listaDeUsuarios.getUsersList());
        newintent.putExtra("listaDeUsuarios", listaDeUsuarios);
        setResult(RESULT_OK, newintent);
        finish();
    }
}
