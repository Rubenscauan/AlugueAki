package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alugueaki.Models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActitivy extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText edtEmail;

    EditText edtUsername;

    EditText edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_actitivy);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

    }

    public void realizarRegistro(View view) {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String nomeUsuario = edtUsername.getText().toString();

        if (senha.equals("") || email.equals("") || nomeUsuario.equals("")) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        } else {
            // Crie uma nova conta com e-mail e senha
            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Registro bem-sucedido, você pode obter o UID do usuário recém-criado
                            String uid = mAuth.getCurrentUser().getUid();

                            // Agora, você pode armazenar as informações adicionais no Firestore
                            // Por exemplo, crie uma coleção 'usuarios' e adicione um documento com as informações do usuário
                            Map<String, Object> userData = new HashMap<>();
                            Usuario usuario = new Usuario(uid,nomeUsuario,email);
                            userData.put(nomeUsuario, usuario);

                            FirebaseFirestore.getInstance().collection("usuarios")
                                    .document(uid)
                                    .set(usuario)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Usuario " + nomeUsuario + " cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                                        // Sucesso ao salvar informações adicionais
                                    })
                                    .addOnFailureListener(e -> {
                                        // Falha ao salvar informações adicionais
                                        Toast.makeText(this, "Falha no registro. Tente novamente.", Toast.LENGTH_SHORT).show();
                                    });


                            finish();


                        } else {
                            // Se o registro falhar, exiba uma mensagem para o usuário.
                            Toast.makeText(this, "Falha no registro. Verifique os detalhes e tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
