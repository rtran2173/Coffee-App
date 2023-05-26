package com.example.coffeeapp.Model;

public class CartModel {

    String coffeesize;
    int quantity;

    double totalprice;

    public CartModel() {
    }

    public CartModel(String coffeesize, double totalprice, int quantity) {
        this.coffeesize = coffeesize;
        this.totalprice = totalprice;
        this.quantity = quantity;
    }

    public String getCoffeesize() {
        return coffeesize;
    }

    public void setCoffeesize(String coffeesize) {
        this.coffeesize = coffeesize;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
