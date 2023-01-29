package com.sicredi.cooperativa.repository;

import com.sicredi.cooperativa.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    @Query(value = "SELECT COUNT(*) FROM VOTO VT WHERE VT.CPF_ASSOCIADO = :cpf AND VT.ID_PAUTA = :idPauta",
            nativeQuery = true)
    Long buscarPorCpfEPauta(String cpf, Long idPauta);
}
