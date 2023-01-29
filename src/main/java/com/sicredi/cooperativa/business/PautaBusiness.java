package com.sicredi.cooperativa.business;

import com.sicredi.cooperativa.model.Pauta;
import com.sicredi.cooperativa.model.dto.ErroDTO;
import com.sicredi.cooperativa.model.enums.Status;
import com.sicredi.cooperativa.model.filter.PautaFiltro;
import com.sicredi.cooperativa.repository.PautaRepository;
import com.sicredi.cooperativa.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PautaBusiness {

    @Autowired
    private PautaRepository repository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private MessageSource messageSource;

    private static final String PAUTA_LIBERADA = "";

    @Transactional(rollbackFor = Exception.class)
    public Pauta salvar(Pauta pauta) {
        if (Objects.isNull(pauta.getStatus())) {
            pauta.setStatus(Status.NAO_INICIADA);
        }

        return repository.save(pauta);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> iniciarVotacaoParaPauta(Long id, LocalDateTime dataHoraFim) {
        Optional<Pauta> pautaOptional = repository.findById(id);

        String mensagemValidacao = validarPauta(pautaOptional);
        if(!mensagemValidacao.isEmpty()) {
            return ResponseEntity.badRequest().body(ErroDTO.buildMensagemErroSomenteUsuario(mensagemValidacao));
        }

        Pauta pauta = pautaOptional.get();
        pauta.setStatus(Status.INICIADA);
        pauta.setDataHoraInicio(LocalDateTime.now());
        pauta.setDataHoraFim(dataHoraFim);

        if (Objects.isNull(pauta.getDataHoraFim())) {
            pauta.setDataHoraFim(pauta.getDataHoraInicio().plusMinutes(1));
        }

        return ResponseEntity.ok(repository.save(pauta));
    }

    private String validarPauta(Optional<Pauta> pautaOptional) {
        if(pautaOptional.isEmpty()) {
            return messageSource.getMessage("pauta.nao.encontrada", null, LocaleContextHolder.getLocale());
        }

        Pauta pauta = pautaOptional.get();
        if(pauta.getStatus().equals(Status.INICIADA)) {
            return messageSource.getMessage("pauta.ja.iniciada", null, LocaleContextHolder.getLocale());
        } else if (pauta.getStatus().equals(Status.ENCERRADA)) {
            return messageSource.getMessage("pauta.encerrada", null, LocaleContextHolder.getLocale());
        }

        return PAUTA_LIBERADA;
    }

    public Optional<Pauta> findById(Long id) {
        return repository.findById(id);
    }

    public List<Pauta> buscarTodos(PautaFiltro filtro) {
        List<Pauta> pautas = filtrarPautas(filtro);
        for(Pauta pauta : pautas) {
            pauta = atualizarVotacoesEncerradas(pauta);
        }

        return pautas;
    }

    private List<Pauta> filtrarPautas(PautaFiltro filtro) {
        if (filtro.getTitulo() != null && filtro.getStatus() != null) {
            return repository.findByTituloAndStatus(filtro.getTitulo(), filtro.getStatus());
        } else if (filtro.getTitulo() != null && filtro.getStatus() == null) {
            return repository.findByTitulo(filtro.getTitulo());
        }

        return repository.findAll();
    }

    private Pauta atualizarVotacoesEncerradas(Pauta pauta) {
        if(!pauta.getStatus().equals(Status.ENCERRADA)) {
            LocalDateTime agora = LocalDateTime.now();
            if(agora.isAfter(pauta.getDataHoraInicio()) && agora.isBefore(pauta.getDataHoraFim())) {
                pauta = this.encerrarPauta(pauta);
            }
        }

        pauta.setVotosContra(votoRepository.buscarContagemVotosContra(pauta.getId()));
        pauta.setVotosAFavor(votoRepository.buscarContagemVotosAFavor(pauta.getId()));

        return pauta;
    }

    public Pauta encerrarPauta(Pauta pauta) {
        pauta.setStatus(Status.ENCERRADA);


        return repository.save(pauta);
    }

}
