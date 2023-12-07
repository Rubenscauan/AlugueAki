package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alugueaki.Adapter.CasaAdapter;
import com.example.alugueaki.Adapter.MensagemAdapter;
import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.Chat;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Mensagem;
import com.example.alugueaki.Models.Pedido;
import com.example.alugueaki.Models.Usuario;
import com.example.alugueaki.databinding.ActitivyDeterminadoChatBinding;
import com.example.alugueaki.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MensagemActitivy extends AppCompatActivity {

    Casa casa = new Casa();
    Usuario usuario = new Usuario();
    Chat chat = new Chat();

    private ActitivyDeterminadoChatBinding binding;
    private MensagemAdapter mensagemAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mensagensRef;


    private EditText edtMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivy_determinado_chat);

        casa = (Casa) getIntent().getSerializableExtra("casa");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        chat = (Chat) getIntent().getSerializableExtra("chat");

        usuario.addChat(chat);


        binding = ActitivyDeterminadoChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView recyclerViewChat = binding.recyclerViewChat;
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setHasFixedSize(true);

        mensagemAdapter = new MensagemAdapter(chat.getMensagens(), this);
        recyclerViewChat.setAdapter(mensagemAdapter);
        edtMensagem = findViewById(R.id.conteudoMensagem);
        mensagemAdapter.notifyDataSetChanged();

    }

    public void enviarMensagem(View view) {
        String conteudo = edtMensagem.getText().toString();
        Mensagem mensagem = new Mensagem(usuario.getUid(), casa.getUserId(), conteudo, true, usuario.getNome(),casa.getNome());
        ArrayList<Chat> chatsDoDono = new ArrayList<>();

        chat.addMensagem(mensagem);

        DocumentReference donoRef = db.collection("usuarios").document(casa.getUserId());

        donoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Encontrou o documento do usuário
                    ArrayList<Map<String, Object>> chatsData = (ArrayList<Map<String, Object>>) document.get("chats");

                    Chat chat = new Chat();

                    if (chatsData != null) {
                        for (Map<String, Object> chatData : chatsData) {
                            // Crie objetos Casa com os dados e adicione ao array todasAsCasas
                            chat.setId((String) chatData.get("id"));
                            chat.setUsuarioDestinatarioId((String) chatData.get("usuarioDestinatarioId"));
                            chat.setUsuarioRemetenteId((String) chatData.get("usuarioRemetenteId"));
                            if (chatData.containsKey("mensagens")) {
                                ArrayList<Map<String, Object>> mensagemData = (ArrayList<Map<String, Object>>) chatData.get("mensagens");
                                ArrayList<Mensagem> mensagens = new ArrayList<>();
                                for (Map<String, Object> mensagem3 : mensagemData){
                                    Mensagem mensagem2 = new Mensagem();
                                    mensagem2.setDestinatario((String) mensagem3.get("destinatario"));
                                    mensagem2.setRemetente((String) mensagem3.get("remetente"));
                                    mensagem2.setConteudo((String) mensagem3.get("conteudo"));
                                    mensagem2.setDestinatarioNome((String) mensagem3.get("destinatarioNome"));
                                    mensagem2.setRemetenteNome((String) mensagem3.get("remetenteNome"));
                                    mensagens.add(mensagem2);

                                }

                                chat.setMensagens(mensagens);
                            }
                            chatsDoDono.add(chat);

                        }
                    }
                }
            }
        });

        chatsDoDono.add(chat);
        mensagemAdapter.notifyDataSetChanged();

        edtMensagem.setText("");

        DocumentReference userRef = db.collection("usuarios").document(usuario.getUid());
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("chats", usuario.getChats());

        userRef.update(updateData).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Falha ao atualizar o Firestore
                Toast.makeText(MensagemActitivy.this, "Falha ao enviar a mensagem", Toast.LENGTH_SHORT).show();
            }
        });

        Map<String, Object> updateData2 = new HashMap<>();
        updateData2.put("chats", chatsDoDono);


        donoRef.update(updateData2).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Falha ao atualizar o Firestore
                Toast.makeText(MensagemActitivy.this, "Falha ao enviar a mensagem", Toast.LENGTH_SHORT).show();
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
            intent.putExtra("casa", casa);
            intent.putExtra("usuario", usuario);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
