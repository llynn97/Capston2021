package com.example.sassydesign;

import android.widget.Spinner;

public class ItemDetail {
    String productName;
    String productCost;
    String productCategory;
    String productQuantity;
    String objectId;

    Spinner category;
    int position;

    public ItemDetail(String productName, String productCost, String productQuantity, String productCategory, String objectId) {
        this.productName = productName;
        this.productCost = productCost;
        this.productQuantity = productQuantity;
        this.productCategory = productCategory;
        this.objectId = objectId;
    }
    public ItemDetail(String objectId) {
        this.objectId = objectId;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public Spinner getCategory() {
        return category;
    }

    public void setCategory(Spinner category) {
        this.category = category;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
