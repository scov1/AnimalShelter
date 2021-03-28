package org.seda.animalshelter.Models;

public class Category {

    private String CategoryIcon;
    private String CategoryName;

    public Category(String categoryIcon, String categoryName) {
        CategoryIcon = categoryIcon;
        CategoryName = categoryName;
    }

    public String getCategoryIcon() {
        return CategoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        CategoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
