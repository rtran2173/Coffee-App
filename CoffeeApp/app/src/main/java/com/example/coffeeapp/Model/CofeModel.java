package com.example.coffeeapp.Model;

import com.google.firebase.firestore.DocumentId;

import java.lang.annotation.Documented;

public class CofeModel {

    @DocumentId
    String coffeeid;
    String coffeesize;
    int  quantity;
    double coffeeprice;

    public CofeModel() {
    }

    public CofeModel(String coffeeid, String coffeesize, double coffeeprice, int quantity) {
        this.coffeeid = coffeeid;
        this.coffeesize = coffeesize;
        this.coffeeprice = coffeeprice;
        this.quantity = quantity;
    }

    public String getCoffeeid() {
        return coffeeid;
    }

    public void setCoffeeid(String coffeeid) {
        this.coffeeid = coffeeid;
    }

    public String getCoffeesize() {
        return coffeesize;
    }

    public void setCoffeesize(String coffeesize) {
        this.coffeesize = coffeesize;
    }

    public double getCoffeeprice() {
        return coffeeprice;
    }

    public void setCoffeeprice(double coffeeprice) {
        this.coffeeprice = coffeeprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CofeModel{" +
                "coffeeid='" + coffeeid + '\'' +
                ", coffeesize='" + coffeesize + '\'' +
                ", coffeeprice=" + coffeeprice +
                ", quantity=" + quantity +
                '}';
    }
}