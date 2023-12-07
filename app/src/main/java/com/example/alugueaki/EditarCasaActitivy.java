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

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.Usuario;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditarCasaActitivy extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {


        Casa casa = new Casa();

        Usuario usuario = new Usuario();

        EditText edtTelefone;
        EditText edtAluguel;
        EditText edtDescricao;

        double longitude;
        double latitude;
        EditText edtEndereco;
        private GoogleMap mMap;
        private PlacesClient placesClient;

        private Button selecionarLocalButton;
        private EditText edtLocalizacao;
        private LatLng selectedLocation;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_casa);

        casa = (Casa) getIntent().getSerializableExtra("casa");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Log.d("DEBUG", "casas do usuario: " + usuario.getCasasPAluguel());




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), "AIzaSyDAxYBM3VlmDILF4gvhIIIY0UiLrGa0XRA");
        placesClient = Places.createClient(this);

        edtLocalizacao = findViewById(R.id.edtLocalizacao);

        edtTelefone = findViewById(R.id.edtTelefone);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtAluguel = findViewById(R.id.edtAluguel);
        edtEndereco = findViewById(R.id.edtEndereco);

        edtTelefone.setText(casa.getTelefone());
        edtDescricao.setText(casa.getDescricao());
        edtAluguel.setText(casa.getAluguel());
        edtEndereco.setText(casa.getEndereco());
        edtLocalizacao.setText(casa.getLocalizacao());

        edtLocalizacao.setFocusable(false);
        edtLocalizacao.setOnClickListener(v -> startPlacePicker());

    }

        private void startPlacePicker () {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.GEOCODE)
                .build(this);
        startActivityForResult(intent, AutocompleteActivity.RESULT_ERROR);

    }
        @Override
        public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;
        mMap.setOnMapClickListener(this);


        if (selectedLocation != null) {
            updateMapMarker(selectedLocation);
        }

        LatLng sydney = new LatLng(casa.getLatitude(),casa.getLongitude());
        latitude = casa.getLatitude();
        longitude = casa.getLongitude();
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Localização inicial é em Sidney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17.0f));


    }

        @Override
        public void onMapClick (LatLng point){
        // Este método é chamado quando você clica no mapa
        selectedLocation = point;

        // Atualize o campo de texto com as coordenadas (latitude e longitude) do lugar selecionado
        edtLocalizacao.setText(String.format(Locale.getDefault(), "%.6f, %.6f", point.latitude, point.longitude));

        latitude = point.latitude;
        longitude = point.longitude;

        // Atualize o marcador no mapa
        updateMapMarker(selectedLocation);
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                selectedLocation = place.getLatLng();

                latitude = selectedLocation.latitude;
                longitude = selectedLocation.longitude;


                edtLocalizacao.setText(place.getName());

                // Atualize o marcador no mapa
                updateMapMarker(selectedLocation);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            }
        }
    }

        private void updateMapMarker (LatLng location){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 25)); // Zoom 15 (pode ajustar conforme necessário)
    }


        public void editarCasa (View view){
        String telefone = edtTelefone.getText().toString();
        String aluguel = edtAluguel.getText().toString();
        String descricao = edtDescricao.getText().toString();
        String endereco = edtEndereco.getText().toString();
        String localizacao = edtLocalizacao.getText().toString();

        Log.d("DEBUG", "Aluguel: " + aluguel);


        Casa c = new Casa(casa.getUserId(), casa.getId(), casa.getNome(), telefone, descricao, endereco, localizacao, latitude, longitude, aluguel, R.drawable.casa1);


        Log.d("DEBUG", "Nova casa: " + latitude + " " + longitude);

        // Limpa os campos após adicionar uma casa
        edtTelefone.setText("");
        edtAluguel.setText("");
        edtDescricao.setText("");

        casa = c;
        DocumentReference usuarioRef = db.collection("usuarios").document(c.getUserId());
        usuarioRef.get().addOnSuccessListener(documentSnapshot -> {
            Usuario usuario = documentSnapshot.toObject(Usuario.class);
            if (usuario != null) {
                ArrayList<Casa> casasPAluguel = usuario.getCasasPAluguel();
                    // Encontre a casa a ser editada no ArrayList
                for (int i = 0; i < casasPAluguel.size(); i++) {
                    Casa casaAntiga = casasPAluguel.get(i);
                    if (casaAntiga.getId().equals(c.getId())) {
                        // Substitua a casa antiga pela nova
                        casasPAluguel.set(i, c);

                            // Atualize os dados no Firestore
                        usuarioRef.update("casasPAluguel", casasPAluguel)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("DEBUG", "Casa editada com sucesso no Firestore");
                                    Intent intent = new Intent();
                                    Log.d("DEBUG",""+ usuario.getCasasPAluguel());


                                    Log.d("DEBUG",""+ usuario.getCasasPAluguel());

                                    intent.putExtra("usuario", usuario);
                                    intent.putExtra("casasPAluguel", casasPAluguel);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("ERROR", "Falha ao editar casa no Firestore", e);
                                });
                            break; // Interrompe o loop, pois a casa foi encontrada e atualizada
                        }
                    }
                }
            });
        }







    public void limparLista(View view){
        edtTelefone.setText("");
        edtDescricao.setText("");
        edtAluguel.setText("");
        edtEndereco.setText("");
        edtLocalizacao.setText("");
    }

    public void deleteCasa(View view){
        Log.d("DEBUG","" +usuario.getCasasPAluguel());
        usuario.removeCasaPAluguel(casa.getId());
        Intent intent = new Intent();
        intent.putExtra("usuario",usuario);
        Log.d("DEBUG",""+usuario.getCasasPAluguel());
        setResult(RESULT_OK, intent);
        finish();

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