package com.example.admin.uber_tcc_sqlite.model;

/**
 * Created by Admin on 29/02/2016.
 */
public class Usuario {
    private int idcontato;
    private String nome;
    private String usuario;
    private String macadress;

    public long getIdcontato() {
        return idcontato;
    }

    public void setIdcontato(int idcontato) {
        this.idcontato = idcontato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMacadress() {
        return macadress;
    }

    public void setMacadress(String macadress) {
        this.macadress = macadress;
    }
}
