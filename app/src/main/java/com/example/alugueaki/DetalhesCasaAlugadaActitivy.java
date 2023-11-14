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
import com.example.alugueaki.Models.ListaDeUsuarios;
import com.example.alugueaki.Models.Pedido;
import com.example.alugueaki.Models.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetalhesCasaAlugadaActitivy extends AppCompatActivity {
    private GoogleMap mMap;

    private Casa casa = new Casa();
    private Usuario usuario = new Usuario();
    private ListaDeUsuarios listaDeUsuarios = new ListaDeUsuarios();
    private Usuario solicitante = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_casa_alugada);
        casa = (Casa) getIntent().getSerializableExtra("casa");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        listaDeUsuarios = (ListaDeUsuarios) getIntent().getSerializableExtra("listaDeUsuarios");
        solicitante = listaDeUsuarios.getDeterminadoUsuario(casa.getPedido().getSolicitante().getId());

        Log.d("DEBUG", "" + casa);
        TextView textTitulo = findViewById(R.id.textTitulo);
        TextView textDescricao = findViewById(R.id.textDescricao);
        TextView textEndereco = findViewById(R.id.textEndereco);
        TextView textInquilino = findViewById(R.id.textInquilino);

        textTitulo.setText(usuario.getNome());
        textDescricao.setText(casa.getDescricao());
        textEndereco.setText(casa.getEndereco());
        textInquilino.setText(solicitante.getNome());


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
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void encerrarContrato(View view){
        Log.d("DEBUG", "" + listaDeUsuarios);

        usuario.removeCasaAlugada(casa.getId());
        usuario.addCasaPAlugar(casa);

        solicitante.removeCasaQueAluguei(casa.getId());


        listaDeUsuarios.setUsuario(usuario.getId(),usuario);
        listaDeUsuarios.setUsuario(solicitante.getId(),solicitante);

        Log.d("DEBUG", "" + listaDeUsuarios);

        Intent intent = new Intent();
        intent.putExtra("listaDeUsuarios", listaDeUsuarios);
        intent.putExtra("usuario",usuario);
        setResult(RESULT_OK,intent);
        finish();
        Log.d("DEBUG", "" + listaDeUsuarios);

    }






}
