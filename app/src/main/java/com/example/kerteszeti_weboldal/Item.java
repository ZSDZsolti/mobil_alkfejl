package com.example.kerteszeti_weboldal;

public class Item {
    private final String name;
    private final int price;
    private final String description;
    private int quantity;
    private boolean inTheCart;
    private int quantityInTheCart;

    public Item(String name, int price, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        inTheCart = false;
        quantityInTheCart = 0;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isInTheCart() {
        return inTheCart;
    }

    public int getQuantityInTheCart() {
        return quantityInTheCart;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setInTheCart(boolean inTheCart) {
        this.inTheCart = inTheCart;
    }

    public void setQuantityInTheCart(int quantityInTheCart){
        this.quantityInTheCart = quantityInTheCart;
    }
}
