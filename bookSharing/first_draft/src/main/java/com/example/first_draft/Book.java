package com.example.first_draft;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Book {
    private String title;
    private String author;
    private String description;
    private String imagePath;
    private int buyAmount;
    private int rentAmount;
    private boolean isAvailable;
    private double rating;
    private ImageView cover;


    public Book(String title, String author, String description, String imagePath, int buyAmount, int rentAmount, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.imagePath = imagePath;
        this.buyAmount = buyAmount;
        this.rentAmount = rentAmount;
        this.isAvailable = true;
        this.cover= new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getRentAmount() {
        return rentAmount;
    }

    public int getBuyAmount() {
        return buyAmount;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public ImageView getCover() {
        return cover;
    }

}
