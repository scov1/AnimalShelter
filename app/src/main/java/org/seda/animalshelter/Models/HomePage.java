package org.seda.animalshelter.Models;

import java.util.ArrayList;
import java.util.List;

public class HomePage {

    private int type;
    public static final int BANNER = 0;
    public static final int HORIZONTAL_PRODUCT = 1;
    public static final int GRID_ANIMALS = 2;
    public static final int ADOPT_PET = 3;
    private String resource;
    private String productName;
    private List<HorizontalProduct> productList;
    private List<Animals> animalsList;
    private List<Wishlist> viewAllProductList;

    public HomePage(int type, String resource) {
        this.type = type;
        this.resource = resource;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public HomePage(int type, String productName, List<HorizontalProduct> productList,List<Wishlist> viewAllProductList) {
        this.type = type;
        this.productName = productName;
        this.productList = productList;
        this.viewAllProductList=viewAllProductList;
    }

    public List<Wishlist> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<Wishlist> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }

    public HomePage(int type, String productName, List<HorizontalProduct> productList) {
        this.type = type;
        this.productName = productName;
        this.productList = productList;
    }

    public HomePage(int type, String resource, String productName, List<Animals> animalsList) {
        this.type = type;
        this.resource = resource;
        this.productName = productName;
        this.animalsList = animalsList;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<HorizontalProduct> getProductList() {
        return productList;
    }

    public List<Animals> getAnimalsList() {
        return animalsList;
    }

    public void setAnimalsList(List<Animals> animalsList) {
        this.animalsList = animalsList;
    }

    public void setProductList(List<HorizontalProduct> productList) {
        this.productList = productList;
    }
}


