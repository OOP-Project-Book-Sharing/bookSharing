package com.example.first_draft;

import java.io.*;
import java.util.*;

public class GenreDatabase {
    private static final String FILE_PATH = "database/genres.txt";

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

    public static void addGenre(String genre) {
        List<String> genres = loadGenres();
        if (!genres.contains(genre)) {
            //creates new file if there doesn't exist a file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                bw.write(genre);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
