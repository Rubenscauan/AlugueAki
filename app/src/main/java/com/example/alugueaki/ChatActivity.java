package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alugueaki.Adapter.ChatAdapter;
import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.Chat;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Mensagem;
import com.example.alugueaki.Models.Usuario;
import com.example.alugueaki.databinding.ActitivyChatBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.OnItemClickListener {


    private ActitivyChatBinding binding;
    private ChatAdapter chatAdapter;
    Chat chat = new Chat();

    Usuario usuario = new Usuario();

    private final int REQUEST_CODE_DETERMINADO_CHAT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Log.d("debug", "usuario id" + usuario.getUid());


        binding = ActitivyChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerViewChat = binding.recyclerViewListaDeChats;
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setHasFixedSize(true);

        chatAdapter = new ChatAdapter(usuario.getChats(), this, this);
        recyclerViewChat.setAdapter(chatAdapter);

        chatAdapter.notifyDataSetChanged();

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

        intent.putExtra("chat", chat);

        startActivityForResult(intent, REQUEST_CODE_DETERMINADO_CHAT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_DETERMINADO_CHAT && resultCode == RESULT_OK){
            usuario = (Usuario) data.getSerializableExtra("usuario");
            chatAdapter.setChats(usuario.getChats());
            chatAdapter.notifyDataSetChanged();
        }
    }
}
