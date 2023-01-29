package com.sicredi.cooperativa.controller;

import com.sicredi.cooperativa.business.VotoBusiness;
import com.sicredi.cooperativa.model.Voto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votar")
public class VotoController {

    @Autowired
    private VotoBusiness business;

    @PostMapping
    public ResponseEntity<?> votar(@RequestBody Voto voto) {
        return business.votar(voto);
    }

}
