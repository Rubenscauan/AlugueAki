package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;


    Usuario usuario = new Usuario();
    EditText edtNome;
    EditText edtSenha;

    EditText edtEmail;

    private int id = 0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actitivy);
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtPassword);
    }

    public void realizarLogin(View view) {

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (senha.equals("")) {
            Toast.makeText(this, "Senha não pode ser vazia", Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(this, "E-mail não pode ser vazio", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            String uid = mAuth.getCurrentUser().getUid();

                            verificarUsuarioNoFirestore(uid);


                        } else {
                            // Se o login falhar, exiba uma mensagem para o usuário.
                            Toast.makeText(this, "Autenticação falhou.", Toast.LENGTH_SHORT).show();
                        }


                    });
        }
    }

    public void realizarCadastro(View view){
        Intent intent = new Intent(this,  RegisterActitivy.class);
        startActivity(intent);
    }

    private void verificarUsuarioNoFirestore(String userId) {
        FirebaseFirestore.getInstance().collection("usuarios")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // O usuário existe no Firestore
                            Usuario usuario = task.getResult().toObject(Usuario.class);

                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("usuario", usuario);
                            intent.putExtra("id", id);
                            startActivity(intent);

                            Toast.makeText(this, "Bem-vindo de volta, " + usuario.getNome(), Toast.LENGTH_SHORT).show();

                        } else {
                            // O usuário não existe no Firestore
                            Toast.makeText(this, "Usuário não encontrado. Registre-se primeiro.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Falha ao consultar o Firestore
                        Toast.makeText(this, "Falha ao verificar usuário. Tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
