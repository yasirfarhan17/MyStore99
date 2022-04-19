package com.noor.mystore99;

public class product {
    private String products_name;
    private String price;
    private String img;
    public String quant;
    private String HindiName;
    private String stock;


    public product() {
    }

    public product(String product_name, String price, String quant, String nameHindi, String stock) {
        this.products_name = product_name;
        this.price = price;
        this.img = img;
        this.quant=quant;
        this.HindiName=nameHindi;
        this.stock=stock;

    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getHindiName() {
        return HindiName;
    }

    public void setHindiName(String hindiName) {
        HindiName = hindiName;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String product_name) {
        this.products_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}


