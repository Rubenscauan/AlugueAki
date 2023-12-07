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
import com.example.alugueaki.Models.Chat;
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
import java.util.List;
import java.util.Map;

public class DetalhesCasaActivity extends AppCompatActivity {
    private GoogleMap mMap;

    private final int REQUEST_CODE_CHAT = 1;
    Casa casa = new Casa();
    private String chatId = null;

    Usuario usuario = new Usuario();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_casa);
        casa = (Casa) getIntent().getSerializableExtra("casa");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        TextView textTitulo = findViewById(R.id.textTitulo);
        TextView textDescricao = findViewById(R.id.textDescricao);
        TextView textEndereco = findViewById(R.id.textEndereco);

        textTitulo.setText(casa.getNome());
        textDescricao.setText(casa.getDescricao());
        textEndereco.setText(casa.getEndereco());

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
            Intent intent = new Intent();
            intent.putExtra("usuario",usuario);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void solicitarAluguel(View view){
        Log.d("debug", "casa antes do pedido ser setado " + casa);
        casa.setPedido(new Pedido(usuario.getUid(), usuario.getNome()));
        Log.d("debug", "casa depois do pedido ser setado " + casa);


        DocumentReference donoDaCasaRef = db.collection("usuarios").document(casa.getUserId());

        donoDaCasaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Encontrou o documento do usuário

                    ArrayList<Casa> casasComPedido = new ArrayList<>();
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
                            casasComPedido.add(casa);

                            Log.d("debug" , "casas com pedido no for" + casasComPedido );
                        }
                    }


                    casasComPedido.add(casa);
                    Log.d("debug" , "casas com pedido no final" + casasComPedido );

                    // Adicionar o pedido à lista de pedidos do usuário.add(casa);


                    // Atualizar os dados do usuário no Firestore
                    donoDaCasaRef.update("casasComPedido", casasComPedido)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Sucesso ao atualizar o Firestore
                                    Toast.makeText(DetalhesCasaActivity.this, "Pedido enviado com sucesso", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Falha ao atualizar o Firestore
                                    Toast.makeText(DetalhesCasaActivity.this, "Falha ao enviar o pedido", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Log.d("DEBUG", "Documento do usuário não encontrado");
                }
            } else {
                Log.d("DEBUG", "Falha ao obter dados do Firestore: ", task.getException());
            }
        });
    }

    public void iniciarChat(View view){
        chatId = db.collection("chats").document().getId();
        Chat chat = new Chat(chatId, usuario.getUid(), casa.getUserId(),usuario.getNome(),casa.getNome());

        Intent intent = new Intent(this, MensagemActitivy.class);
        intent.putExtra("casa", casa);
        intent.putExtra("chat", chat);
        intent.putExtra("usuario", usuario);

        startActivityForResult(intent, REQUEST_CODE_CHAT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHAT && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
            casa = (Casa) data.getSerializableExtra("casa");
        }
    }


}
