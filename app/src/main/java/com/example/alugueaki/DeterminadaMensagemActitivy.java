package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

import java.util.ArrayList;

public class DeterminadaMensagemActitivy extends AppCompatActivity {

    Casa casa = new Casa();
    Usuario usuario = new Usuario();
    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    Chat chat = new Chat();

    Usuario destinatario = new Usuario();

    private ActitivyDeterminadoChatBinding binding;
    private MensagemAdapter mensagemAdapter;

    private EditText edtMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivy_determinado_chat);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");
        chat = (Chat) getIntent().getSerializableExtra("chat");

        Log.d("debug", "" + chat);
        destinatario = chat.getUsuarioDestinatario();

        Log.d("debug","" + chat);

        ArrayList<Mensagem> mensagensAux = new ArrayList<>(chat.getMensagens());

        for(Mensagem mensagem : mensagensAux){
            if(mensagem.getDestinatario().getId() == usuario.getId()){
                mensagem.setIsRemetente();
            }
        }


        chat.setMensagens(mensagensAux);

        Log.d("debug",""+ mensagensAux);


        Log.d("DEBUG","" + listaDeUsuarios);


        binding = ActitivyDeterminadoChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerViewChat= binding.recyclerViewChat;
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setHasFixedSize(true);

        Log.d("debug",""+ usuario);
        Log.d("debug",""+ listaDeUsuarios.getDeterminadoUsuario(casa.getUserId()));


        mensagemAdapter = new MensagemAdapter(chat.getMensagens(), this);
        recyclerViewChat.setAdapter(mensagemAdapter);
        edtMensagem = findViewById(R.id.conteudoMensagem);


    }

    public void enviarMensagem(View view){
        String conteudo = edtMensagem.getText().toString();
        Mensagem mensagem = new Mensagem(usuario, destinatario, conteudo, true);

        chat.addMensagem(mensagem);
        listaDeUsuarios.getDeterminadoChat(chat.getUsuarioRemetente().getId(),chat.getUsuarioDestinatario().getId()).setMensagens(chat.getMensagens());

        edtMensagem.setText("");

        Log.d("DEBUG", "" + chat);
        Log.d("debug", "" + chat.getUltimaMensagem());
        mensagemAdapter.notifyDataSetChanged();
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
            for(Mensagem mensagem : chat.getMensagens()){
                if(mensagem.getDestinatario().getId() == usuario.getId()){
                    mensagem.setIsRemetente();
                }
            }
            intent.putExtra("listaDeUsuarios",listaDeUsuarios);
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




