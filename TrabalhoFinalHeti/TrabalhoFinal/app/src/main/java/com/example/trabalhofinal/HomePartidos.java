package com.example.trabalhofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapters.PartidoAdapter;
import api.ApiService;
import api.RetrofitClient;
import models.Partido;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePartidos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PartidoAdapter partidoAdapter;
    private BottomNavigationView bottomNavigationView;

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";

    private Retrofit retrofitPartidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_partidos);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fetchData();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return selectedItemNavigationView(item);
            }
        });
    }

    private void fetchData() {
        ApiService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.getPartidos();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        List<Partido> partidos = parseJson(responseData);

                        setupRecyclerView(partidos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                handleConnectionError(t);
            }
        });
    }

    private List<Partido> parseJson(String jsonString) {
        List<Partido> partidos = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dadosArray = json.getJSONArray("dados");

            for (int i = 0; i < dadosArray.length(); i++) {
                JSONObject partidoJson = dadosArray.getJSONObject(i);

                int id = partidoJson.getInt("id");
                String sigla = partidoJson.getString("sigla");
                String nome = partidoJson.getString("nome");
                String uri = partidoJson.getString("uri");

                Partido partido = new Partido(id, nome, sigla);
                partidos.add(partido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return partidos;
    }

    private void setupRecyclerView(List<Partido> partidos) {
        partidoAdapter = new PartidoAdapter(partidos, new PartidoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Partido partido) {
                Intent intent = new Intent(HomePartidos.this, com.example.trabalhofinal.Partido.class);
                intent.putExtra("ID_PARTIDO", partido.getId());
                Log.d("RecyclerView", "ID do Partido clicado: " + partido.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(partidoAdapter);
    }

    private void handleApiError(Response<?> response) {
        try {
            Log.e("API", "Código de erro: " + response.code());
            String errorBody = "Corpo da resposta de erro indisponível";

            if (response.errorBody() != null) {
                errorBody = response.errorBody().string();
                Log.e("API", "Corpo da resposta de erro: " + errorBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConnectionError(Throwable t) {
        Log.e("API", "Erro de conexão", t);
    }

    private boolean selectedItemNavigationView(MenuItem item){
        int idItem = item.getItemId();

        if (idItem == R.id.sair){
            startActivity(new Intent(this, Login.class));
        } else if (idItem == R.id.config) {
            startActivity(new Intent(this, Config.class));
        } else if (idItem == R.id.deputados) {
            startActivity(new Intent(this, HomeDeputados.class));
        } else if (idItem == R.id.partidos) {
            startActivity(new Intent(this, HomePartidos.class));
        }

        return false;
    }

}