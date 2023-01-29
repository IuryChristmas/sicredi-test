package com.sicredi.cooperativa.repository;

import com.sicredi.cooperativa.model.Pauta;
import com.sicredi.cooperativa.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

    List<Pauta> findByTituloAndStatus(String titulo, Status status);

    List<Pauta> findByTitulo(String titulo);
}
