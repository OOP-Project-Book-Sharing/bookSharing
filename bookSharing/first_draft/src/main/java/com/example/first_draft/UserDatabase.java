package com.example.first_draft;

import java.io.*;
import java.util.*;

public class UserDatabase {
    private static final String FILE_PATH = "users.dat";
    private List<User> users;

    public UserDatabase() {
        users = new ArrayList<>();
        try {
            load();
        } catch (FileNotFoundException e) {
            System.err.println("User database file not found. Creating a new one...");
            save();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error loading user database: " + e.getMessage());
        }
    }

    public void load() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new FileNotFoundException("User database file not found: " + FILE_PATH);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = (List<User>) ois.readObject();
        }
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving user database: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        users.add(user);
        save();
    }

    public boolean validateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean userExists(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return true;
        }
        return false;
    }

    public boolean emailExists(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserByUsername(String username) {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (User u : users) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public List<String> getAllUsernames() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> usernames = new ArrayList<>();
        for (User u : users) {
            usernames.add(u.getUsername());
        }
        return usernames;
    }
}
