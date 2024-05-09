package com.yarem.dbConnect.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@SequenceGenerator(name = "category_generator",sequenceName = "category_sequence_generator",allocationSize = 1)
public class Category {

    @Id
    @GeneratedValue(generator = "category_generator")
    protected Long id;

    @Column(nullable = false,unique = true)
    protected String categoryName;

    @OneToMany(mappedBy = "category")
    protected Set<Product> products = new HashSet<>();


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }
}
