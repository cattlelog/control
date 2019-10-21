package io.crf.cattlelog.control.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Exam.class)
public abstract class Exam_ {

	public static volatile SingularAttribute<Exam, String> result;
	public static volatile SingularAttribute<Exam, Medical> medical;
	public static volatile SingularAttribute<Exam, Animal> animal;
	public static volatile SingularAttribute<Exam, Long> id;

	public static final String RESULT = "result";
	public static final String MEDICAL = "medical";
	public static final String ANIMAL = "animal";
	public static final String ID = "id";

}

