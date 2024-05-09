package com.yarem.dbConnect.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Double> priceForOne;
	public static volatile SingularAttribute<Product, byte[]> byteImage;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, Integer> count;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile SingularAttribute<Product, Integer> automatSlot;
	public static volatile SingularAttribute<Product, Boolean> useInAutomat;
	public static volatile SingularAttribute<Product, Category> category;

	public static final String PRICE_FOR_ONE = "priceForOne";
	public static final String BYTE_IMAGE = "byteImage";
	public static final String NAME = "name";
	public static final String COUNT = "count";
	public static final String DESCRIPTION = "description";
	public static final String AUTOMAT_SLOT = "automatSlot";
	public static final String USE_IN_AUTOMAT = "useInAutomat";
	public static final String CATEGORY = "category";

}

