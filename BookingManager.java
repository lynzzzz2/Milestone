package org.example;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BookingManager {

    private final Scanner scanner = new Scanner(System.in);
    private final Repository repo = new Repository();

    public void bookWorkspace() {
        System.out.println("\n===== BOOK WORKSPACE =====");

        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();

        System.out.print("Enter Room ID: ");
        int roomId = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        System.out.print("Enter Date (e.g. 2025-04-01): ");
        String date = scanner.nextLine();

        System.out.print("Enter Time (e.g. 09:00): ");
        String time = scanner.nextLine();

        repo.insertBooking(userId, roomId, date, time); // matches your Repository
        System.out.println("Workspace booked successfully!");
    }

    public void cancelBooking() {
        System.out.println("\n===== CANCEL BOOKING =====");

        System.out.print("Enter Booking ID: ");
        int bookingId = scanner.nextInt();

        repo.cancelBooking(bookingId); // correct method name from Repository
    }

    public void viewAllBookings() {
        System.out.println("\n===== ALL BOOKINGS =====");

        ResultSet rs = repo.getAllBookings(); // correct method name
        try {
            boolean hasBookings = false;
            while (rs != null && rs.next()) {
                hasBookings = true;
                System.out.println("Booking ID : " + rs.getInt("bookingId"));
                System.out.println("User ID    : " + rs.getInt("userId"));
                System.out.println("Room ID    : " + rs.getInt("roomId"));
                System.out.println("Date       : " + rs.getString("date"));
                System.out.println("Time       : " + rs.getString("time"));
                System.out.println("Status     : " + rs.getString("status"));
                System.out.println("---------------------------");
            }
            if (!hasBookings) System.out.println("No bookings available.");
        } catch (SQLException e) {
            System.out.println("Error displaying bookings: " + e.getMessage());
        }
    }
}