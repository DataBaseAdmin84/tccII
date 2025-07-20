package com.repository;

import com.model.ArquivoAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ArquivoAulaRepository extends JpaRepository<ArquivoAula, Long>, JpaSpecificationExecutor<ArquivoAula> {
}
