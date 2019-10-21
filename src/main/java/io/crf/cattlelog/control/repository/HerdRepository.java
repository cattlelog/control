package io.crf.cattlelog.control.repository;

import io.crf.cattlelog.control.domain.Herd;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Herd entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HerdRepository extends JpaRepository<Herd, Long> {

}
