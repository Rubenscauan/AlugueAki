package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alugueaki.Adapter.ChatAdapter;
import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.Chat;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Usuario;
import com.example.alugueaki.databinding.ActitivyChatBinding;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.OnItemClickListener {


    private ActitivyChatBinding binding;
    private ChatAdapter chatAdapter;
    ArrayList<Chat> listadechats = new ArrayList<>();
    Chat chat = new Chat();

    Usuario usuario = new Usuario();
    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    private final int REQUEST_CODE_DETERMINADO_CHAT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivy_chat);

        binding = ActitivyChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerViewChat = binding.recyclerViewListaDeChats;
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setHasFixedSize(true);
        Log.d("debug","chats" + listaDeUsuarios.getChats());


        for(Chat chat : listaDeUsuarios.getChats()){
            Log.d("debug"," chats do for "+chat);
            Log.d("debug"," id do remetente"+chat.getUsuarioRemetente().getId());
            Log.d("debug"," id do destinatario "+ chat.getUsuarioDestinatario().getId());
            Log.d("debug"," id do usuario " + usuario.getId());

            if(chat.getUsuarioDestinatario().getId() == usuario.getId() || chat.getUsuarioRemetente().getId() == usuario.getId()){
                listadechats.add(chat);
                Log.d("debug"," chats do if "+chat);
            }
        }


        chatAdapter = new ChatAdapter(listadechats, this, this);
        recyclerViewChat.setAdapter(chatAdapter);



        chatAdapter.notifyDataSetChanged();

        //Usuario destinatario = listaDeUsuarios.getDeterminadoUsuario(casa.getUserId());

        /*
        Button sendButton = findViewById(R.id.enviar);
        EditText messageInput = findViewById(R.id.mensagem);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageInput.getText().toString();
                if (!messageText.isEmpty()) {
                    //usuario.enviarMensagem(destinatario, messageText);
                    messageAdapter.addMessage(new Mensagem(usuario, destinatario, messageText,"20:10"));
                    messageInput.setText(""); // Limpe o campo de entrada
                }
            }
        });*/
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
            intent.putExtra("listaDeUsuarios",listaDeUsuarios);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemClicked(Chat chat) {
        Intent intent = new Intent(this, DeterminadaMensagemActitivy.class);
        intent.putExtra("usuario", usuario);
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        intent.putExtra("chat", chat);
        Log.d("debug","" + listaDeUsuarios.getChats());
        Log.d("debug","" + listaDeUsuarios);

        startActivityForResult(intent, REQUEST_CODE_DETERMINADO_CHAT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_DETERMINADO_CHAT && resultCode == RESULT_OK){
            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            listadechats = listaDeUsuarios.getChats();
            Log.d("debug", "" + listaDeUsuarios.getChats());
            chatAdapter.notifyDataSetChanged();
        }
    }
    }
