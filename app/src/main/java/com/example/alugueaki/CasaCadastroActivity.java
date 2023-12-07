package com.example.alugueaki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alugueaki.Models.Casa;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CasaCadastroActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

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

    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casa_cadastro);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            // Inicializar o mapa e obter a localização atual
            initializeMapAndGetCurrentLocation();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), "AIzaSyDAxYBM3VlmDILF4gvhIIIY0UiLrGa0XRA");
        placesClient = Places.createClient(this);

        // Configurar o campo de pesquisa de lugares
        edtLocalizacao = findViewById(R.id.edtLocalizacao);
        edtLocalizacao.setFocusable(false);
        edtLocalizacao.setOnClickListener(v -> startPlacePicker());

        edtTelefone = findViewById(R.id.edtTelefone);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtAluguel = findViewById(R.id.edtAluguel);
        edtEndereco = findViewById(R.id.edtEndereco);
    }

    private void startPlacePicker() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.GEOCODE)
                .build(this);
        startActivityForResult(intent, AutocompleteActivity.RESULT_ERROR);
    }

    private void initializeMapAndGetCurrentLocation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Obtém a localização atual do dispositivo
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Verifica as permissões novamente
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Localização Atual"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        } else {
                            // Se a localização ainda não estiver disponível, continue tentando
                            requestLocationUpdates();
                        }
                    });
        }
        else {
            // Se as permissões não estiverem concedidas, solicite-as ao usuário
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // As permissões estão concedidas, agora você pode iniciar as atualizações de localização
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null && locationResult.getLastLocation() != null) {
                        LatLng currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                        selectedLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Localização Atual"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                        Log.d("debug", "localização selecionada" + selectedLocation);
                        Log.d("debug", "localização atual" + currentLocation);

                        // Cancela as atualizações de localização após obter uma localização válida
                        fusedLocationClient.removeLocationUpdates(this);
                    }
                }
            };

            // Inicia as atualizações de localização
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            // Se as permissões não estiverem concedidas, solicite-as ao usuário
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("debug", "onMapReady called");

        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        // Obtenha e configure a localização atual do dispositivo
        getCurrentLocation();

        if (selectedLocation != null) {
            updateMapMarker(selectedLocation);
            latitude = selectedLocation.latitude;
            longitude = selectedLocation.longitude;
            Log.d("debug", "onMapReady - Latitude: " + latitude);
            Log.d("debug", "onMapReady - Longitude: " + longitude);
        }

        Log.d("debug", "localização selecionada" + selectedLocation);

    }

    @Override
    public void onMapClick(LatLng point) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                selectedLocation = place.getLatLng();
                latitude = selectedLocation.latitude;
                longitude = selectedLocation.longitude;
                // Atualize o campo de texto com o nome do lugar selecionado
                edtLocalizacao.setText(place.getName());

                // Atualize o marcador no mapa
                updateMapMarker(selectedLocation);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                // Lide com erros aqui
            }
        }
    }

    private void updateMapMarker(LatLng location) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15)); // Zoom 15 (pode ajustar conforme necessário)
    }


    public void adicionarCasa(View view) {
        String telefone = edtTelefone.getText().toString();
        String aluguel = edtAluguel.getText().toString();
        String descricao = edtDescricao.getText().toString();
        String endereco = edtEndereco.getText().toString();
        String localizacao = edtLocalizacao.getText().toString();
        Log.d("debug", "localização selecionada" + selectedLocation);
        
        if(endereco.equals("") || aluguel.equals("") || descricao.equals("")){
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Certifique-se de preencher pelo menos os campos de descrição, endereço e aluguel", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        Log.d("DEBUG", "Aluguel: " + aluguel);
        Casa c = new Casa("","","", telefone, descricao,endereco, localizacao, latitude, longitude, aluguel, 0);
        Log.d("DEBUG", "Nova casa: " + latitude + " " + longitude);

        // Limpa os campos após adicionar uma casa
        edtTelefone.setText("");
        edtAluguel.setText("");
        edtDescricao.setText("");



        Intent intent = new Intent();
        intent.putExtra("novaCasa", c);
        setResult(RESULT_OK, intent);
        finish();


    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Log.d("DEBUG", "URI da imagem selecionada: " + selectedImageUri);

            // Use o Glide para carregar e exibir a imagem selecionada na ImageView
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(imagemCasa);

            // Armazene a URI da imagem selecionada na variável imagemDaCasa
            imagemCasa = selectedImageUri;
        }
    }




    private void exibirMensagemErro(String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensagem)
                .setPositiveButton("OK", null)
                .show();
    }*/

    /*public void limparLista(View view){
        edtNome.setText("");
        edtTelefone.setText("");
        edtEndereco.setText("");
        edtAluguel.setText("");
        edtDescricao.setText("");
        imagemCasa = 0;
        // Limpar a exibição da lista ou tomar outras medidas, se necessário
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voltar, menu);
        return true;
    }

    public void limparLista(View view){
        edtTelefone.setText("");
        edtDescricao.setText("");
        edtAluguel.setText("");
        edtEndereco.setText("");
        edtLocalizacao.setText("");
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
