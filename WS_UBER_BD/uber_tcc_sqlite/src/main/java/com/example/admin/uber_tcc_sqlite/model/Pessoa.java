package com.example.admin.uber_tcc_sqlite.model;

import org.json.JSONObject;

/**
 * Created by Admin on 14/03/2016.
 */
public class Pessoa {
    private int idade;
    private int lat;
    private int longi;
    private int nivel_alcool;
    private String nome;
    private String sexo;

    public Pessoa(int idade, int lat, int longi, int nivel_alcool, String nome, String sexo) {
        this.idade = idade;
        this.lat = lat;
        this.longi = longi;
        this.nivel_alcool = nivel_alcool;
        this.nome = nome;
        this.sexo = sexo;
    }

    public Pessoa() {
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLongi() {
        return longi;
    }

    public void setLongi(int longi) {
        this.longi = longi;
    }

    public int getNivel_alcool() {
        return nivel_alcool;
    }

    public void setNivel_alcool(int nivel_alcool) {
        this.nivel_alcool = nivel_alcool;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

}
