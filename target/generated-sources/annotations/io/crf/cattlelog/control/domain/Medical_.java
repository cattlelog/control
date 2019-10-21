package io.crf.cattlelog.control.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Medical.class)
public abstract class Medical_ {

	public static volatile SingularAttribute<Medical, LocalDate> date;
	public static volatile SingularAttribute<Medical, String> name;
	public static volatile SingularAttribute<Medical, Long> id;

	public static final String DATE = "date";
	public static final String NAME = "name";
	public static final String ID = "id";

}

