package com.example.alugueaki.Models;

import java.io.Serializable;

public class Pedido implements Serializable {
    private Usuario solicitante; // O usuário que está fazendo o pedido

    public Pedido(Usuario solicitante) {
        this.solicitante = solicitante;

    }

    public Pedido(){}

    // Getters e setters para os campos acima

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                 solicitante +
                '}';
    }
}
