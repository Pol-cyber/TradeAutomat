package com.yarem.dbConnect.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Immutable
@Entity
public class ProductAllSold {

    @Embeddable
    public static class  Id implements Serializable {

        @Column(name = "allSold_id")
        protected int allSold_id;

        @Column(name = "product_id")
        protected String prod_Id;

        public Id() {

        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return prod_Id == id.prod_Id && allSold_id == id.allSold_id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(prod_Id, allSold_id);
        }

    }

    @EmbeddedId
    private Id id = new Id();

    @Column(nullable = false,updatable = false)
    protected int count;

    @Column(nullable = false,updatable = false)
    protected double totalCost;


    @ManyToOne
    @JoinColumn(name = "allSold_id",updatable = false,insertable = false)
    protected AllSold allSold;

    @ManyToOne
    @JoinColumn(name = "product_id",updatable = false,insertable = false)
    protected Product product;

    public ProductAllSold(int count, AllSold allSold, Product product,double totalCost) {
        this.count = count;
        this.allSold = allSold;
        this.product = product;
        this.totalCost = totalCost;

        this.id.allSold_id = allSold.id;
        this.id.prod_Id = product.name;
        allSold.addSoldProduct(this);
    }

    public ProductAllSold() {

    }

    public String getProductId() {
        return id.prod_Id;
    }

    public int getCount() {
        return count;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
