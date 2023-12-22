package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deputado {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("siglaPartido")
    @Expose
    private String siglaPartido;

    @SerializedName("siglaUf")
    @Expose
    private String siglaUf;

    public Deputado(int id, String nome, String email, String siglaPartido, String siglaUf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.siglaPartido = siglaPartido;
        this.siglaUf = siglaUf;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }
    public void setSiglaPartido(String siglaPartido) {
        this.siglaPartido = siglaPartido;
    }

    public String getSiglaUf() {
        return siglaUf;
    }
    public void setSiglaUf(String siglaUf) {
        this.siglaUf = siglaUf;
    }

    @Override
    public String toString() {
        return "Deputado{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", siglaPartido='" + siglaPartido + '\'' +
                ", siglaUf='" + siglaUf + '\'' +
                '}';
    }
}
