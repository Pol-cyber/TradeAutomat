package com.yarem.dbConnect.entity;

import com.yarem.dbConnect.entity.ProductAllSold.Id;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductAllSold.class)
public abstract class ProductAllSold_ {

	public static volatile SingularAttribute<ProductAllSold, Product> product;
	public static volatile SingularAttribute<ProductAllSold, AllSold> allSold;
	public static volatile SingularAttribute<ProductAllSold, Integer> count;
	public static volatile SingularAttribute<ProductAllSold, Id> id;
	public static volatile SingularAttribute<ProductAllSold, Double> totalCost;

	public static final String PRODUCT = "product";
	public static final String ALL_SOLD = "allSold";
	public static final String COUNT = "count";
	public static final String ID = "id";
	public static final String TOTAL_COST = "totalCost";

}

