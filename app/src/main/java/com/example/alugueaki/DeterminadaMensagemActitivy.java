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
    
    import com.example.alugueaki.Adapter.MensagemAdapter;
    import com.example.alugueaki.Models.Casa;
    import com.example.alugueaki.Models.Chat;
    import com.example.alugueaki.Models.ListaDeUsuarios;
    import com.example.alugueaki.Models.Mensagem;
    import com.example.alugueaki.Models.Usuario;
    import com.example.alugueaki.databinding.ActitivyDeterminadoChatBinding;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;
    
    public class DeterminadaMensagemActitivy extends AppCompatActivity {
    
        Casa casa = new Casa();
        Usuario usuario = new Usuario();

        Chat chat = new Chat();
    
        Usuario destinatario = new Usuario();
    
        private ActitivyDeterminadoChatBinding binding;
        private MensagemAdapter mensagemAdapter;
    
        private EditText edtMensagem;
    
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.actitivy_determinado_chat);
    
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            chat = (Chat) getIntent().getSerializableExtra("chat");
    
            Log.d("debug", "usuario id" + usuario.getUid());
    
    
            Log.d("debug", "" + chat);
    
            Log.d("debug","" + chat);
    
            ArrayList<Mensagem> mensagensAux = new ArrayList<>(chat.getMensagens());
    
            Log.d("debug","mensagens" + mensagensAux);
            for(Mensagem mensagem : mensagensAux){
                if(mensagem.getRemetente().equals(usuario.getUid())){
                    mensagem.setIsRemetente();
                    Log.d("debug", "mensagem no for: " + mensagem.GetIsRemetente());
                }
            }
            Log.d("debug","mensagens" + mensagensAux);
    
    
    
            chat.setMensagens(mensagensAux);
    
            Log.d("debug",""+ mensagensAux);
    
    

    
            binding = ActitivyDeterminadoChatBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
    
            RecyclerView recyclerViewChat= binding.recyclerViewChat;
            recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewChat.setHasFixedSize(true);
    
            Log.d("debug",""+ usuario);
    
    
            mensagemAdapter = new MensagemAdapter(chat.getMensagens(), this);
            recyclerViewChat.setAdapter(mensagemAdapter);
            edtMensagem = findViewById(R.id.conteudoMensagem);
    
    
        }
    
        public void enviarMensagem(View view) {
            String conteudo = edtMensagem.getText().toString();
            Mensagem mensagem;
            if(usuario.getUid().equals(chat.getUsuarioDestinatarioId())){
                mensagem = new Mensagem(usuario.getUid(), chat.getUsuarioRemetenteId(), conteudo, true, usuario.getNome(),chat.getRemetenteNome());
                chat.addMensagem(mensagem);
                ArrayList<Chat> chatsDoDono = new ArrayList<>();
                usuario.substituirChat(chat.getId(),chat);
                DocumentReference donoRef = db.collection("usuarios").document(chat.getUsuarioRemetenteId());

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
                                        for (Map<String, Object> mensagem3 : mensagemData) {
                                            Mensagem mensagem2 = new Mensagem();
                                            mensagem2.setDestinatario((String) mensagem3.get("destinatario"));
                                            mensagem2.setRemetente((String) mensagem3.get("remetente"));
                                            mensagem2.setConteudo((String) mensagem3.get("conteudo"));
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
                // Atualizar o array 'casasPAluguel' e o ID da nova casa no Firestore
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("chats", usuario.getChats());

                userRef.update(updateData).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DeterminadaMensagemActitivy.this, "Falha ao enviar a mensagem", Toast.LENGTH_SHORT).show();
                    }
                });

                Map<String, Object> updateData2 = new HashMap<>();
                updateData2.put("chats", chatsDoDono);

                donoRef.update(updateData2).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DeterminadaMensagemActitivy.this, "Falha ao enviar a mensagem", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d("debug", "chat atualmente no usuario" + usuario.getChats());
                Log.d("debug", "mensagens atualmente no chat" + chat);

            }
            else{
                mensagem = new Mensagem(usuario.getUid(), chat.getUsuarioDestinatarioId(), conteudo, true,usuario.getNome(),chat.getDestinatarioNome());
                chat.addMensagem(mensagem);
                ArrayList<Chat> chatsDoDono = new ArrayList<>();
                usuario.substituirChat(chat.getId(),chat);

                DocumentReference donoRef = db.collection("usuarios").document(chat.getUsuarioDestinatarioId());

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
                                        for (Map<String, Object> mensagem3 : mensagemData) {
                                            Mensagem mensagem2 = new Mensagem();
                                            mensagem2.setDestinatario((String) mensagem3.get("destinatario"));
                                            mensagem2.setRemetente((String) mensagem3.get("remetente"));
                                            mensagem2.setConteudo((String) mensagem3.get("conteudo"));
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
                // Atualizar o array 'casasPAluguel' e o ID da nova casa no Firestore
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("chats", usuario.getChats());

                userRef.update(updateData).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DeterminadaMensagemActitivy.this, "Falha ao enviar a mensagem", Toast.LENGTH_SHORT).show();
                    }
                });

                Map<String, Object> updateData2 = new HashMap<>();
                updateData2.put("chats", chatsDoDono);

                donoRef.update(updateData2).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha ao atualizar o Firestore
                        Toast.makeText(DeterminadaMensagemActitivy.this, "Falha ao enviar a mensagem", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d("debug", "chat atualmente no usuario" + usuario.getChats());
                Log.d("debug", "mensagens atualmente no chat" + chat);


            }




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
    
                intent.putExtra("chat", chat);
                intent.putExtra("usuario",usuario);
                setResult(RESULT_OK,intent);
                finish();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    
    }
    
    
    
    
