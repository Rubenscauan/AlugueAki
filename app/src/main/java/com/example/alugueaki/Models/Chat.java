package com.example.alugueaki.Models;

import android.media.MediaMetadata;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Chat implements Serializable {

    String id;
    String usuarioRemetenteId;
    String usuarioDestinatarioId;

    String remetenteNome;

    String destinatarioNome;

    ArrayList<Mensagem> mensagens =  new ArrayList<>();


    public Chat(String id,String usuarioRemetenteId, String usuarioDestinatarioId,String remetenteNome,String destinatarioNome) {
        this.id = id;
        this.usuarioRemetenteId = usuarioRemetenteId;
        this.usuarioDestinatarioId = usuarioDestinatarioId;
        this.remetenteNome = remetenteNome;
        this.destinatarioNome = destinatarioNome;
        this.mensagens = mensagens;
    }

    public Chat() {
    }

    public String getUsuarioRemetente() {
        return usuarioRemetenteId;
    }

    public void setUsuarioRemetente(String usuarioRemetente) {
        this.usuarioRemetenteId = usuarioRemetente;
    }

    public String getUsuarioDestinatario() {
        return usuarioDestinatarioId;
    }

    public void setUsuarioDestinatario(String usuarioDestinatario) {
        this.usuarioDestinatarioId = usuarioDestinatario;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    public Mensagem getUltimaMensagem(){
        if (mensagens != null && !mensagens.isEmpty()) {
            return mensagens.get(mensagens.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", usuarioRemetenteId='" + usuarioRemetenteId + '\'' +
                ", usuarioDestinatarioId='" + usuarioDestinatarioId + '\'' +
                ", remetenteNome='" + remetenteNome + '\'' +
                ", destinatarioNome='" + destinatarioNome + '\'' +
                ", mensagens=" + mensagens +
                '}';
    }

    public void addMensagem(Mensagem mensagem){
        mensagens.add(mensagem);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioRemetenteId() {
        return usuarioRemetenteId;
    }

    public void setUsuarioRemetenteId(String usuarioRemetenteId) {
        this.usuarioRemetenteId = usuarioRemetenteId;
    }

    public String getUsuarioDestinatarioId() {
        return usuarioDestinatarioId;
    }

    public void setUsuarioDestinatarioId(String usuarioDestinatarioId) {
        this.usuarioDestinatarioId = usuarioDestinatarioId;
    }

    public String getRemetenteNome() {
        return remetenteNome;
    }

    public void setRemetenteNome(String remetenteNome) {
        this.remetenteNome = remetenteNome;
    }

    public String getDestinatarioNome() {
        return destinatarioNome;
    }

    public void setDestinatarioNome(String destinatarioNome) {
        this.destinatarioNome = destinatarioNome;
    }
}
