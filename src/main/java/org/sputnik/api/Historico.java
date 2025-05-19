package org.sputnik.api;

import java.sql.Timestamp;

public class Historico {
    private String codigo;
    private String explicacao;
    private Timestamp dataCriacao;

    public Historico(String codigo, String explicacao, Timestamp dataCriacao) {
        this.codigo = codigo;
        this.explicacao = explicacao;
        this.dataCriacao = dataCriacao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getExplicacao() {
        return explicacao;
    }

    public void setExplicacao(String explicacao) {
        this.explicacao = explicacao;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}