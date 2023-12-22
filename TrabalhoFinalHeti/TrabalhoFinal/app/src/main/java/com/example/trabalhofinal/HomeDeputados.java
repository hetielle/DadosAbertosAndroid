package com.example.trabalhofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapters.DeputadoAdapter;
import api.ApiService;
import api.RetrofitClient;
import models.Deputado;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeDeputados extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DeputadoAdapter deputadoAdapter;
    private BottomNavigationView bottomNavigationView;

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";

    private Retrofit retrofitDeputados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_deputados);

        recyclerView = findViewById(R.id.recyclerView);
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
        Call<ResponseBody> call = apiService.getDeputados();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        List<Deputado> deputados = parseJson(responseData);

                        setupRecyclerView(deputados);
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

    private List<Deputado> parseJson(String jsonString) {
        List<Deputado> deputados = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dadosArray = json.getJSONArray("dados");

            for (int i = 0; i < dadosArray.length(); i++) {
                JSONObject deputadoJson = dadosArray.getJSONObject(i);

                int id = deputadoJson.getInt("id");
                String siglaPartido = deputadoJson.getString("siglaPartido");
                String nome = deputadoJson.getString("nome");
                String uri = deputadoJson.getString("uri");
                String email = deputadoJson.getString("email");
                String siglaUf = deputadoJson.getString("siglaUf");

                Deputado deputado = new Deputado(id, nome, email, siglaPartido, siglaUf);
                deputados.add(deputado);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return deputados;
    }

    private void setupRecyclerView(List<Deputado> deputados) {
        deputadoAdapter = new DeputadoAdapter(deputados, new DeputadoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Deputado deputado) {
                Intent intent = new Intent(HomeDeputados.this, com.example.trabalhofinal.Deputado.class);
                intent.putExtra("ID_DEPUTADO", deputado.getId());
                Log.d("RecyclerView", "ID do Deputado clicado: " + deputado.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(deputadoAdapter);
    }

    private void abrirDetalhesDeputado(int deputadoId) {

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