package com.example.trabalhofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import api.ApiService;
import api.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Partido extends AppCompatActivity {

    private TextView txtPartido;
    private TextView txtSigla;
    private TextView txtSituacao;
    private TextView txtMembros;
    private TextView txtNomeLider;
    private TextView txtUfLider;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido);

        txtPartido = findViewById(R.id.idNomePartidoPerfil);
        txtSigla = findViewById(R.id.idSiglaPartidoPerfil);
        txtSituacao = findViewById(R.id.idSituacaoPartido);
        txtMembros = findViewById(R.id.idTotalMembrosPartido);
        txtNomeLider = findViewById(R.id.idNomeLider);
        txtUfLider = findViewById(R.id.idUfLider);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return selectedItemNavigationView(item);
            }
        });

        int idPartido = getIntent().getIntExtra("ID_PARTIDO", 0);
        getPartidoDetails(idPartido);
    }

    private void getPartidoDetails(int idPartido){
        ApiService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.getPartido(idPartido);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        setPartidoDetails(responseData);
                    } catch (IOException e){
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


    public void setPartidoDetails(String jsonStr){
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONObject dados = json.getJSONObject("dados");
            JSONObject status = dados.getJSONObject("status");
            JSONObject lider = status.getJSONObject("lider");

            String nome = dados.getString("nome");
            String sigla = dados.getString("sigla");
            String situacao = status.getString("situacao");
            String totalMembros = status.getString("totalMembros");
            String nomeLider = lider.getString("nome");
            String ufLider = lider.getString("uf");

            txtPartido.setText(nome);
            txtSigla.setText(sigla);
            txtSituacao.setText("Situação: " + situacao);
            txtMembros.setText("Número de membros: " + totalMembros);
            txtNomeLider.setText("Líder: " + nomeLider);
            txtUfLider.setText("Estado: " + ufLider);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void handleApiError(Response<?> response){
        Log.e("API", "Cod erro: " + response.code());
    }

    private void handleConnectionError(Throwable t){
        Log.e("API", "Erro de conexão: " + t);
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