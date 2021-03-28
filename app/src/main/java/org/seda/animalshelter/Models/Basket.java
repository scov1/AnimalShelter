package org.seda.animalshelter.Models;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    public static final int BASKET = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    ///basket item
    private String productId;
    private String productImage;
    private String productName;
    private String productPrice;
    private Long productAmount;
    private List<String> qtyId;

    public Basket(int type,String productId, String productImage, String productName, String productPrice, Long productAmount) {
        this.type = type;
        this.productId=productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
        qtyId = new ArrayList<>();
    }

    public List<String> getQtyId() {
        return qtyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Long getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(long productAmount) {
        this.productAmount = productAmount;
    }

    ///basket total

    private int totalItems,totalItemsPrice,totalAmount;

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public void setTotalItemsPrice(int totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Basket(int type) {
        this.type = type;
    }


    ///basket total


}
