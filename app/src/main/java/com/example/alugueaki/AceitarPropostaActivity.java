    package com.example.alugueaki;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.example.alugueaki.Models.Casa;
    import com.example.alugueaki.Models.ListaDeUsuarios;
    import com.example.alugueaki.Models.Pedido;
    import com.example.alugueaki.Models.Usuario;

    import java.util.ArrayList;

    public class AceitarPropostaActivity extends AppCompatActivity {

        Casa casa = new Casa();

        Usuario usuario = new Usuario();

        ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            casa = (Casa) getIntent().getSerializableExtra("casa");
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            int solicitanteId = getIntent().getIntExtra("solicitanteId", -1); // Obtém o ID do usuário solicitante
            listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.aceitar_proposta);

            TextView textUsername = findViewById(R.id.textUsername);
            TextView textDescricao = findViewById(R.id.textDescricao);
            TextView textEndereco = findViewById(R.id.textEndereco);

            textUsername.setText(listaDeUsuarios.getDeterminadoUsuario(solicitanteId).getNome());
            textDescricao.setText(casa.getDescricao());
            textEndereco.setText(casa.getEndereco());

            Log.d("Debug","casas do usuario com pedido: "+ usuario.getCasasComPedido());
            Log.d("Debug", "casas do pedinte " + listaDeUsuarios.getUsersList());
            Log.d("DEBUG", "id da casa" + casa.getId());
        }

        public void aceitarProposta(View view){
            Log.d("DEBUG",""+ listaDeUsuarios.getDeterminadoUsuario(casa.getPedido().getSolicitante().getId()));

            usuario.removeCasaComPedido(casa.getId());
            usuario.removeCasaPAluguel(casa.getId());
            usuario.addCasaAlugada(casa);

            Log.d("DEBUG",""+ listaDeUsuarios.getDeterminadoUsuario(casa.getPedido().getSolicitante().getId()));


            listaDeUsuarios.setUsuario(usuario.getId(),usuario);

            Usuario pedinte = listaDeUsuarios.getDeterminadoUsuario(casa.getPedido().getSolicitante().getId());

            pedinte.addCasaQueAluguei(casa);

            listaDeUsuarios.setUsuario(casa.getPedido().getSolicitante().getId(), pedinte);

            Log.d("DEBUG", "casas do dono" + usuario);
            Log.d("DEBUG", "casas do pedinte" + pedinte);

            Intent newintent = new Intent();
            Log.d("DEBUG", ""+listaDeUsuarios.getUsersList());
            newintent.putExtra("listaDeUsuarios", listaDeUsuarios);
            setResult(RESULT_OK, newintent);
            finish();
            Toast.makeText(this, "Proposta Aceita", Toast.LENGTH_SHORT).show();

        }

        public void rejeitarProposta(View view){
            usuario.removeCasaComPedido(casa.getId());

            listaDeUsuarios.setUsuario(usuario.getId(),usuario);

            Intent newintent = new Intent();
            Log.d("DEBUG", ""+listaDeUsuarios.getUsersList());
            newintent.putExtra("listaDeUsuarios", listaDeUsuarios);
            setResult(RESULT_CANCELED, newintent);
            finish();

            Toast.makeText(this, "Proposta Rejeitada", Toast.LENGTH_SHORT).show();

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

