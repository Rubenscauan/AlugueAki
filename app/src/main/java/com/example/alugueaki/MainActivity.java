package com.example.alugueaki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.alugueaki.Adapter.CasaAdapter;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Usuario;

import java.util.ArrayList;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements CasaAdapter.OnItemClickListener {

    private static final int REQUEST_CODE_CADASTRAR_CASA = 1; // Defina um código de solicitação adequado

    private static final int REQUEST_CODE_MINHAS_CASAS = 2;

    private static final int REQUEST_CODE_FAZER_PEDIDO = 3;
    private static final int REQUEST_CODE_CHATS = 6;
    private ActivityMainBinding binding;
    private CasaAdapter casaAdapter;


    Usuario usuario = new Usuario();
    ArrayList<Casa> todasAsCasas = new ArrayList<>();
    private int id;

    Usuario usuarioteste = new Usuario();


    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    private Casa casa = new Casa();


    @Override
    public void onItemClicked(Casa casa) {
        Intent intent = new Intent(this, DetalhesCasaActivity.class);
        intent.putExtra("casa", casa);
        intent.putExtra("usuario", usuario);
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        startActivityForResult(intent, REQUEST_CODE_FAZER_PEDIDO);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        id = (int) getIntent().getSerializableExtra("id");

        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");

        for (int i = 0; i < listaDeUsuarios.getUsersList().size(); i++) {
            todasAsCasas.addAll(listaDeUsuarios.getDeterminadoUsuario(i).getCasasPAluguel());
        }

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        RecyclerView recyclerViewCasa = binding.recyclerViewCasas1;
        recyclerViewCasa.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCasa.setHasFixedSize(true);

        casaAdapter = new CasaAdapter(todasAsCasas, this, this);
        recyclerViewCasa.setAdapter(casaAdapter);

        casaAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.config) {
            Intent intent = new Intent(this, OpcoesActitivy.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra("listaDeUsuarios", listaDeUsuarios);
            startActivityForResult(intent,REQUEST_CODE_MINHAS_CASAS);
            return true;
        } else if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent();
            intent.putExtra("usuario", usuario);
            intent.putExtra("listaDeUsuarios", listaDeUsuarios);
            intent.putExtra("id", id);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }else if(item.getItemId() == R.id.addCasa){
            Intent intent = new Intent(this, CasaCadastroActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CADASTRAR_CASA);
            return true;
        }else if(item.getItemId() == R.id.chat){
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("listaDeUsuarios", listaDeUsuarios);
            intent.putExtra("usuario", usuario);
            startActivityForResult(intent, REQUEST_CODE_CHATS);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MINHAS_CASAS && resultCode == RESULT_OK){

            ListaDeUsuarios novalista = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            listaDeUsuarios.setUsersList(novalista.getUsersList());

            Log.d("DEBUG","Primeiro teste "+usuario);

            usuario = (Usuario) data.getSerializableExtra("usuario");

            Log.d("DEBUG","segundo teste "+usuario);

            listaDeUsuarios.setUsersList(novalista.getUsersList());
            todasAsCasas.clear();
            for(int i = 0; i < listaDeUsuarios.getUsersList().size(); i++){
                todasAsCasas.addAll(listaDeUsuarios.getDeterminadoUsuario(i).getCasasPAluguel());
            }
            Log.d("DEBUG", "Lista de usuarios: " + listaDeUsuarios);
            Log.d("DEBUG", "todas as casas" + todasAsCasas);

            casaAdapter.notifyDataSetChanged();
        }


        if(requestCode == REQUEST_CODE_FAZER_PEDIDO && resultCode == RESULT_OK) {
            /*Casa casa = (Casa) data.getSerializableExtra("casa");
            int userId = casa.getUserId();
            Log.d("DEBUG", "casa recebida: " + casa);

            Usuario usuario = listaDeUsuarios.getDeterminadoUsuario(userId);

            for (int i = 0; i < usuario.getCasasPAluguel().size(); i++) {
                if (usuario.getDeterminadaCasaPAluguel(i).getId() == casa.getId()) {
                    Log.d("DEBUG", "casa obtida: " + casa);
                    usuario.addPedidoDeCasa(casa);
                    listaDeUsuarios.setUsuario(userId,usuario);
                    Log.d("DEBUG", "Usuario dentro do for" + listaDeUsuarios.getDeterminadoUsuario(userId));
                    break;
                }
            }*/
            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");

            Log.d("DEBUG", "" + listaDeUsuarios);
        }


        if (requestCode == REQUEST_CODE_CADASTRAR_CASA && resultCode == RESULT_OK) {

            Casa novaCasa = (Casa) data.getSerializableExtra("novaCasa");

            Casa casa = new Casa(
                    usuario.getId(),
                    id,
                    usuario.getNome(),
                    novaCasa.getTelefone(),
                    novaCasa.getDescricao(),
                    novaCasa.getEndereco(),
                    novaCasa.getLocalizacao(),
                    novaCasa.getLatitude(),
                    novaCasa.getLongitude(),
                    "R$:" + novaCasa.getAluguel(),
                    R.drawable.casa1);


            usuario.addCasaPAlugar(casa);

            todasAsCasas.add(casa);

            casaAdapter.notifyDataSetChanged();
            listaDeUsuarios.setUsuario(usuario.getId(),usuario);

            Log.d("DEBUG","Todas as casas: " + todasAsCasas);

            Log.d("DEBUG", "Lista de usuarios: " + listaDeUsuarios);

            Log.d("DEBUG", "casa adicionada: " + usuario.getCasasPAluguel());

            Log.d("DEBUG", "id no momento: " + id);

        }

        if(requestCode == REQUEST_CODE_CHATS && resultCode == RESULT_OK){
            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
        }

    }






}