package com.guiltylogik.travelmanticsalc40.utils;

import java.io.Serializable;

public class TravelDeal implements Serializable {

    private String id;
    private String imageUrl;
    private String title;
    private String price;
    private String description;
    private String dealImageName;

    public TravelDeal(){}

    public TravelDeal(String imageUrl, String title, String price, String description, String dealImageName) {
        this.setId(id);
        this.setImageUrl(imageUrl);
        this.setTitle(title);
        this.setPrice(price);
        this.setDescription(description);
        this.setDealImageName(dealImageName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDealImageName() {
        return dealImageName;
    }

    public void setDealImageName(String dealImageName) {
        this.dealImageName = dealImageName;
    }
}
