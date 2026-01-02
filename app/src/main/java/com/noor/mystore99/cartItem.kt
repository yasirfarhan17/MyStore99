package com.noor.mystore99;

public class cartItem {
    private String name,price,weight;
    int quant,total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public cartItem(){

    }

    public cartItem(String name, String price,int total) {
        this.name = name;
        this.price = price;
        this.total=total;



    }

    public cartItem(String name, String price, int quant,int total,String weight) {
        this.name = name;
        this.price = price;
        this.quant=quant;
        this.total=total;
        this.weight=weight;


    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
