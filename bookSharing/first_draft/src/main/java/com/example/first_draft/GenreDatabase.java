package com.example.first_draft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDatabase {
    private static final String FILE_PATH = "genres.txt";

    public static List<String> loadGenres() {
        List<String> genres = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) genres.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading genres.txt: " + e.getMessage());
        }
        return genres;
    }
}
