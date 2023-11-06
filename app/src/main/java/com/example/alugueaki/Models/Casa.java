package com.example.alugueaki.Models;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Casa implements Serializable {
    private int id;
    private int userId; // Adicione um campo para armazenar o ID do usuário


    private String nome;
    private String telefone;
    private String descricao;
    private String aluguel;

    double latitude;
    double longitude;

    String endereco;

    String localizacao;
    private int imagem;

    private Pedido pedido; //



    public Casa(){
    }

    public Casa(int userId, int id,String nome, String telefone, String descricao,String endereco, String localizacao, double latitude, double longitude, String aluguel, int imagem) {
        this.userId = userId;
        this.nome = nome;
        this.telefone = telefone;
        this.descricao = descricao;
        this.endereco = endereco;
        this.localizacao = localizacao;
        this.aluguel = aluguel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagem = imagem;
        this.id = id;

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }



    @Override
    public String toString() {
        return "Casa{" +
                "userId" + userId +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", descricao='" + descricao + '\'' +
                ", aluguel='" + aluguel + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", pedido" + pedido +
                '}';
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAluguel() {
        return aluguel;
    }
    public void setAluguel(String aluguel) {
        this.aluguel = aluguel;
    }
    public int getImagem() {
        return imagem;
    }
    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }



}