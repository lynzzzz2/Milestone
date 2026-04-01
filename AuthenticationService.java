package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {

    private final Repository repo; // no "= new Repository()" here

    public AuthenticationService(Repository repo) { // receive it instead
        this.repo = repo;
    }

    public void registerUser(String name, String email, String password, String role) {
        repo.insertUser(name, email, password, role);
    }

    public void loginUser(String email, String password) {
        ResultSet rs = repo.getUserByEmail(email);

        try {
            if (rs != null && rs.next()) {
                String storedPassword = rs.getString("password");
                String userName = rs.getString("name");
                String role = rs.getString("role");

                if (storedPassword.equals(password)) {
                    System.out.println("Login successful!");
                    System.out.println("Welcome, " + userName + " (" + role + ")");
                } else {
                    System.out.println("Incorrect password.");
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }
}
