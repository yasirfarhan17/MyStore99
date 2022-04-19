package com.noor.mystore99;

public class lastData {
    public String name;
    public String price;
    public String quant;
    public String weight;

    public lastData(){

    }

    public lastData(String name,String price, String quant,String weight) {
        this.name=name;
        this.price = price;
        this.quant = quant;
        this.weight=weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }
}
