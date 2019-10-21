package io.crf.cattlelog.control.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

import io.crf.cattlelog.control.domain.enumeration.Origin;

import io.crf.cattlelog.control.domain.enumeration.Gender;

import io.crf.cattlelog.control.domain.enumeration.Conception;

/**
 * A Animal.
 */
@Entity
@Table(name = "animal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "earring", nullable = false)
    private String earring;

    @Column(name = "code")
    private String code;

    @Column(name = "identification")
    private String identification;

    @Column(name = "remark")
    private String remark;

    @Column(name = "breeder")
    private String breeder;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin")
    private Origin origin;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "conception")
    private Conception conception;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_of_purchase")
    private LocalDate dateOfPurchase;

    @Column(name = "date_of_weaning")
    private LocalDate dateOfWeaning;

    @Column(name = "weight_at_birth")
    private Float weightAtBirth;

    @Column(name = "weight_at_weaning")
    private Float weightAtWeaning;

    @Column(name = "weight_205")
    private Float weight205;

    @Column(name = "weight_365")
    private Float weight365;

    @Column(name = "scrotum")
    private Float scrotum;

    @Column(name = "ranch")
    private Integer ranch;

    @Column(name = "mphp")
    private Float mphp;

    @OneToOne
    @JoinColumn(unique = true)
    private Animal mother;

    @OneToOne
    @JoinColumn(unique = true)
    private Animal father;

    @ManyToOne
    @JsonIgnoreProperties("animals")
    private Breed breed;

    @ManyToOne
    @JsonIgnoreProperties("animals")
    private Herd herd;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEarring() {
        return earring;
    }

    public Animal earring(String earring) {
        this.earring = earring;
        return this;
    }

    public void setEarring(String earring) {
        this.earring = earring;
    }

    public String getCode() {
        return code;
    }

    public Animal code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdentification() {
        return identification;
    }

    public Animal identification(String identification) {
        this.identification = identification;
        return this;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getRemark() {
        return remark;
    }

    public Animal remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBreeder() {
        return breeder;
    }

    public Animal breeder(String breeder) {
        this.breeder = breeder;
        return this;
    }

    public void setBreeder(String breeder) {
        this.breeder = breeder;
    }

    public Origin getOrigin() {
        return origin;
    }

    public Animal origin(Origin origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Gender getGender() {
        return gender;
    }

    public Animal gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Conception getConception() {
        return conception;
    }

    public Animal conception(Conception conception) {
        this.conception = conception;
        return this;
    }

    public void setConception(Conception conception) {
        this.conception = conception;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Animal dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public Animal dateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
        return this;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public LocalDate getDateOfWeaning() {
        return dateOfWeaning;
    }

    public Animal dateOfWeaning(LocalDate dateOfWeaning) {
        this.dateOfWeaning = dateOfWeaning;
        return this;
    }

    public void setDateOfWeaning(LocalDate dateOfWeaning) {
        this.dateOfWeaning = dateOfWeaning;
    }

    public Float getWeightAtBirth() {
        return weightAtBirth;
    }

    public Animal weightAtBirth(Float weightAtBirth) {
        this.weightAtBirth = weightAtBirth;
        return this;
    }

    public void setWeightAtBirth(Float weightAtBirth) {
        this.weightAtBirth = weightAtBirth;
    }

    public Float getWeightAtWeaning() {
        return weightAtWeaning;
    }

    public Animal weightAtWeaning(Float weightAtWeaning) {
        this.weightAtWeaning = weightAtWeaning;
        return this;
    }

    public void setWeightAtWeaning(Float weightAtWeaning) {
        this.weightAtWeaning = weightAtWeaning;
    }

    public Float getWeight205() {
        return weight205;
    }

    public Animal weight205(Float weight205) {
        this.weight205 = weight205;
        return this;
    }

    public void setWeight205(Float weight205) {
        this.weight205 = weight205;
    }

    public Float getWeight365() {
        return weight365;
    }

    public Animal weight365(Float weight365) {
        this.weight365 = weight365;
        return this;
    }

    public void setWeight365(Float weight365) {
        this.weight365 = weight365;
    }

    public Float getScrotum() {
        return scrotum;
    }

    public Animal scrotum(Float scrotum) {
        this.scrotum = scrotum;
        return this;
    }

    public void setScrotum(Float scrotum) {
        this.scrotum = scrotum;
    }

    public Integer getRanch() {
        return ranch;
    }

    public Animal ranch(Integer ranch) {
        this.ranch = ranch;
        return this;
    }

    public void setRanch(Integer ranch) {
        this.ranch = ranch;
    }

    public Float getMphp() {
        return mphp;
    }

    public Animal mphp(Float mphp) {
        this.mphp = mphp;
        return this;
    }

    public void setMphp(Float mphp) {
        this.mphp = mphp;
    }

    public Animal getMother() {
        return mother;
    }

    public Animal mother(Animal animal) {
        this.mother = animal;
        return this;
    }

    public void setMother(Animal animal) {
        this.mother = animal;
    }

    public Animal getFather() {
        return father;
    }

    public Animal father(Animal animal) {
        this.father = animal;
        return this;
    }

    public void setFather(Animal animal) {
        this.father = animal;
    }

    public Breed getBreed() {
        return breed;
    }

    public Animal breed(Breed breed) {
        this.breed = breed;
        return this;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Herd getHerd() {
        return herd;
    }

    public Animal herd(Herd herd) {
        this.herd = herd;
        return this;
    }

    public void setHerd(Herd herd) {
        this.herd = herd;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Animal)) {
            return false;
        }
        return id != null && id.equals(((Animal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Animal{" +
            "id=" + getId() +
            ", earring='" + getEarring() + "'" +
            ", code='" + getCode() + "'" +
            ", identification='" + getIdentification() + "'" +
            ", remark='" + getRemark() + "'" +
            ", breeder='" + getBreeder() + "'" +
            ", origin='" + getOrigin() + "'" +
            ", gender='" + getGender() + "'" +
            ", conception='" + getConception() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", dateOfPurchase='" + getDateOfPurchase() + "'" +
            ", dateOfWeaning='" + getDateOfWeaning() + "'" +
            ", weightAtBirth=" + getWeightAtBirth() +
            ", weightAtWeaning=" + getWeightAtWeaning() +
            ", weight205=" + getWeight205() +
            ", weight365=" + getWeight365() +
            ", scrotum=" + getScrotum() +
            ", ranch=" + getRanch() +
            ", mphp=" + getMphp() +
            "}";
    }
}
