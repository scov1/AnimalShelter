package org.seda.animalshelter.Models;

import java.util.ArrayList;

public class Wishlist {
    private String productId;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private ArrayList<String> tags;

    public Wishlist(String productId,String productImage, String productTitle, String productPrice) {
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
     //   this.COD = cod;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    //    public boolean isCOD() {
//        return COD;
//    }
//
//    public void setCOD(boolean COD) {
//        this.COD = COD;
//    }


    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
