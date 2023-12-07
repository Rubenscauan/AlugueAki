package com.example.alugueaki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.alugueaki.Adapter.CasaAdapter;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;


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


    private Casa casa = new Casa();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        id = (int) getIntent().getSerializableExtra("id");


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String nomeDoUsuario = currentUser.getDisplayName();
        Log.d("DEBUG", "casas do usuario: " + usuario.getCasasPAluguel());


        FirebaseFirestore.getInstance().collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nome = document.getString("nome");
                            String email = document.getString("email");
                            ArrayList<Map<String, Object>> casasData = (ArrayList<Map<String, Object>>) document.get("casasPAluguel");


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


                                    casa.setUserId((String) casaData.get("userId"));
                                    casa.setEndereco((String) casaData.get("endereco"));
                                    casa.setImagemURL((String) casaData.get("imagemURL"));

                                    todasAsCasas.add(casa);
                                    Log.d("debug", "todas as casas no momento = " + todasAsCasas);

                                }

                                // Configurar o adaptador e atualizar
                                binding = ActivityMainBinding.inflate(getLayoutInflater());
                                setContentView(binding.getRoot());
                                getSupportActionBar().setDisplayShowTitleEnabled(false);

                                RecyclerView recyclerViewCasa = binding.recyclerViewCasas1;
                                recyclerViewCasa.setLayoutManager(new LinearLayoutManager(this));
                                recyclerViewCasa.setHasFixedSize(true);

                                casaAdapter = new CasaAdapter(todasAsCasas, this, this);
                                recyclerViewCasa.setAdapter(casaAdapter);

                                casaAdapter.notifyItemInserted(todasAsCasas.size()-1);
                                Log.d("debug", "todas as casas" + todasAsCasas);

                            }

                            // Faça algo com os dados obtidos (por exemplo, exibir no console ou em uma lista)
                            Log.d("DADOS_USUARIO", "Nome: " + nome + ", Email: " + email + " , casas =" + casasData);
                        }
                    } else {
                        // Falha ao obter dados
                        Log.e("ERRO", "Falha ao obter dados de usuários.", task.getException());
                    }
                });



        Log.d("debug","'-'"+todasAsCasas);




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
            startActivityForResult(intent,REQUEST_CODE_MINHAS_CASAS);
            return true;
        }else if(item.getItemId() == R.id.logout) {
            Intent intent = new Intent();
            intent.putExtra("usuario", usuario);
            intent.putExtra("id", id);
            setResult(RESULT_OK, intent);
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        }else if(item.getItemId() == R.id.addCasa){
            Intent intent = new Intent(this, CasaCadastroActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CADASTRAR_CASA);
            return true;
        }else if(item.getItemId() == R.id.chat){
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("usuario", usuario);
            Log.d("debug", "usuario id" + usuario.getUid());
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

            usuario = (Usuario) data.getSerializableExtra("usuario");
            casaAdapter.notifyDataSetChanged();
        }


        if (requestCode == REQUEST_CODE_CADASTRAR_CASA && resultCode == RESULT_OK) {

            Casa novaCasa = (Casa) data.getSerializableExtra("novaCasa");
            String imagemUrl = novaCasa.getImagemURL();

            DocumentReference usuarioRef = db.collection("usuarios").document(currentUser.getUid());

            //adquire o nome do usuario e constroi a casa para adicionar no BD

            usuarioRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Documento encontrado, você pode acessar os dados
                        String nomeUsuario = document.getString("nome");

                        String idCasa = db.collection("casas").document().getId();
                        Casa casa = new Casa(
                                currentUser.getUid(),
                                idCasa,
                                nomeUsuario,
                                novaCasa.getTelefone(),
                                novaCasa.getDescricao(),
                                novaCasa.getEndereco(),
                                novaCasa.getLocalizacao(),
                                novaCasa.getLatitude(),
                                novaCasa.getLongitude(),
                                "R$:" + novaCasa.getAluguel(),
                                novaCasa.getImagemURL());

                        // Adicionar a nova casa ao array 'casasPAluguel' do usuário
                        usuario.addCasaPAlugar(casa);
                        todasAsCasas.add(casa);
                        casaAdapter.notifyDataSetChanged();
                        Log.d("debug", "todas as casas" + todasAsCasas);

                        // Atualizar o array 'casasPAluguel' e o ID da nova casa no Firestore
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("casasPAluguel", usuario.getCasasPAluguel());


                        usuarioRef.update(updateData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Sucesso ao atualizar o Firestore
                                        Toast.makeText(MainActivity.this, "Casa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Falha ao atualizar o Firestore
                                        Toast.makeText(MainActivity.this, "Falha ao adicionar a casa", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Log.d("DEBUG", "Documento não encontrado");
                    }
                } else {
                    Log.d("DEBUG", "Falha ao obter dados do Firestore: ", task.getException());
                }
            });




        }

        if(requestCode == 4 && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
        }

    }


    @Override
    public void onItemClicked(Casa casa) {
        Intent intent = new Intent(this, DetalhesCasaActivity.class);
        intent.putExtra("casa", casa);
        intent.putExtra("usuario", usuario);
        startActivityForResult(intent,4);
    }
}