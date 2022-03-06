package com.example.sassydesign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Item implements Serializable {

    String date;
    String title;
    String inOrOut;
    String cacheOrCard;

    int subCursor;

    ArrayList<String> itemList;
    ArrayList<String> priceList;
    ArrayList<String> categoryList;
    ArrayList<String> quantityList;
    ArrayList<String> objectidList;

    public Item(String date, String title, String inOrOut, String cacheOrCard,
                ArrayList<String> itemList, ArrayList<String>  categoryList,
                ArrayList<String>  quantityList, ArrayList<String>  priceList, ArrayList<String> objectidList){
        this.date = date;
        this.title = title;
        this.inOrOut = inOrOut;
        this.itemList = itemList;

        this.priceList = priceList;
        this.cacheOrCard = cacheOrCard;
        this.quantityList = quantityList;
        this.categoryList = categoryList;
        this.objectidList = objectidList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String getCacheOrCard() {
        return cacheOrCard;
    }

    public void setCacheOrCard(String cacheOrCard) {
        this.cacheOrCard = cacheOrCard;
    }


    public ArrayList<String> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<String> getPriceList() {
        return priceList;
    }

    public void setPriceList(ArrayList<String> priceList) {
        this.priceList = priceList;
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<String> getQuantityList() {
        return quantityList;
    }

    public void setQuantityList(ArrayList<String> quantityList) {
        this.quantityList = quantityList;
    }

    public ArrayList<String> getObjectidList() {
        return objectidList;
    }

    public void setObjectidList(ArrayList<String> objectidList) {
        this.objectidList = objectidList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSubCursor(int subCursor){
        this.subCursor = subCursor;
    }

    public int getSubCursor(){
        return subCursor;
    }
}