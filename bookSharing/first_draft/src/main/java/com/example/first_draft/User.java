package com.example.first_draft;

import java.io.Serial;
import java.io.Serializable;

public class  User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String location;

    public User() {}

    public User(String username, String password, String email, String phone, String location) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.location = location;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setLocation(String location) { this.location = location; }
}
