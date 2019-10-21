package io.crf.cattlelog.control.domain;

import io.crf.cattlelog.control.domain.enumeration.Conception;
import io.crf.cattlelog.control.domain.enumeration.Gender;
import io.crf.cattlelog.control.domain.enumeration.Origin;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Animal.class)
public abstract class Animal_ {

	public static volatile SingularAttribute<Animal, String> code;
	public static volatile SingularAttribute<Animal, Gender> gender;
	public static volatile SingularAttribute<Animal, Origin> origin;
	public static volatile SingularAttribute<Animal, Animal> father;
	public static volatile SingularAttribute<Animal, String> breeder;
	public static volatile SingularAttribute<Animal, Float> scrotum;
	public static volatile SingularAttribute<Animal, String> remark;
	public static volatile SingularAttribute<Animal, LocalDate> dateOfBirth;
	public static volatile SingularAttribute<Animal, Float> weightAtWeaning;
	public static volatile SingularAttribute<Animal, Breed> breed;
	public static volatile SingularAttribute<Animal, Integer> ranch;
	public static volatile SingularAttribute<Animal, String> earring;
	public static volatile SingularAttribute<Animal, Animal> mother;
	public static volatile SingularAttribute<Animal, String> identification;
	public static volatile SingularAttribute<Animal, LocalDate> dateOfWeaning;
	public static volatile SingularAttribute<Animal, LocalDate> dateOfPurchase;
	public static volatile SingularAttribute<Animal, Conception> conception;
	public static volatile SingularAttribute<Animal, Float> mphp;
	public static volatile SingularAttribute<Animal, Float> weight205;
	public static volatile SingularAttribute<Animal, Long> id;
	public static volatile SingularAttribute<Animal, Float> weightAtBirth;
	public static volatile SingularAttribute<Animal, Float> weight365;
	public static volatile SingularAttribute<Animal, Herd> herd;

	public static final String CODE = "code";
	public static final String GENDER = "gender";
	public static final String ORIGIN = "origin";
	public static final String FATHER = "father";
	public static final String BREEDER = "breeder";
	public static final String SCROTUM = "scrotum";
	public static final String REMARK = "remark";
	public static final String DATE_OF_BIRTH = "dateOfBirth";
	public static final String WEIGHT_AT_WEANING = "weightAtWeaning";
	public static final String BREED = "breed";
	public static final String RANCH = "ranch";
	public static final String EARRING = "earring";
	public static final String MOTHER = "mother";
	public static final String IDENTIFICATION = "identification";
	public static final String DATE_OF_WEANING = "dateOfWeaning";
	public static final String DATE_OF_PURCHASE = "dateOfPurchase";
	public static final String CONCEPTION = "conception";
	public static final String MPHP = "mphp";
	public static final String WEIGHT205 = "weight205";
	public static final String ID = "id";
	public static final String WEIGHT_AT_BIRTH = "weightAtBirth";
	public static final String WEIGHT365 = "weight365";
	public static final String HERD = "herd";

}

