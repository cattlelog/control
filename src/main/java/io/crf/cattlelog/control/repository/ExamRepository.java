package io.crf.cattlelog.control.repository;

import io.crf.cattlelog.control.domain.Exam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Exam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

}
