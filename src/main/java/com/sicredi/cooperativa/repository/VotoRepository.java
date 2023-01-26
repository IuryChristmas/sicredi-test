package com.sicredi.cooperativa.repository;

import com.sicredi.cooperativa.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
}
