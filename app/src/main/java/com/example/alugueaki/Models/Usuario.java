package com.example.alugueaki.Models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private String nome;

    private String uid;

    private String email;
    private String senha;

    private ArrayList<Casa> casasPAluguel = new ArrayList<>();

    private ArrayList<Casa> casasAlugadas = new ArrayList<>();

    private ArrayList<Casa> casasComPedido = new ArrayList<>();

    private ArrayList<Casa> casasQueAluguei = new ArrayList<>();

    private ArrayList<Chat> chats = new ArrayList<>();


    public Usuario(String uid, String nome,String email) {
        this.uid = uid;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.casasAlugadas = new ArrayList<>();
        this.casasPAluguel = new ArrayList<>();
        this.casasComPedido = new ArrayList<>();
        this.casasQueAluguei = new ArrayList<>();
        this.chats = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario(){}


    public Casa getDeterminadaCasaPAluguel(int i ){
        return casasPAluguel.get(i);
    }

    public void modificarCasaPAluguelPorId(String idDesejado, Casa casaModificada) {
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
                "nome='" + nome +
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

    public void removeCasaComPedido(String id) {
        ArrayList<Casa> copiaCasasComPedido = new ArrayList<>(casasComPedido);
        for (Casa casa : copiaCasasComPedido) {
            if (id.equals(casa.getId())) {
                casasComPedido.remove(casa);
            }
        }
    }

    public void removeCasaAlugada(String id) {
        ArrayList<Casa> copiaCasasAlugadas = new ArrayList<>(casasAlugadas);
        for (Casa casa : copiaCasasAlugadas) {
            if (id.equals(casa.getId())) {
                casasAlugadas.remove(casa);
            }
        }
    }

    public void removeCasaQueAluguei(String id) {
        ArrayList<Casa> copiaCasasQueAluguei = new ArrayList<>(casasQueAluguei);
        for (Casa casa : copiaCasasQueAluguei) {
            if (id.equals(casa.getId())) {
                casasQueAluguei.remove(casa);
            }
        }
    }




    public void removeCasaPAluguel(String id) {
        ArrayList<Casa> copiaCasasPAluguel = new ArrayList<>(casasPAluguel);
        for (Casa casa : copiaCasasPAluguel) {
            if (id.equals(casa.getId())) {
                casasPAluguel.remove(casa);
            }
        }
    }

    public void addCasaQueAluguei (Casa casa) {this.casasQueAluguei.add(casa);}

    public void addCasaAlugada (Casa casa) {this.casasAlugadas.add(casa);}

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public void addChat(Chat chat){
        chats.add(chat);
    }

    public void substituirChat(String idChatAntigo, Chat novoChat) {
        for (int i = 0; i < chats.size(); i++) {
            if (idChatAntigo.equals(chats.get(i).getId())) {
                chats.set(i, novoChat);
                break;
            }
        }
    }
}
