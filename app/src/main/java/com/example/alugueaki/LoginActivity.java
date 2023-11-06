package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Usuario;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    Usuario usuario = new Usuario();
    EditText edtNome;
    EditText edtSenha;

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cadastro);

        edtNome = findViewById(R.id.edtUsername);
        edtSenha = findViewById(R.id.edtPassword);
    }

    public void realizarLogin(View view) {
        String nome = edtNome.getText().toString();
        String senha = edtSenha.getText().toString();

        Usuario usuarioExistente = listaDeUsuarios.getUsuarioPeloNome(nome);

        if(senha.equals("")){
            Toast.makeText(this, "Senha nao pode ser vazia", Toast.LENGTH_SHORT).show();
        }
        else if(nome.equals("")){
            Toast.makeText(this, "Nome nao pode ser vazio", Toast.LENGTH_SHORT).show();
        }
        else if (usuarioExistente == null) {
            Usuario novoUsuario = new Usuario(id, nome, senha);
            listaDeUsuarios.addUsuario(novoUsuario);
            id++;


            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("usuario", novoUsuario);
            intent.putExtra("listaDeUsuarios", listaDeUsuarios);
            intent.putExtra("id", id);
            Toast.makeText(this, "Usuario " + nome + " cadastrado com id " + id , Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 1);

        }

        else if (usuarioExistente.getSenha().equals(senha)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("usuario", usuarioExistente);
            intent.putExtra("listaDeUsuarios", listaDeUsuarios);
            intent.putExtra("id", id);
            Toast.makeText(this, "Bem vindo " + nome, Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 1);

        } else {
            Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {

            usuario = (Usuario) data.getSerializableExtra("usuario");
            Log.d("DEBUG", "usuario chegando no login: "+ usuario);


            ListaDeUsuarios novalista = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            Log.d("DEBUG", "lista de usuarios chegando no login: "+ listaDeUsuarios);
            listaDeUsuarios.setUsersList(novalista.getUsersList());
            listaDeUsuarios.setChats(novalista.getChats());
            Log.d("DEBUG", "Teste da lista de user: " + listaDeUsuarios.getChats());

            Log.d("DEBUG", "lista de usuarios sendo editado no login: "+ listaDeUsuarios);


            for(int i = 0 ;i < listaDeUsuarios.getUsersList().size(); i++){
                if(listaDeUsuarios.getDeterminadoUsuario(i).getNome().equals(usuario.getNome())) {
                    listaDeUsuarios.setUsuario(i,usuario);
                    Log.d("DEBUG","usuario sendo setado:  "+ usuario);
                }
            }

            id = (int) data.getSerializableExtra("id");

            Log.d("DEBUG",""+ usuario);
            Log.d("DEBUG", ""+ listaDeUsuarios);
            Log.d("DEBUG", "id no momento: " + id);

        }
    }
}
