package com.sicredi.cooperativa.model;

import com.sicredi.cooperativa.model.enums.SimNaoEnum;
import com.sicredi.cooperativa.model.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Entity
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column(name = "data_criacao")
    private Date dataCriacao;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "liberar_votacao")
    private SimNaoEnum liberarVotacao;

    @Column(name = "data_hora_inicio_votacao")
    private Date dataHoraInicio;

    @Column(name = "tempo_votacao")
    private Double tempoVotacao;

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SimNaoEnum getLiberarVotacao() {
        return liberarVotacao;
    }

    public void setLiberarVotacao(SimNaoEnum liberarVotacao) {
        this.liberarVotacao = liberarVotacao;
    }

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Double getTempoVotacao() {
        return this.tempoVotacao;
    }

    public void setTempoVotacao(Double tempoVotacao) {
        this.tempoVotacao = tempoVotacao;
    }
}
