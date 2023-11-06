package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alugueaki.Adapter.MinhasCasasAdapter;
import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Usuario;
import com.example.alugueaki.databinding.MinhasCasasBinding;
import com.google.android.gms.common.api.Result;

public class CasasAlugadasActitivy extends AppCompatActivity implements MinhasCasasAdapter.OnItemClickListener  {
    private MinhasCasasBinding binding;
    private MinhasCasasAdapter minhasCasasAdapter;
    Usuario usuario =  new Usuario();

    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");

        super.onCreate(savedInstanceState);
        binding = MinhasCasasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerViewMinhasCasas = binding.recyclerMinhasCasas1;
        recyclerViewMinhasCasas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMinhasCasas.setHasFixedSize(true);


        minhasCasasAdapter = new MinhasCasasAdapter(usuario.getCasasAlugadas(),this,this);
        recyclerViewMinhasCasas.setAdapter(minhasCasasAdapter);

        minhasCasasAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voltar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.voltar) {
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            intent.putExtra("usuario",usuario);
            intent.putExtra("listaDeUsuarios",listaDeUsuarios);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClicked(Casa casa) {
        Intent intent = new Intent(this, DetalhesCasaAlugadaActitivy.class);
        intent.putExtra("casa", casa);
        intent.putExtra("usuario",usuario);
        intent.putExtra("listaDeUsuarios",listaDeUsuarios);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3 && resultCode ==  RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            Log.d("DEBUG",""+ usuario.getCasasPAluguel());
            minhasCasasAdapter.notifyDataSetChanged();

        }
    }
}


