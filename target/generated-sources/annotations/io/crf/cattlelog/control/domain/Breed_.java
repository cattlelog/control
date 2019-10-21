package io.crf.cattlelog.control.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Breed.class)
public abstract class Breed_ {

	public static volatile SingularAttribute<Breed, Kind> kind;
	public static volatile SingularAttribute<Breed, String> name;
	public static volatile SingularAttribute<Breed, String> remark;
	public static volatile SingularAttribute<Breed, Long> id;

	public static final String KIND = "kind";
	public static final String NAME = "name";
	public static final String REMARK = "remark";
	public static final String ID = "id";

}

