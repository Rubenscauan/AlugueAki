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


    Usuario usuarioTeste1 = new Usuario(0,"Rubens","123");
    Usuario usuarioTeste2 = new Usuario(1,"Cauan","123");
    Usuario usuarioTeste3 = new Usuario(2,"Marcos","123");
    Usuario usuarioTeste4 = new Usuario(3,"Leonardo","123");
    Usuario usuarioTeste5 = new Usuario(4,"Messi","123");
    Usuario usuarioTeste6 = new Usuario(5,"Neymar","123");

    Usuario usuario = new Usuario();
    EditText edtNome;
    EditText edtSenha;

    private int id = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cadastro);


        usuarioTeste2.addCasaPAlugar(new Casa(1, 1, "Cauan", "88994072425", "3 quartos, 2 banheiros, 15m²", "Avenida X, n55", "", 0, 0, "350", R.drawable.casa1));

        usuarioTeste3.addCasaPAlugar(new Casa(2, 2, "Marcos", "88994072425", "1 quarto, 1 sala, 10m²", "Rua Z, n30", "", 0, 0, "200", R.drawable.casa1));

        usuarioTeste4.addCasaPAlugar(new Casa(3, 3, "Leonardo", "88994072425", "4 quartos, 2 banheiros, 20m²", "Avenida Y, n75", "", 0, 0, "450", R.drawable.casa1));

        usuarioTeste5.addCasaPAlugar(new Casa(4, 4, "Messi", "88994072425", "3 quartos, 1 área de serviço, 18m²", "Rua W, n45", "", 0, 0, "300", R.drawable.casa1));

        usuarioTeste6.addCasaPAlugar(new Casa(5, 5, "Neymar", "88994072425", "2 quartos, 1 sala, 14m²", "Avenida A, n15", "", 0, 0, "270", R.drawable.casa1));

        listaDeUsuarios.addUsuario(usuarioTeste1);
        listaDeUsuarios.addUsuario(usuarioTeste2);
        listaDeUsuarios.addUsuario(usuarioTeste3);
        listaDeUsuarios.addUsuario(usuarioTeste4);
        listaDeUsuarios.addUsuario(usuarioTeste5);
        listaDeUsuarios.addUsuario(usuarioTeste6);



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


            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            Log.d("DEBUG", "lista de usuarios chegando no login: "+ listaDeUsuarios);
            //listaDeUsuarios.setUsersList(novalista.getUsersList());
            //listaDeUsuarios.setChats(novalista.getChats());
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
