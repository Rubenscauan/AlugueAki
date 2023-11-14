package com.example.alugueaki.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ListaDeUsuarios implements Serializable {
    private ArrayList<Usuario> usersList = new ArrayList<>();
    private ArrayList<Chat> chats = new ArrayList<Chat>();

    public ListaDeUsuarios() {
    }

    public ArrayList<Usuario> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<Usuario> usersList) {
        this.usersList = usersList;
    }

    public void addUsuario(Usuario usuario){
        usersList.add(usuario);
    }

    public Usuario getDeterminadoUsuario(int i ){
        return usersList.get(i);
    }

    @Override
    public String toString() {
        return "ListaDeUsuarios{" +
                "usersList=" + usersList +
                "Chats" + chats +
                '}';
    }

    public void setUsuario(int index, Usuario novoUsuario) {
        if (index >= 0 && index < usersList.size()) {
            usersList.set(index, novoUsuario);
        }
    }

    public Usuario getUsuarioPeloNome(String nome) {
        for (Usuario usuario : usersList) {
            if (usuario != null && usuario.getNome() != null && usuario.getNome().equals(nome)) {
                return usuario;
            }
        }
        return null;
    }


    public void addChat(Chat chat){
        chats.add(chat);
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public Chat getDeterminadoChat(int remetenteId,int destinatarioId) {
        for (Chat chat : chats) {
            if (chat.getUsuarioRemetente().getId() == remetenteId && chat.getUsuarioDestinatario().getId() == destinatarioId)
                return chat;
        }
        return null;
    }

    public void setDeterminadoChat(int remetenteId, Chat novoChat) {
        for(Chat chat: chats){
            if(chat.getUsuarioRemetente().getId() == remetenteId){
                chats.set(remetenteId,novoChat);
                return;
            }
        }
    }
}
