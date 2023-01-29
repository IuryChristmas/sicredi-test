package com.sicredi.cooperativa.model.filter;

import com.sicredi.cooperativa.model.enums.Status;

public class PautaFiltro {

    private String titulo;

    private Status status;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
