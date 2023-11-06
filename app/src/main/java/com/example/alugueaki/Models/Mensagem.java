package com.example.alugueaki.Models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensagem implements Serializable {
    private Usuario remetente;
    private Usuario destinatario;
    private String conteudo;
    private LocalDateTime dataHoraEnvio;
    private boolean isRemetente;


    public Mensagem(){}
    public Mensagem(Usuario remetente, Usuario destinatario, String conteudo, boolean isRemetente) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.conteudo = conteudo;
        this.dataHoraEnvio = LocalDateTime.now();
        this.isRemetente = isRemetente;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(LocalDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    @Override
    public String toString() {
        return conteudo + "isremetente = " + isRemetente;
    }

    public boolean isRemetente() {
        return isRemetente;
    }

    public void setIsRemetente() {
        this.isRemetente = !isRemetente;
    }
}
