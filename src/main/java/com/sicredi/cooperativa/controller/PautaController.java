package com.sicredi.cooperativa.controller;

import com.sicredi.cooperativa.business.PautaBusiness;
import com.sicredi.cooperativa.model.Pauta;
import com.sicredi.cooperativa.model.filter.PautaFiltro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pauta")
public class PautaController {

    @Autowired
    private PautaBusiness business;

    @PostMapping
    public ResponseEntity<Pauta> criar(@RequestBody Pauta pauta) {
        return ResponseEntity.ok(business.salvar(pauta));
    }

    @PutMapping("/iniciar-votacao-pauta/{id}")
    public ResponseEntity<?> iniciarVotacaoPauta(@PathVariable Long id, LocalDateTime dataHoraFim) {
        return business.iniciarVotacaoParaPauta(id, dataHoraFim);
    }

    @GetMapping
    public List<Pauta> buscarPorFiltro(PautaFiltro filtro) {
        return business.buscarTodos(filtro);
    }

}
