package org.example;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Repository repo = new Repository();
    private static final BookingManager bookingManager = new BookingManager();
    private static final AuthenticationService authService = new AuthenticationService();

    public static void main(String[] args) {
        repo.connect();
        repo.createTables();

        int choice;

        do {
            System.out.println("\n===== COLAB HUB COWORKING SYSTEM =====");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. View Available Rooms");
            System.out.println("4. Book Workspace");
            System.out.println("5. Cancel Booking");
            System.out.println("6. View All Bookings (Admin)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: "); String name = scanner.nextLine();
                    System.out.print("Email: "); String email = scanner.nextLine();
                    System.out.print("Password: "); String password = scanner.nextLine();
                    System.out.print("Role (customer/admin): "); String role = scanner.nextLine();
                    authService.registerUser(name, email, password, role);
                }
                case 2 -> {
                    System.out.print("Email: "); String email = scanner.nextLine();
                    System.out.print("Password: "); String password = scanner.nextLine();
                    authService.loginUser(email, password);
                }
                case 3 -> displayAvailableRooms();
                case 4 -> bookingManager.bookWorkspace();
                case 5 -> bookingManager.cancelBooking();
                case 6 -> bookingManager.viewAllBookings();
                case 0 -> System.out.println("Exiting system...");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        repo.close();
    }

    private static void displayAvailableRooms() {
        System.out.println("\n===== AVAILABLE ROOMS =====");
        ResultSet rs = repo.getAvailableRooms(); // correct method from Repository
        try {
            boolean found = false;
            while (rs != null && rs.next()) {
                found = true;
                System.out.println("Room ID    : " + rs.getInt("roomId"));
                System.out.println("Room Name  : " + rs.getString("roomName"));
                System.out.println("Type       : " + rs.getString("roomType"));
                System.out.println("Capacity   : " + rs.getInt("capacity"));
                System.out.println("Status     : " + rs.getString("availability"));
                System.out.println("---------------------------");
            }
            if (!found) System.out.println("No available rooms.");
        } catch (SQLException e) {
            System.out.println("Error displaying rooms: " + e.getMessage());
        }
    }
}