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
import com.example.alugueaki.Models.Usuario;
import com.example.alugueaki.databinding.MinhasCasasBinding;

import java.util.ArrayList;

public class MinhasCasasActitivy extends AppCompatActivity implements MinhasCasasAdapter.OnItemClickListener  {
    private MinhasCasasBinding binding;
    private MinhasCasasAdapter minhasCasasAdapter;
    Usuario usuario =  new Usuario();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        Log.d("DEBUG", "Casas= " + usuario.getCasasPAluguel());

        super.onCreate(savedInstanceState);
        binding = MinhasCasasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerViewMinhasCasas = binding.recyclerMinhasCasas1;
        recyclerViewMinhasCasas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMinhasCasas.setHasFixedSize(true);

        minhasCasasAdapter = new MinhasCasasAdapter(usuario.getCasasPAluguel(),this,this);
        recyclerViewMinhasCasas.setAdapter(minhasCasasAdapter);

        minhasCasasAdapter.notifyDataSetChanged();
    }

    public void Voltar(View view){

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
            Log.d("DEBUG", ""+ usuario);
            newintent.putExtra("usuario", usuario);
            setResult(RESULT_OK, newintent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemClicked(Casa casa) {
        Intent intent = new Intent(this, EditarCasaActitivy.class);
        intent.putExtra("casa", casa);
        intent.putExtra("usuario",usuario);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3 && resultCode ==  RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
            Log.d("DEBUG",""+ usuario.getCasasPAluguel());
            minhasCasasAdapter.notifyDataSetChanged();

        }
    }
}


