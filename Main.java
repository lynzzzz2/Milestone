package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Repository repo = new Repository();
    private static final BookingManager bookingManager = new BookingManager(repo);
    private static final AuthenticationService authService = new AuthenticationService(repo);
    private static int loggedInUserId = -1;

    private static final String ADMIN_PASSWORD = "admin123"; // change this to whatever you want

    public static void main(String[] args) {
        repo.connect();
        repo.createTables();
        repo.seedRooms();

        int choice;

        do {
            System.out.println("\n===== COLAB HUB COWORKING SYSTEM =====");
            System.out.println("1. I am a User");
            System.out.println("2. I am an Admin");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> userMenu();
                case 2 -> adminLogin();
                case 0 -> System.out.println("Exiting system. Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        repo.close();
    }

    // ─────────────────────────────────────────
    // USER MENU
    // ─────────────────────────────────────────

    private static void userMenu() {
        int choice;

        do {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Available Rooms");
            System.out.println("4. Book Workspace");
            System.out.println("5. Cancel Booking");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: "); String name = scanner.nextLine();
                    System.out.print("Email: "); String email = scanner.nextLine();
                    System.out.print("Password: "); String password = scanner.nextLine();
                    authService.registerUser(name, email, password, "customer");
                }
                case 2 -> {
                    System.out.print("Email: "); String email = scanner.nextLine();
                    System.out.print("Password: "); String password = scanner.nextLine();
                    int result = authService.loginUser(email, password);
                    if (result != -1) {
                        loggedInUserId = result; // save the userId after login
                    }
                }
                case 3 -> displayAvailableRooms();
                case 4 -> {
                    if (loggedInUserId == -1) {
                        System.out.println("Please login first before booking.");
                    } else {
                        bookingManager.bookWorkspace(loggedInUserId); // pass it in
                    }
                }                case 5 -> bookingManager.cancelBooking();
                case 0 -> {
                    loggedInUserId = -1; // clear on logout
                    System.out.println("Returning to main menu...");
                }                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    // ─────────────────────────────────────────
    // ADMIN LOGIN
    // ─────────────────────────────────────────

    private static void adminLogin() {
        System.out.print("\nEnter Admin Password: ");
        String input = scanner.nextLine();

        if (input.equals(ADMIN_PASSWORD)) {
            System.out.println("Access granted. Welcome, Admin!");
            adminMenu();
        } else {
            System.out.println("Incorrect password. Access denied.");
        }
    }

    // ─────────────────────────────────────────
    // ADMIN MENU
    // ─────────────────────────────────────────

    private static void adminMenu() {
        int choice;

        do {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Bookings");
            System.out.println("2. View All Users");
            System.out.println("3. View Available Rooms");
            System.out.println("4. Add Room");
            System.out.println("5. Cancel Any Booking");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> bookingManager.viewAllBookings();
                case 2 -> displayAllUsers();
                case 3 -> displayAvailableRooms();
                case 4 -> {
                    System.out.print("Room Name: "); String roomName = scanner.nextLine();
                    System.out.print("Room Type: "); String roomType = scanner.nextLine();
                    System.out.print("Capacity: "); int capacity = scanner.nextInt(); scanner.nextLine();
                    repo.insertRoom(roomName, roomType, capacity, "available");
                }
                case 5 -> bookingManager.cancelBooking();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    // ─────────────────────────────────────────
    // DISPLAY HELPERS
    // ─────────────────────────────────────────

    private static void displayAvailableRooms() {
        System.out.println("\n===== AVAILABLE ROOMS =====");
        ResultSet rs = repo.getAvailableRooms();
        try {
            boolean found = false;
            while (rs != null && rs.next()) {
                found = true;
                new Room(
                        rs.getInt("roomId"),
                        rs.getString("roomName"),
                        rs.getString("roomType"),
                        rs.getInt("capacity"),
                        rs.getString("availability")
                ).display();
            }
            if (!found) System.out.println("No available rooms.");
        } catch (SQLException e) {
            System.out.println("Error displaying rooms: " + e.getMessage());
        }
    }

    private static void displayAllUsers() {
        System.out.println("\n===== ALL USERS =====");
        ResultSet rs = repo.getUsers();
        try {
            boolean found = false;
            while (rs != null && rs.next()) {
                found = true;
                System.out.println("User ID : " + rs.getInt("userId"));
                System.out.println("Name    : " + rs.getString("name"));
                System.out.println("Email   : " + rs.getString("email"));
                System.out.println("Role    : " + rs.getString("role"));
                System.out.println("---------------------------");
            }
            if (!found) System.out.println("No users found.");
        } catch (SQLException e) {
            System.out.println("Error displaying users: " + e.getMessage());
        }
    }
}
