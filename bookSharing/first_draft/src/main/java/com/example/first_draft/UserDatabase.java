package com.example.first_draft;

import java.io.*;
import java.util.*;

public class UserDatabase {
    private static final String FILE_PATH = "users.dat";
    private List<User> users;

    public UserDatabase() {
        users = new ArrayList<>();
        load();
    }

    public void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = (List<User>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading user database: " + e.getMessage());
        }
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (Exception e) {
            System.out.println("Error saving user database: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        users.add(user);
        save();
    }

    public boolean validateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) return true;
        }
        return false;
    }

    public boolean userExists(String username) {
        for (User u : users) if (u.getUsername().equals(username)) return true;
        return false;
    }
}
