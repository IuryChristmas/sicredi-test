package com.sicredi.cooperativa.repository;

import com.sicredi.cooperativa.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    @Query(value = "SELECT COUNT(*) FROM VOTO VT WHERE VT.CPF_ASSOCIADO = :cpf AND VT.ID_PAUTA = :idPauta",
            nativeQuery = true)
    Long buscarPorCpfEPauta(String cpf, Long idPauta);

    @Query(value = "select distinct count(voto_associado) from " +
            "voto where id_pauta = :idPauta and voto_associado = 0", nativeQuery = true)
    Long buscarContagemVotosAFavor(Long idPauta);

    @Query(value = "select distinct count(voto_associado) from " +
            "voto where id_pauta = :idPauta and voto_associado = 1", nativeQuery = true)
    Long buscarContagemVotosContra(Long idPauta);
}
