package com.sicredi.cooperativa.business;

import com.sicredi.cooperativa.model.Pauta;
import com.sicredi.cooperativa.model.Voto;
import com.sicredi.cooperativa.model.dto.ErroDTO;
import com.sicredi.cooperativa.model.enums.Status;
import com.sicredi.cooperativa.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VotoBusiness {

    @Autowired
    private VotoRepository repository;

    @Autowired
    private PautaBusiness pautaBusiness;

    @Autowired
    private MessageSource messageSource;

    private static final String VOTO_LIBERADO = "";

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> votar(Voto voto) {

        Long countVotoSamePauta = repository.buscarPorCpfEPauta(voto.getCpfAssociado(), voto.getPauta().getId());

        if (countVotoSamePauta > 0) {
            return ResponseEntity.badRequest()
                    .body(ErroDTO.buildMensagemErroSomenteUsuario(messageSource
                            .getMessage("associado.ja.votou", null, LocaleContextHolder.getLocale())));
        }

        String mensagemLiberacaoPauta = verificarPauta(voto.getPauta().getId());
        if(!mensagemLiberacaoPauta.isEmpty()) {
            return ResponseEntity.badRequest().body(ErroDTO.buildMensagemErroSomenteUsuario(mensagemLiberacaoPauta));
        }

        return ResponseEntity.ok(repository.save(voto));
    }

    private String verificarPauta(Long idPauta) {
        Optional<Pauta> pautaOptional = pautaBusiness.findById(idPauta);

        if(!pautaOptional.isPresent()) {
            return messageSource.getMessage("pauta.nao.encontrada", null, LocaleContextHolder.getLocale());
        }

        Pauta pauta = pautaOptional.get();
        if(pauta.getStatus().equals(Status.INICIADA)) {

            LocalDateTime agora = LocalDateTime.now();
            if(agora.isAfter(pauta.getDataHoraInicio()) && agora.isBefore(pauta.getDataHoraFim())) {
                return VOTO_LIBERADO;
            }

            pautaBusiness.encerrarPauta(pauta);
            return messageSource.getMessage("tempo.votacao.ja.encerrado", null, LocaleContextHolder.getLocale());
        }

        return messageSource.getMessage("pauta.nao.liberada.votacao", null, LocaleContextHolder.getLocale());
    }

}
