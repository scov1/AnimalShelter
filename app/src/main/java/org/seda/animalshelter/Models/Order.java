package org.seda.animalshelter.Models;

public class Order {

    private int productImage;
    private String productTitle;
    private String productDelivery;

    public Order(int productImage, String productTitle, String productDelivery) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productDelivery = productDelivery;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDelivery() {
        return productDelivery;
    }

    public void setProductDelivery(String productDelivery) {
        this.productDelivery = productDelivery;
    }
}
