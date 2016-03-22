package com.example.admin.uber_tcc_sqlite.model;

/**
 * Created by Admin on 17/03/2016.
 */
public class PessoaRequisicao {
    private String data;
    private String latitude;
    private String longitude;
    private String nivelAlcool;
    private String servico;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNivelAlcool() {
        return nivelAlcool;
    }

    public void setNivelAlcool(String nivelAlcool) {
        this.nivelAlcool = nivelAlcool;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }
}
