package com.sicredi.cooperativa.model;

import com.sicredi.cooperativa.model.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pauta")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAUTA_SEQ")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "data_hora_inicio_votacao")
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim_votacao")
    private LocalDateTime dataHoraFim;

    @Column(name = "votos_a_favor")
    private Long votosAFavor;

    @Column(name = "votos_contra")
    private Long votosContra;

    public Long getId() {
        return id;
    }

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

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return this.dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime tempoVotacao) {
        this.dataHoraFim = tempoVotacao;
    }

    public Long getVotosContra() {
        return votosContra;
    }

    public void setVotosContra(Long votosContra) {
        this.votosContra = votosContra;
    }

    public Long getVotosAFavor() {
        return votosAFavor;
    }

    public void setVotosAFavor(Long votosAFavor) {
        this.votosAFavor = votosAFavor;
    }
}
