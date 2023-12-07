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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class MinhasPropostasActivity extends AppCompatActivity implements MinhasCasasAdapter.OnItemClickListener  {
    private MinhasCasasBinding binding;
    private MinhasCasasAdapter minhasCasasAdapter;
    Usuario usuarioQueVem =  new Usuario();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private ArrayList<Casa> casasComProposta = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarioQueVem = (Usuario) getIntent().getSerializableExtra("usuario");


        DocumentReference usuario = db.collection("usuarios").document(usuarioQueVem.getUid());

        usuario.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Encontrou o documento do usuário

                    ArrayList<Map<String, Object>> casasData = (ArrayList<Map<String, Object>>) document.get("casasComPedido");


                    if (casasData != null) {
                        for (Map<String, Object> casaData : casasData) {
                            // Crie objetos Casa com os dados e adicione ao array todasAsCasas
                            Casa casa = new Casa();
                            casa.setNome((String) casaData.get("nome"));
                            casa.setAluguel((String) casaData.get("aluguel"));
                            casa.setDescricao((String) casaData.get("descricao"));
                            casa.setTelefone((String) casaData.get("telefone"));
                            casa.setLocalizacao((String) casaData.get("localizacao"));
                            casa.setLatitude((Double) casaData.get("latitude"));
                            casa.setLongitude((Double) casaData.get("longitude"));
                            casa.setId((String) casaData.get("id"));


                            if (casaData.containsKey("pedido")) {
                                Map<String, Object> pedidoData = (Map<String, Object>) casaData.get("pedido");

                                Pedido pedido = new Pedido();
                                pedido.setUsuarioId((String) pedidoData.get("usuarioId"));
                                pedido.setUsuarioNome((String) pedidoData.get("usuarioNome"));
                                casa.setPedido(pedido);
                            }

                            casa.setUserId((String) casaData.get("userId"));
                            casa.setEndereco((String) casaData.get("endereco"));
                            casa.setImagem(R.drawable.casa1);
                            //falta carregar imagem certa
                            casasComProposta.add(casa);

                            Log.d("debug", "casas com pedido no for" + casasComProposta);
                        }
                    }
                }
            }
            usuarioQueVem.setCasasComPedido(casasComProposta);
            binding = MinhasCasasBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            RecyclerView recyclerViewMinhasCasas = binding.recyclerMinhasCasas1;
            recyclerViewMinhasCasas.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewMinhasCasas.setHasFixedSize(true);


            minhasCasasAdapter = new MinhasCasasAdapter(usuarioQueVem.getCasasComPedido(),this,this);
            recyclerViewMinhasCasas.setAdapter(minhasCasasAdapter);

            minhasCasasAdapter.notifyDataSetChanged();
        });
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
            newintent.putExtra("usuario", usuarioQueVem);
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
        intent.putExtra("casa", casa);
        intent.putExtra("usuario",usuarioQueVem);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3 && resultCode == RESULT_OK) {
            usuarioQueVem = (Usuario) data.getSerializableExtra("usuario");

            ArrayList<Casa> casasComProposta = (ArrayList<Casa>) data.getSerializableExtra("casasComProposta");

            Log.d("debug", "chegou no result code do minhas propostas" + usuarioQueVem);
            usuarioQueVem.setCasasComPedido(casasComProposta);

            minhasCasasAdapter.setCasas(usuarioQueVem.getCasasComPedido());
            minhasCasasAdapter.notifyDataSetChanged();

        }
    }




}
