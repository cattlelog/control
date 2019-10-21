package io.crf.cattlelog.control.repository;

import io.crf.cattlelog.control.domain.Kind;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Kind entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KindRepository extends JpaRepository<Kind, Long> {

}
