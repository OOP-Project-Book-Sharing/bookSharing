package com.example.first_draft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    public static List<Book> loadBooks(String filePath) throws IOException{

        List<Book> books = new ArrayList<>();
        File file = new File("Database/"+filePath);
        if(!file.exists()) return books;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while((line = reader.readLine())!= null){
            String[] parts = line.split("\\|");
            if(parts.length==7){
                String title = parts[0];
                String author = parts[1];
                String description = parts[2];
                String imagePath = parts[3];
                int buyAmount = Integer.parseInt(parts[4]);
                int rentAmount = Integer.parseInt(parts[5]);
                boolean isAvailable = Boolean.parseBoolean(parts[6]);

                Book book = new Book(title, author, description, imagePath, buyAmount, rentAmount, isAvailable);
                books.add(book);
            }
        }
        return books;
    }
}
