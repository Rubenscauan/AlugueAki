package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Pedido;
import com.example.alugueaki.Models.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetalhesCasaAlugadaActitivy extends AppCompatActivity {
    private GoogleMap mMap;

    private Casa casa = new Casa();
    private Usuario usuario = new Usuario();
    private Usuario solicitante = new Usuario();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_casa_alugada);
        casa = (Casa) getIntent().getSerializableExtra("casa");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        Log.d("DEBUG", "" + casa);
        TextView textTitulo = findViewById(R.id.textTitulo);
        TextView textDescricao = findViewById(R.id.textDescricao);
        TextView textEndereco = findViewById(R.id.textEndereco);
        TextView textInquilino = findViewById(R.id.textInquilino);

        textTitulo.setText(usuario.getNome());
        textDescricao.setText(casa.getDescricao());
        textEndereco.setText(casa.getEndereco());
        textInquilino.setText(casa.getPedido().getUsuarioNome());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng locCasa = new LatLng(casa.getLatitude(), casa.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(locCasa)
                        .title("LocCasa"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locCasa,17.0f));

            }

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
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void encerrarContrato(View view){

        ArrayList<Casa> casasQueOPedinteAlugou = new ArrayList<>();
        DocumentReference pedinteRef = db.collection("usuarios").document(casa.getPedido().getUsuarioId());

        pedinteRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Encontrou o documento do usuário
                    ArrayList<Map<String, Object>> casasData = (ArrayList<Map<String, Object>>) document.get("casasQueAluguei");


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
                            casasQueOPedinteAlugou.add(casa);

                        }
                    }
                }
            }
        });

        usuario.removeCasaAlugada(casa.getId());
        usuario.addCasaPAlugar(casa);

        solicitante.setCasasQueAluguei(casasQueOPedinteAlugou);
        solicitante.removeCasaQueAluguei(casa.getId());

        DocumentReference donoDaCasaRef = db.collection("usuarios").document(casa.getUserId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("casasAlugadas", usuario.getCasasAlugadas());
        updates.put("casasPAluguel", usuario.getCasasPAluguel());

        // Atualizar os dados do usuário no Firestore
        donoDaCasaRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Sucesso ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaAlugadaActitivy.this, "Proposta aceita com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaAlugadaActitivy.this, "Falha ao aceitar proposta", Toast.LENGTH_SHORT).show();
                    }
                });


        Map<String, Object> updatesPedinte = new HashMap<>();
        updatesPedinte.put("casasQueAluguei",casasQueOPedinteAlugou);
        pedinteRef.update(updatesPedinte)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Sucesso ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaAlugadaActitivy.this, "Proposta aceita com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaAlugadaActitivy.this, "Falha ao aceitar proposta", Toast.LENGTH_SHORT).show();
                    }
                });


        Intent intent = new Intent();
        intent.putExtra("usuario",usuario);
        setResult(RESULT_OK,intent);
        finish();

    }






}
