package com.yarem.dbConnect.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Category.class)
public abstract class Category_ {

	public static volatile SingularAttribute<Category, Long> id;
	public static volatile SingularAttribute<Category, String> categoryName;
	public static volatile SetAttribute<Category, Product> products;

	public static final String ID = "id";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String PRODUCTS = "products";

}

