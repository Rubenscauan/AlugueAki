package com.example.alugueaki;

import android.app.AlertDialog;
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
import com.example.alugueaki.Models.Pedido;
import com.example.alugueaki.Models.Usuario;
import com.example.alugueaki.databinding.MinhasCasasBinding;

public class MinhasPropostasActivity extends AppCompatActivity implements MinhasCasasAdapter.OnItemClickListener  {
    private MinhasCasasBinding binding;
    private MinhasCasasAdapter minhasCasasAdapter;
    Usuario usuario =  new Usuario();
    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");


        Log.d("DEBUG", "Casas com proposta = " + usuario.getCasasComPedido());

        super.onCreate(savedInstanceState);
        binding = MinhasCasasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerViewMinhasCasas = binding.recyclerMinhasCasas1;
        recyclerViewMinhasCasas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMinhasCasas.setHasFixedSize(true);


        minhasCasasAdapter = new MinhasCasasAdapter(usuario.getCasasComPedido(),this,this);
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
            Intent newintent = new Intent();
            Log.d("DEBUG", ""+listaDeUsuarios.getUsersList());

            newintent.putExtra("listaDeUsuarios", listaDeUsuarios);
            setResult(RESULT_OK, newintent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemClicked(Casa casa) {
        Intent intent = new Intent(this, AceitarPropostaActivity.class);
        int solicitanteId = casa.getPedido().getSolicitante().getId();
        intent.putExtra("casa", casa);
        intent.putExtra("solicitanteId", solicitanteId);
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        intent.putExtra("usuario",usuario);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3) {
            if (resultCode == RESULT_OK) {


                ListaDeUsuarios novalista = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
                Usuario userATT = novalista.getDeterminadoUsuario(usuario.getId());
                Log.d("DEBUG", "" + usuario);
                Log.d("DEBUG", "" + userATT);


                usuario = userATT;

                Log.d("DEBUG", "" + usuario);


                listaDeUsuarios.setUsersList(novalista.getUsersList());

                Log.d("DEBUG", "" + novalista.getUsersList());

                Log.d("DEBUG", "" + listaDeUsuarios.getUsersList());
                minhasCasasAdapter.notifyDataSetChanged();

                Intent newintent = new Intent();
                Log.d("DEBUG", "" + listaDeUsuarios.getUsersList());

                newintent.putExtra("listaDeUsuarios", listaDeUsuarios);
                setResult(RESULT_OK, newintent);
            } else if (resultCode == RESULT_CANCELED) {
                ListaDeUsuarios novalista = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
                Usuario userATT = novalista.getDeterminadoUsuario(usuario.getId());

                usuario = userATT;

                Log.d("DEBUG", "" + usuario);

                listaDeUsuarios.setUsersList(novalista.getUsersList());

                Log.d("DEBUG", "" + novalista.getUsersList());

                Log.d("DEBUG", "" + listaDeUsuarios.getUsersList());
                minhasCasasAdapter.notifyDataSetChanged();

                Intent newintent = new Intent();
                Log.d("DEBUG", "" + listaDeUsuarios.getUsersList());

                newintent.putExtra("listaDeUsuarios", listaDeUsuarios);
                setResult(RESULT_OK, newintent);
            }
        }
    }


}
