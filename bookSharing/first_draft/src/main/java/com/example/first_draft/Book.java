package com.example.first_draft;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String description;
    private String imagePath;
    private int buyAmount;
    private int rentAmount;
    private boolean isAvailable;
    private String owner;
    private String rentedTo;
    private String dueDate;
    private String genre;

    public Book() {}

    public Book(String title, String author, String description, String imagePath,
                int buyAmount, int rentAmount, boolean isAvailable,
                String owner, String rentedTo, String dueDate, String genre) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.imagePath = imagePath;
        this.buyAmount = buyAmount;
        this.rentAmount = rentAmount;
        this.isAvailable = isAvailable;
        this.owner = owner;
        this.rentedTo = rentedTo;
        this.dueDate = dueDate;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public int getBuyAmount() { return buyAmount; }
    public int getRentAmount() { return rentAmount; }
    public boolean isAvailable() { return isAvailable; }
    public String getOwner() { return owner; }
    public String getRentedTo() { return rentedTo; }
    public String getDueDate() { return dueDate; }
    public String getGenre() { return genre; }


    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setDescription(String description) { this.description = description; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setBuyAmount(int buyAmount) { this.buyAmount = buyAmount; }
    public void setRentAmount(int rentAmount) { this.rentAmount = rentAmount; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setRentedTo(String rentedTo) { this.rentedTo = rentedTo; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setGenre(String genre) { this.genre = genre; }


    public ImageView getCover() {
        ImageView img = new ImageView();
        img.setFitWidth(100);
        img.setFitHeight(150);
        img.setPreserveRatio(true);

        if (imagePath == null || imagePath.isEmpty()) return img;

        try {
            File file = new File(imagePath);
            if (file.exists()) {
                // Load from absolute file path
                img.setImage(new Image(file.toURI().toString()));
            } else {
                // Load from project resources
                img.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Could not load image: " + imagePath);
        }
        return img;
    }
}
