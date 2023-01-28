package com.sicredi.cooperativa.business;

import com.sicredi.cooperativa.exceptions.CpfInvalidoException;
import com.sicredi.cooperativa.exceptions.VotoJaRealizadoException;
import com.sicredi.cooperativa.model.Pauta;
import com.sicredi.cooperativa.model.Voto;
import com.sicredi.cooperativa.model.dto.ErroDTO;
import com.sicredi.cooperativa.model.dto.UserDTO;
import com.sicredi.cooperativa.model.enums.SimNaoEnum;
import com.sicredi.cooperativa.model.enums.Status;
import com.sicredi.cooperativa.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
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
    private static final String URL = "https://user-info.herokuapp.com/users/{cpf}";
    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> votar(Voto voto) {

        if (!this.isCpfValido(voto.getCpfAssociado())) {
            throw new CpfInvalidoException("CPF Invalido");
        }

        Long countVotoSamePauta = repository.buscarPorCpfEPauta(voto.getCpfAssociado(), voto.getPauta().getId());

        if(countVotoSamePauta > 0) {
            throw new VotoJaRealizadoException("Voce ja votou nesta pauta anteriormente");
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
            Instant inicioVotacao = pauta.getDataHoraInicio().toInstant();
            Instant tempoAtual = Instant.now();
            Duration tempoDecorrido = Duration.between(inicioVotacao, tempoAtual);
            if(tempoDecorrido.getSeconds() < 60) {
                return VOTO_LIBERADO;
            }

            pautaBusiness.encerrarPauta(pauta);
            return messageSource.getMessage("tempo.votacao.ja.encerrado", null, LocaleContextHolder.getLocale());
        }

        return messageSource.getMessage("pauta.nao.liberada.votacao", null, LocaleContextHolder.getLocale());
    }

    /*public Long quantidadeVotosAFavor(Long idPauta) {
        return repository.quantidadeVotosAFavor(idPauta);
    }

    public Long quantidadeVotosContra(Long idPauta) {
        return repository.quantidadeVotosContra(idPauta);
    }*/

    private boolean isCpfValido(String cpf) {
        boolean isValido = false;

        RestTemplate restTemplate = new RestTemplate();
        UserDTO userDTO = restTemplate.getForObject(URL.replace("{cpf}", cpf), UserDTO.class);

        if(Objects.nonNull(userDTO) && userDTO.getStatus().equals(ABLE_TO_VOTE)) {
            isValido = true;
        }

        return isValido;
    }
}
