package io.crf.cattlelog.control.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Herd.class)
public abstract class Herd_ {

	public static volatile SingularAttribute<Herd, String> nutrition;
	public static volatile SingularAttribute<Herd, String> code;
	public static volatile SingularAttribute<Herd, String> name;
	public static volatile SingularAttribute<Herd, String> remark;
	public static volatile SingularAttribute<Herd, Long> id;

	public static final String NUTRITION = "nutrition";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String REMARK = "remark";
	public static final String ID = "id";

}

