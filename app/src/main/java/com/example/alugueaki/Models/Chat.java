package com.example.alugueaki.Models;

import android.media.MediaMetadata;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Chat implements Serializable {
    Usuario usuarioRemetente;
    Usuario usuarioDestinatario;
    ArrayList<Mensagem> mensagens =  new ArrayList<>();


    public Chat(Usuario usuarioRemetente, Usuario usuarioDestinatario) {
        this.usuarioRemetente = usuarioRemetente;
        this.usuarioDestinatario = usuarioDestinatario;
    }

    public Chat() {
    }

    public Usuario getUsuarioRemetente() {
        return usuarioRemetente;
    }

    public void setUsuarioRemetente(Usuario usuarioRemetente) {
        this.usuarioRemetente = usuarioRemetente;
    }

    public Usuario getUsuarioDestinatario() {
        return usuarioDestinatario;
    }

    public void setUsuarioDestinatario(Usuario usuarioDestinatario) {
        this.usuarioDestinatario = usuarioDestinatario;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    public Mensagem getUltimaMensagem(){
        return mensagens.get(mensagens.size()-1);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "usuarioRemetente=" + usuarioRemetente +
                ", usuarioDestinatario=" + usuarioDestinatario +
                ", mensagens=" + mensagens +
                '}';
    }
    public void addMensagem(Mensagem mensagem){
        mensagens.add(mensagem);
    }






}
