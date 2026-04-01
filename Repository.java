package org.example;

import java.sql.*;

public class Repository {

    // Path to your SQLite database file
    private static final String DB_URL = "jdbc:sqlite:C:/Users/QC SDO/IdeaProjects/M1 Database/MILESTONE 1.db";
    private Connection connection;

    // ─────────────────────────────────────────
    // CONNECTION METHODS
    // ─────────────────────────────────────────

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public void seedRooms() {
        String check = "SELECT COUNT(*) FROM rooms";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(check)) {
            if (rs.next() && rs.getInt(1) == 0) {

                // Hot Desks
                insertRoom("Hot Desk A", "Hot Desk", 1, "available");
                insertRoom("Hot Desk B", "Hot Desk", 1, "available");
                insertRoom("Hot Desk C", "Hot Desk", 1, "available");

                // Meeting Rooms
                insertRoom("Meeting Room 1", "Meeting Room", 6, "available");
                insertRoom("Meeting Room 2", "Meeting Room", 10, "available");
                insertRoom("Meeting Room 3", "Meeting Room", 4, "available");

                // Private Offices
                insertRoom("Private Office 1", "Private Office", 2, "available");
                insertRoom("Private Office 2", "Private Office", 4, "available");
                insertRoom("Private Office 3", "Private Office", 6, "available");

                // Event/Training Rooms
                insertRoom("Training Room A", "Training Room", 20, "available");
                insertRoom("Event Hall", "Event Hall", 50, "available");

                System.out.println("Sample rooms added.");
            }
        } catch (SQLException e) {
            System.out.println("Error seeding rooms: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // CREATE TABLES
    // ─────────────────────────────────────────

    public void createTables() {
        String createUserTable = """
                CREATE TABLE IF NOT EXISTS users (
                    userId    INTEGER PRIMARY KEY AUTOINCREMENT,
                    name      TEXT NOT NULL,
                    email     TEXT NOT NULL UNIQUE,
                    password  TEXT NOT NULL,
                    role      TEXT NOT NULL DEFAULT 'customer'
                );
                """;

        String createRoomTable = """
                CREATE TABLE IF NOT EXISTS rooms (
                    roomId       INTEGER PRIMARY KEY AUTOINCREMENT,
                    roomName     TEXT NOT NULL,
                    roomType     TEXT NOT NULL,
                    capacity     INTEGER NOT NULL,
                    availability TEXT NOT NULL DEFAULT 'available'
                );
                """;

        String createBookingTable = """
                CREATE TABLE IF NOT EXISTS bookings (
                    bookingId INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId    INTEGER NOT NULL,
                    roomId    INTEGER NOT NULL,
                    date      TEXT NOT NULL,
                    time      TEXT NOT NULL,
                    status    TEXT NOT NULL DEFAULT 'active',
                    FOREIGN KEY (userId) REFERENCES users(userId),
                    FOREIGN KEY (roomId) REFERENCES rooms(roomId)
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createRoomTable);
            stmt.execute(createBookingTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // USER METHODS (used by Mintac)
    // ─────────────────────────────────────────

    public void insertUser(String name, String email, String password, String role) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.executeUpdate();
            System.out.println("User registered successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    public ResultSet getUsers() {
        String sql = "SELECT * FROM users";
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, email);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
            return null;
        }
    }

    // ─────────────────────────────────────────
    // ROOM METHODS (used by Sabang)
    // ─────────────────────────────────────────

    public void insertRoom(String roomName, String roomType, int capacity, String availability) {
        String sql = "INSERT INTO rooms (roomName, roomType, capacity, availability) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roomName);
            pstmt.setString(2, roomType);
            pstmt.setInt(3, capacity);
            pstmt.setString(4, availability);
            pstmt.executeUpdate();
            System.out.println("Room added successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting room: " + e.getMessage());
        }
    }

    public ResultSet getAvailableRooms() {
        String sql = "SELECT * FROM rooms WHERE availability = 'available'";
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving rooms: " + e.getMessage());
            return null;
        }
    }

    public void updateRoomAvailability(int roomId, String availability) {
        String sql = "UPDATE rooms SET availability = ? WHERE roomId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, availability);
            pstmt.setInt(2, roomId);
            pstmt.executeUpdate();
            System.out.println("Room availability updated.");
        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // BOOKING METHODS (used by Cabalona)
    // ─────────────────────────────────────────

    public void insertBooking(int userId, int roomId, String date, String time) {
        String sql = "INSERT INTO bookings (userId, roomId, date, time, status) VALUES (?, ?, ?, ?, 'active')";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, roomId);
            pstmt.setString(3, date);
            pstmt.setString(4, time);
            pstmt.executeUpdate();
            System.out.println("Booking created successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting booking: " + e.getMessage());
        }
    }

    public void cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'cancelled' WHERE bookingId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
            System.out.println("Booking cancelled successfully.");
        } catch (SQLException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }

    public ResultSet getBookingsByUser(int userId) {
        String sql = "SELECT * FROM bookings WHERE userId = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving user bookings: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getAllBookings() {
        String sql = "SELECT * FROM bookings";
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving all bookings: " + e.getMessage());
            return null;
        }
    }
}
