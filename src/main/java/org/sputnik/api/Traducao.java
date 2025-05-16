package org.sputnik.api;

import java.time.LocalDateTime;

public class Traducao {
    private String codigo;
    private String traducao;
    private LocalDateTime dataCriacao;

    public Traducao(String codigo, String traducao, LocalDateTime dataCriacao) {
        this.codigo = codigo;
        this.traducao = traducao;
        this.dataCriacao = dataCriacao;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTraducao() {
        return traducao;
    }
    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
