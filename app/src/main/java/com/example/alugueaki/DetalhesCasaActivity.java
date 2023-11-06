package com.example.alugueaki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.Chat;
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Pedido;
import com.example.alugueaki.Models.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetalhesCasaActivity extends AppCompatActivity {
    private GoogleMap mMap;

    private final int REQUEST_CODE_CHAT = 1;
    Casa casa = new Casa();
    Usuario usuario = new Usuario();
    ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_casa);
        casa = (Casa) getIntent().getSerializableExtra("casa");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");

        TextView textTitulo = findViewById(R.id.textTitulo);
        TextView textDescricao = findViewById(R.id.textDescricao);
        TextView textEndereco = findViewById(R.id.textEndereco);

        textTitulo.setText(casa.getNome());
        textDescricao.setText(casa.getDescricao());
        textEndereco.setText(casa.getEndereco());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng locCasa = new LatLng(casa.getLatitude(), casa.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(locCasa)
                        .title("LocCasa"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locCasa,17.0f));

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
            intent.putExtra("listaDeUsuarios",listaDeUsuarios);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void solicitarAluguel(View view){
        casa.setPedido(new Pedido(usuario));
        Usuario donoDaCasa = listaDeUsuarios.getDeterminadoUsuario(casa.getUserId());
        donoDaCasa.addPedidoDeCasa(casa);


        Usuario usuario = listaDeUsuarios.getDeterminadoUsuario(casa.getUserId());
        listaDeUsuarios.setUsuario(casa.getUserId(),usuario);


        Log.d("DEBUG", "" + listaDeUsuarios.getDeterminadoUsuario(casa.getUserId()));
        Log.d("DEBUG", "" + listaDeUsuarios.getDeterminadoUsuario(usuario.getId()));

        Intent intent = new Intent();
        intent.putExtra("listaDeUsuarios",listaDeUsuarios);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void iniciarChat(View view){
        //Chat chat = new Chat(usuario, listaDeUsuarios.getDeterminadoUsuario(casa.getUserId()));
        //listaDeUsuarios.addChat(chat);
        //Log.d("DEBUG", "" + chat);
        Log.d("DEBUG", "" + listaDeUsuarios);
        Intent intent = new Intent(this, MensagemActitivy.class);
        intent.putExtra("casa", casa);
        //intent.putExtra("chat",chat);
        intent.putExtra("usuario",usuario);
        intent.putExtra("listaDeUsuarios",listaDeUsuarios);
        startActivityForResult(intent,REQUEST_CODE_CHAT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHAT && resultCode == RESULT_OK){
            listaDeUsuarios = (ListaDeUsuarios) data.getSerializableExtra("listaDeUsuarios");
            usuario = (Usuario) data.getSerializableExtra("usuario");
            casa = (Casa) data.getSerializableExtra("casa");
        }
    }


}
