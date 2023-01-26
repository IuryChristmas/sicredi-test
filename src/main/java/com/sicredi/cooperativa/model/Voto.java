package com.sicredi.cooperativa.model;

import com.sicredi.cooperativa.model.enums.SimNaoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voto_associado")
    @Enumerated(EnumType.ORDINAL)
    private SimNaoEnum votoEnum;

    @ManyToOne()
    @JoinColumn(name = "id_pauta", referencedColumnName = "id")
    private Pauta pauta;

    @ManyToOne
    @JoinColumn(name = "id_associado", referencedColumnName = "id")
    private Voto associado;

    public Voto() {
    }

    public Voto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimNaoEnum getVotoEnum() {
        return votoEnum;
    }

    public void setVotoEnum(SimNaoEnum votoEnum) {
        this.votoEnum = votoEnum;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    public Voto getAssociado() {
        return associado;
    }

    public void setAssociado(Voto associado) {
        this.associado = associado;
    }

}
