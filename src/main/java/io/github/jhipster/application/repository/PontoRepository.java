package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Ponto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ponto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {

}
