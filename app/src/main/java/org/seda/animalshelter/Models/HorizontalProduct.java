package org.seda.animalshelter.Models;

public class HorizontalProduct {

    private String productId;
    private String productImage;
    private String productName;
    private String productPrice;
    private String productDescription;


    public HorizontalProduct(String productId,String productImage, String productName, String productPrice, String productDescription) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }

    public HorizontalProduct(String productId, String productImage, String productName) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
