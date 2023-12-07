package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class DetalhesCasaQueAluguei extends AppCompatActivity {
    private GoogleMap mMap;

    private Casa casa = new Casa();
    private Usuario usuario = new Usuario();
    private Usuario dono = new Usuario();

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_casa_alugada);
        casa = (Casa) getIntent().getSerializableExtra("casa");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");



        TextView textTitulo = findViewById(R.id.textTitulo);
        TextView textDescricao = findViewById(R.id.textDescricao);
        TextView textEndereco = findViewById(R.id.textEndereco);
        TextView textInquilino = findViewById(R.id.textInquilino);

        Log.d("DEBUG", "" + casa);

        textTitulo.setText(dono.getNome());
        textDescricao.setText(casa.getDescricao());
        textEndereco.setText(casa.getEndereco());
        textInquilino.setText(usuario.getNome());

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

        ArrayList<Casa> casasPAluguelDono = new ArrayList<>();
        ArrayList<Casa> casasAlugadasDono = new ArrayList<>();

        DocumentReference donoRef = db.collection("usuarios").document(casa.getUserId());

        donoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Encontrou o documento do usuário
                    ArrayList<Map<String, Object>> casasDataAluguel = (ArrayList<Map<String, Object>>) document.get("casasPAluguel");


                    if (casasDataAluguel != null) {
                        for (Map<String, Object> casaData : casasDataAluguel) {
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
                            casasPAluguelDono.add(casa);

                        }
                    }
                    ArrayList<Map<String, Object>> casasDataAluguei = (ArrayList<Map<String, Object>>) document.get("casasAlugadas");

                    if (casasDataAluguei != null) {
                        for (Map<String, Object> casaData : casasDataAluguei) {
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
                            casasAlugadasDono.add(casa);

                        }
                    }
                }
            }
        });

        dono.setCasasPAluguel(casasPAluguelDono);
        dono.setCasasAlugadas(casasAlugadasDono);
        dono.removeCasaAlugada(casa.getId());
        dono.addCasaPAlugar(casa);

        Map<String, Object> updates = new HashMap<>();
        updates.put("casasAlugadas", dono.getCasasAlugadas());
        updates.put("casasPAluguel", dono.getCasasPAluguel());
        donoRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Sucesso ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaQueAluguei.this, "Proposta aceita com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaQueAluguei.this, "Falha ao aceitar proposta", Toast.LENGTH_SHORT).show();
                    }
                });


        Log.d("debug", "casas antes :" + usuario.getCasasQueAluguei());

        usuario.removeCasaQueAluguei(casa.getId());

        Log.d("debug", "casas dps :" + usuario.getCasasQueAluguei());

        DocumentReference usuarioRef = db.collection("usuarios").document(casa.getPedido().getUsuarioId());

        Map<String, Object> updates2 = new HashMap<>();
        updates2.put("casasQueAluguei", usuario.getCasasQueAluguei());

        // Atualizar os dados do usuário no Firestore
        usuarioRef.update(updates2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Sucesso ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaQueAluguei.this, "Contrato encerrado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DetalhesCasaQueAluguei.this, "Erro ao encerrar o contrato", Toast.LENGTH_SHORT).show();
                    }
                });




        Log.d("debug", "casas dps :" + usuario.getCasasQueAluguei());
        Intent intent = new Intent();
        intent.putExtra("usuario",usuario);
        setResult(RESULT_OK,intent);
        finish();
    }
}
