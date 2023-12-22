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

public class Deputado extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtSigla;
    private TextView txtEmail;
    private TextView txtUf;

    private TextView txtCpf;

    private TextView txtEscolaridade;
    private ImageView imgDeputado;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputado);

        txtNome = findViewById(R.id.idNomeDeputado);
        txtSigla = findViewById(R.id.idSiglaDeputado);
        txtEmail = findViewById(R.id.idEmailDeputadoPerfil);
        txtUf = findViewById(R.id.idUfPerfil);
        txtCpf = findViewById(R.id.idCpfDeputado);
        txtEscolaridade = findViewById(R.id.idEscolaridadeDeputado);
        imgDeputado = findViewById(R.id.idFotoDeputado);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return selectedItemNavigationView(item);
            }
        });

        int idDeputado = getIntent().getIntExtra("ID_DEPUTADO", 0);
        getDeputadoDetails(idDeputado);
    }

    private void getDeputadoDetails(int idDeputado){
        ApiService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.getDeputado(idDeputado);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        setDeputadoDetails(responseData);
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


    public void setDeputadoDetails(String jsonStr){
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONObject dados = json.getJSONObject("dados");
            JSONObject status = dados.getJSONObject("ultimoStatus");

            String nomeCivil = dados.getString("nomeCivil");
            String siglaPartido = status.getString("siglaPartido");
            String siglaUf = status.getString("siglaUf");
            String email = status.getString("email");
            String cpf = dados.getString("cpf");
            String foto = status.getString("urlFoto");
            String escolaridade = dados.getString("escolaridade");

            txtNome.setText(nomeCivil);
            txtSigla.setText(siglaPartido);
            txtEmail.setText("Email: " + email);
            txtUf.setText("Estado: " + siglaUf);
            txtCpf.setText("CPF: " + cpf);
            txtEscolaridade.setText("Escolaridade: " + escolaridade);

            Picasso.get().load(foto).into(imgDeputado);

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