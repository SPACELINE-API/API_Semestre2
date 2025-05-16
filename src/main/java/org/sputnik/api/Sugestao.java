package org.sputnik.api;

import java.time.LocalDateTime;

public class Sugestao {
    private String codigo;
    private String sugestao;
    private LocalDateTime dataCriacao;

    public Sugestao(String codigo, String sugestao, LocalDateTime dataCriacao) {
        this.codigo = codigo;
        this.sugestao = sugestao;
        this.dataCriacao = dataCriacao;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSugestao() {
        return sugestao;
    }
    public void setSugestao(String sugestao) {
        this.sugestao = sugestao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
