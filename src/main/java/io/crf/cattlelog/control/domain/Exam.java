package io.crf.cattlelog.control.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "result", nullable = false)
    private String result;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private Medical medical;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private Animal animal;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public Exam result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Medical getMedical() {
        return medical;
    }

    public Exam medical(Medical medical) {
        this.medical = medical;
        return this;
    }

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Exam animal(Animal animal) {
        this.animal = animal;
        return this;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return id != null && id.equals(((Exam) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", result='" + getResult() + "'" +
            "}";
    }
}
