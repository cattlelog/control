package io.crf.cattlelog.control.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Herd.
 */
@Entity
@Table(name = "herd")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Herd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nutrition")
    private String nutrition;

    @Column(name = "code")
    private String code;

    @Column(name = "remark")
    private String remark;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Herd name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNutrition() {
        return nutrition;
    }

    public Herd nutrition(String nutrition) {
        this.nutrition = nutrition;
        return this;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getCode() {
        return code;
    }

    public Herd code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public Herd remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Herd)) {
            return false;
        }
        return id != null && id.equals(((Herd) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Herd{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nutrition='" + getNutrition() + "'" +
            ", code='" + getCode() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
