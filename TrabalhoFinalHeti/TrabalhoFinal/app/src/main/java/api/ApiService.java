package api;

import models.Deputado;
import models.Partido;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("partidos")
    Call<ResponseBody> getPartidos();

    @GET("partidos/{id}")
    Call<ResponseBody> getPartido(@Path("id") int idPartido);

    @GET("deputados")
    Call<ResponseBody> getDeputados();

    @GET("deputados/{id}")
    Call<ResponseBody> getDeputado(@Path("id") int idDeputado);

}
