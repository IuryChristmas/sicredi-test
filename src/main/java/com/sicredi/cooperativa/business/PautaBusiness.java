package com.sicredi.cooperativa.business;

import com.sicredi.cooperativa.model.Pauta;
import com.sicredi.cooperativa.model.dto.ErroDTO;
import com.sicredi.cooperativa.model.enums.Status;
import com.sicredi.cooperativa.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PautaBusiness {

    @Autowired
    private PautaRepository repository;

    @Autowired
    private VotoBusiness votoBusiness;

    @Autowired
    private MessageSource messageSource;

    private static final String PAUTA_LIBERADA = "";

    @Transactional(rollbackFor = Exception.class)
    public Pauta salvar(Pauta pauta) {
        pauta.setStatus(Status.NAO_INICIADA);
        return repository.save(pauta);
    }

    public ResponseEntity<?> iniciarVotacaoParaPauta(Long id) {
        Optional<Pauta> pautaOptional = repository.findById(id);

        String mensagemValidacao = verificarSeExisteInconscistencia(pautaOptional);
        if(!mensagemValidacao.isEmpty()) {
            return ResponseEntity.badRequest().body(ErroDTO.buildMensagemErroSomenteUsuario(mensagemValidacao));
        }

        Pauta pauta = pautaOptional.get();
        pauta.setStatus(Status.INICIADA);
        pauta.setDataHoraInicio(LocalDateTime.now());

        return ResponseEntity.ok(repository.save(pauta));
    }

    private String verificarSeExisteInconscistencia(Optional<Pauta> pautaOptional) {
        if(!pautaOptional.isPresent()) {
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

    private Pauta atualizarVotacoesEncerradas(Pauta pauta) {
        if(pauta.getStatus().equals(Status.ENCERRADA)) {

            if(pauta.getDataHoraInicio().until(pauta.getDataHoraFim(), ChronoUnit.MINUTES) >= pauta.getDataHoraFim().getMinute()) {
                pauta = this.encerrarPauta(pauta);
            }
        }
        return pauta;
    }

    public Pauta encerrarPauta(Pauta pauta) {
        pauta.setStatus(Status.ENCERRADA);

        return repository.save(pauta);
    }

}
