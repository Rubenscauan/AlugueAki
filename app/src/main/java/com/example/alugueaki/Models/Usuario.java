package com.example.alugueaki.Models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private int id;
    private String nome;
    private String senha;

    private ArrayList<Casa> casasPAluguel = new ArrayList<>();

    private ArrayList<Casa> casasAlugadas = new ArrayList<>();

    private ArrayList<Casa> casasComPedido = new ArrayList<>();

    private ArrayList<Casa> casasQueAluguei = new ArrayList<>();



    public Usuario(int id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.casasAlugadas = new ArrayList<>();
        this.casasPAluguel = new ArrayList<>();
        this.casasComPedido = new ArrayList<>();
        this.casasQueAluguei = new ArrayList<>();
    }


    public ArrayList<Casa> getCasasQueAluguei() {
        return casasQueAluguei;
    }

    public void setCasasQueAluguei(ArrayList<Casa> casasQueAluguei) {
        this.casasQueAluguei = casasQueAluguei;
    }

    public ArrayList<Casa> getCasasComPedido() {
        return casasComPedido;
    }

    public void setCasasComPedido(ArrayList<Casa> casasComPedido) {
        this.casasComPedido = casasComPedido;
    }

    public Usuario(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Casa getDeterminadaCasaPAluguel(int i ){
        return casasPAluguel.get(i);
    }

    public void modificarCasaPAluguelPorId(int idDesejado, Casa casaModificada) {
        for (Casa casa : casasPAluguel) {
            if (casa.getId() == idDesejado) {
                casa.setNome(casaModificada.getNome());
                casa.setTelefone(casaModificada.getTelefone());
                casa.setDescricao(casaModificada.getDescricao());
                casa.setEndereco(casaModificada.getEndereco());
                casa.setLocalizacao(casaModificada.getLocalizacao());
                casa.setLatitude(casaModificada.getLatitude());
                casa.setLongitude(casaModificada.getLongitude());
                casa.setAluguel(casaModificada.getAluguel());
                casa.setImagem(casaModificada.getImagem());

                break;
            }
        }
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "id= " + id +
                ", nome='" + nome +
                ", casasPAluguel=" + casasPAluguel +
                ", casasAlugadas=" + casasAlugadas +
                ", casasComPedido" + casasComPedido +
                ", casasQueAluguei" + casasQueAluguei +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ArrayList<Casa> getCasasPAluguel() {
        return casasPAluguel;
    }

    public void setCasasPAluguel(ArrayList<Casa> casasPAluguel) {
        this.casasPAluguel = casasPAluguel;
    }

    public ArrayList<Casa> getCasasAlugadas() {
        return casasAlugadas;
    }

    public void setCasasAlugadas(ArrayList<Casa> casasAlugadas) {
        this.casasAlugadas = casasAlugadas;
    }

    public void setDeterminadaCasaPAluguel(int index, Casa casa) {
        if (index >= 0 && index < casasPAluguel.size()) {
            casasPAluguel.set(index, casa);
        }
    }



    public void addCasaPAlugar(Casa casa){
        this.casasPAluguel.add(casa);
    }
    public void addPedidoDeCasa(Casa casa) {this.casasComPedido.add(casa);}

    public void removeCasaComPedido(int id) {
        ArrayList<Casa> copiaCasasComPedido = new ArrayList<>(casasComPedido);
        for (Casa casa : copiaCasasComPedido) {
            if (id == casa.getId()) {
                casasComPedido.remove(casa);
            }
        }
    }

    public void removeCasaAlugada(int id) {
        ArrayList<Casa> copiaCasasAlugadas = new ArrayList<>(casasAlugadas);
        for (Casa casa : copiaCasasAlugadas) {
            if (id == casa.getId()) {
                casasAlugadas.remove(casa);
            }
        }
    }

    public void removeCasaQueAluguei(int id) {
        ArrayList<Casa> copiaCasasQueAluguei = new ArrayList<>(casasQueAluguei);
        for (Casa casa : copiaCasasQueAluguei) {
            if (id == casa.getId()) {
                casasQueAluguei.remove(casa);
            }
        }
    }




    public void removeCasaPAluguel(int id) {
        ArrayList<Casa> copiaCasasPAluguel = new ArrayList<>(casasPAluguel);
        for (Casa casa : copiaCasasPAluguel) {
            if (id == casa.getId()) {
                casasPAluguel.remove(casa);
            }
        }
    }

    public void addCasaQueAluguei (Casa casa) {this.casasQueAluguei.add(casa);}

    public void addCasaAlugada (Casa casa) {this.casasAlugadas.add(casa);}

}
