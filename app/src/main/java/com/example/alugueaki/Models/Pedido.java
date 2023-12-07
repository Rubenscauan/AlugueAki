package com.example.alugueaki.Models;

import java.io.Serializable;

public class Pedido implements Serializable {
    private String usuarioId;

    private String usuarioNome;


    public Pedido(){}

    // Getters e setters para os campos acima


    @Override
    public String toString() {
        return "Pedido{" +
                "usuarioId='" + usuarioId + '\'' +
                ", usuarioNome='" + usuarioNome + '\'' +
                '}';
    }

    public Pedido(String usuarioid, String usuarioNome) {
        this.usuarioId = usuarioid;
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioid) {
        usuarioId = usuarioid;
    }
}
