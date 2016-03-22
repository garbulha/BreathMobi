package com.example.admin.uber_tcc_sqlite.model;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 14/03/2016.
 */
public class Pessoa {

    private double altura;
    private int idade;
    private String macaddress;
    private String nome;
    private int peso;
    private String senha;
    private String sexo;
    private String usuario;
    private PessoaRequisicao requisicao;
    private PessoatelefonesFavoritos telefonesFavoritos;

    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public double getAltura() {
        return altura;
    }

    public int getIdade() {
        return idade;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public String getNome() {
        return nome;
    }

    public int getPeso() {
        return peso;
    }

    public String getSenha() {
        return senha;
    }

    public String getSexo() {
        return sexo;
    }

    public String getUsuario() {
        return usuario;
    }

    public PessoaRequisicao getRequisicao() {
        return requisicao;
    }

    public PessoatelefonesFavoritos getTelefonesFavoritos() {
        return telefonesFavoritos;
    }
}
