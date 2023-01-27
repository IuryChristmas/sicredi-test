package com.sicredi.cooperativa.business;

import com.sicredi.cooperativa.exceptions.CpfInvalidoException;
import com.sicredi.cooperativa.model.Associado;
import com.sicredi.cooperativa.model.dto.UserDTO;
import com.sicredi.cooperativa.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AssociadoBusiness {

    @Autowired
    private AssociadoRepository repository;

    private static final String URL = "https://user-info.herokuapp.com/users/{cpf}";
    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";


    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> salvar(Associado associado) throws CpfInvalidoException {

        if(!this.isCpfValido(associado.getCpf())) {
            throw new CpfInvalidoException("CPF Invalido");
        }

        return ResponseEntity.ok(repository.save(associado));
    }

    private boolean isCpfValido(String cpf) {
        boolean isValido = false;

        RestTemplate restTemplate = new RestTemplate();
        UserDTO userDTO = restTemplate.getForObject(URL.replace("{cpf}", cpf), UserDTO.class);

        if(Objects.nonNull(userDTO) && userDTO.getStatus().equals(ABLE_TO_VOTE)) {
            isValido = true;
        }

        return isValido;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
