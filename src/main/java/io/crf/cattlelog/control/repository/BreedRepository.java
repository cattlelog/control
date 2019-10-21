package io.crf.cattlelog.control.repository;

import io.crf.cattlelog.control.domain.Breed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Breed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {

}
