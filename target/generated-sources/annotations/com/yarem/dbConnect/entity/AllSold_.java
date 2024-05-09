package com.yarem.dbConnect.entity;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AllSold.class)
public abstract class AllSold_ {

	public static volatile SingularAttribute<AllSold, Date> byDate;
	public static volatile SingularAttribute<AllSold, Double> finalPrice;
	public static volatile SingularAttribute<AllSold, Integer> id;
	public static volatile SetAttribute<AllSold, ProductAllSold> products;

	public static final String BY_DATE = "byDate";
	public static final String FINAL_PRICE = "finalPrice";
	public static final String ID = "id";
	public static final String PRODUCTS = "products";

}

