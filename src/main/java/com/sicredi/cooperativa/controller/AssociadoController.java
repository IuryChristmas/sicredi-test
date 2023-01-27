package com.sicredi.cooperativa.controller;

import com.sicredi.cooperativa.business.AssociadoBusiness;
import com.sicredi.cooperativa.exceptions.CpfInvalidoException;
import com.sicredi.cooperativa.model.Associado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/associado")
public class AssociadoController {

    @Autowired
    private AssociadoBusiness business;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Associado associado) throws CpfInvalidoException {
        return business.salvar(associado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        business.deleteById(id);
    }

}
