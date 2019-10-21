package io.crf.cattlelog.control.repository;

import io.crf.cattlelog.control.domain.Medical;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Medical entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalRepository extends JpaRepository<Medical, Long> {

}
