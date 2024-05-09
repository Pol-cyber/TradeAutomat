package com.yarem.dbConnect.entity;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.swing.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Entity
public class Product {

    @Id
    protected String name;

    @Column(length = 20)
    @ColumnDefault(value = "'Опису немає'")
    protected String description;

    @Positive
    protected int count;

    protected int automatSlot;

    @Column(name = "image",nullable = false)
    protected byte[] byteImage;

    @ColumnDefault(value = "'false'")
    protected boolean useInAutomat;


    @Positive
    protected Double priceForOne;

    @ManyToOne
    protected Category category;

    public Product(String name, String description, int count, int automatSlot, byte[] image, boolean useInAutomat, Double priceForOne, Category category) {
        this.name = name;
        if(!description.equals("")){
            this.description = description;
        }
        this.count = count;
        this.automatSlot = automatSlot;
        this.byteImage = image;
        this.useInAutomat = useInAutomat;
        this.priceForOne = priceForOne;
        this.category = category;
    }

    public Product() {

    }


    public byte[] getByteImage() {
        return byteImage;
    }

    public ImageIcon getImageIcon(){
        return new ImageIcon(byteImage);
    }

    public void swapUseInAutomat(){
        useInAutomat = !useInAutomat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getDescription() {
        return description;
    }

    public Double getPriceForOne() {
        return priceForOne;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void setUseInAutomat(boolean useInAutomat) {
        this.useInAutomat = useInAutomat;
    }

    public String getName() {
        return name;
    }

    public int getAutomatSlot() {
        return automatSlot;
    }

    public boolean isUseInAutomat() {
        return useInAutomat;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAutomatSlot(int automatSlot) {
        this.automatSlot = automatSlot;
    }

    public void setPriceForOne(Double priceForOne) {
        this.priceForOne = priceForOne;
    }
}
