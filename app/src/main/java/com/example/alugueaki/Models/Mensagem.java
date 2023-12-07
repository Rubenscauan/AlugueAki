package com.example.alugueaki.Models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensagem implements Serializable {
    private String remetenteId;
    private String destinatarioId;
    private String conteudo;

    private String remetenteNome;
    private String destinatarioNome;
    //private LocalDateTime dataHoraEnvio;
    private boolean isRemetente;


    public Mensagem(){}
    public Mensagem(String remetente, String destinatario, String conteudo, boolean isRemetente,String remetenteNome,String DestinatarioNome) {
        this.remetenteId = remetente;
        this.destinatarioId = destinatario;
        this.conteudo = conteudo;
        this.remetenteNome = remetenteNome;
        this.destinatarioNome = DestinatarioNome;
        //this.dataHoraEnvio = LocalDateTime.now();
        this.isRemetente = isRemetente;
    }

    public String getRemetente() {
        return remetenteId;
    }

    public void setRemetente(String remetente) {
        this.remetenteId = remetente;
    }

    public String getDestinatario() {
        return destinatarioId;
    }

    public void setDestinatario(String destinatario) {
        this.destinatarioId = destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    //public LocalDateTime getDataHoraEnvio() {
        //return dataHoraEnvio;
    //}

    //public void setDataHoraEnvio(LocalDateTime dataHoraEnvio) {
    //    this.dataHoraEnvio = dataHoraEnvio;
    //}

    @Override
    public String toString() {
        return conteudo + "isremetente = " + isRemetente;
    }

    public boolean GetIsRemetente() {
        return isRemetente;
    }
    public void RecuperarRemetente(boolean sim) {
        this.isRemetente = sim;
    }


    public void setIsRemetente() {
        this.isRemetente = !isRemetente;
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
