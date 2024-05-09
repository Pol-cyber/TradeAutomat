package com.yarem.dbConnect.entity;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@SequenceGenerator(name = "soldProd_generator",sequenceName = "soldProd_sequence_generator",allocationSize = 1)
public class AllSold {
    @Id
    @GeneratedValue(generator = "soldProd_generator")
    protected int id;

    @Temporal(value = TemporalType.DATE)
    @Column(updatable = false)
    @CreationTimestamp
    protected Date byDate;

    @Positive
    @Column(nullable = false)
    protected double finalPrice;

    @OneToMany(mappedBy = "allSold")
    protected Set<ProductAllSold> products = new HashSet<>();

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public void addSoldProduct(ProductAllSold p){
        products.add(p);
    }

    public int getId() {
        return id;
    }
    public Date getByDate() {
        return byDate;
    }
    public double getFinalPrice() {
        return finalPrice;
    }
}
