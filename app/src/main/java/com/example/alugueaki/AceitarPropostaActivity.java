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
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.Source;

    import java.lang.reflect.Array;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;

    public class AceitarPropostaActivity extends AppCompatActivity {

        Casa casa = new Casa();

        Usuario usuario = new Usuario();

        Usuario pedinte = new Usuario();


        FirebaseFirestore db = FirebaseFirestore.getInstance();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            casa = (Casa) getIntent().getSerializableExtra("casa");
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.aceitar_proposta);

            TextView textUsername = findViewById(R.id.textUsername);
            TextView textDescricao = findViewById(R.id.textDescricao);
            TextView textEndereco = findViewById(R.id.textEndereco);

            textUsername.setText(casa.getPedido().getUsuarioNome());
            textDescricao.setText(casa.getDescricao());
            textEndereco.setText(casa.getEndereco());

            Log.d("Debug","usuario: "+ usuario);
            Log.d("DEBUG", "casa: " + casa);
        }

        public void aceitarProposta(View view){
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

            usuario.removeCasaComPedido(casa.getId());
            usuario.removeCasaPAluguel(casa.getId());
            usuario.addCasaAlugada(casa);
            casasQueOPedinteAlugou.add(casa);


            DocumentReference donoDaCasaRef = db.collection("usuarios").document(casa.getUserId());

            Map<String, Object> updates = new HashMap<>();
            updates.put("casasAlugadas", usuario.getCasasAlugadas());
            updates.put("casasComPedido", usuario.getCasasComPedido());
            updates.put("casasPAluguel", usuario.getCasasPAluguel());

            // Atualizar os dados do usuário no Firestore
            donoDaCasaRef.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                                        // Sucesso ao atualizar o Firestore
                            Toast.makeText(AceitarPropostaActivity.this, "Proposta aceita com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Falha ao atualizar o Firestore
                            Toast.makeText(AceitarPropostaActivity.this, "Falha ao aceitar proposta", Toast.LENGTH_SHORT).show();
                        }
                    });






            Map<String, Object> updatesPedinte = new HashMap<>();
            updatesPedinte.put("casasQueAluguei",casasQueOPedinteAlugou);
            pedinteRef.update(updatesPedinte)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Sucesso ao atualizar o Firestore
                            Toast.makeText(AceitarPropostaActivity.this, "Proposta aceita com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Falha ao atualizar o Firestore
                            Toast.makeText(AceitarPropostaActivity.this, "Falha ao aceitar proposta", Toast.LENGTH_SHORT).show();
                        }
                    });
            Intent intent = new Intent();
            intent.putExtra("usuario", usuario);
            intent.putExtra("casasComProposta", usuario.getCasasComPedido());
            setResult(RESULT_OK, intent);
            finish();


        }

        public void rejeitarProposta(View view){
            usuario.removeCasaComPedido(casa.getId());

            DocumentReference donoDaCasaRef = db.collection("usuarios").document(casa.getUserId());

            Map<String, Object> updates = new HashMap<>();
            updates.put("casasComPedido", usuario.getCasasComPedido());

            // Atualizar os dados do usuário no Firestore
            donoDaCasaRef.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Sucesso ao atualizar o Firestore
                            Toast.makeText(AceitarPropostaActivity.this, "Proposta rejeitada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Falha ao atualizar o Firestore
                            Toast.makeText(AceitarPropostaActivity.this, "Falha ao rejeitar proposta", Toast.LENGTH_SHORT).show();
                        }
                    });
            Intent intent = new Intent();
            intent.putExtra("usuario", usuario);
            intent.putExtra("casasComProposta", usuario.getCasasComPedido());
            setResult(RESULT_OK, intent);
            finish();
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
    }

