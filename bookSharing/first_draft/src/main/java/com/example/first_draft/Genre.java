package com.example.first_draft;

import java.util.List;

public class Genre{
    private String name;
    private List<Book> books;
    Genre(String name, List<Book> books){
        this.name = name;
        this.books= books;
    }
    public String getName(){
        return name;
    }
    public List<Book> getBooks(){
        return books;
    }
}
