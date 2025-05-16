package org.sputnik.api;

import java.time.LocalDateTime;

public class Explicacao {
    private String codigo;
    private String explicacao;
    private LocalDateTime dataCriacao;

    public Explicacao(String codigo, String explicacao, LocalDateTime dataCriacao) {
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
