package org.example;

public class Booking {

    private final int bookingId;
    private final int userId;
    private final int roomId;
    private final String date;
    private final String time;
    private final String status; // add this

    public Booking(int bookingId, int userId, int roomId, String date, String time, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.roomId = roomId;
        this.date = date;
        this.time = time;
        this.status = status; // add this
    }

    public int getBookingId() { return bookingId; }
    public int getUserId() { return userId; }
    public int getRoomId() { return roomId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; } // add this

    public void display() {
        System.out.println("Booking ID : " + bookingId);
        System.out.println("User ID    : " + userId);
        System.out.println("Room ID    : " + roomId);
        System.out.println("Date       : " + date);
        System.out.println("Time       : " + time);
        System.out.println("Status     : " + status); // add this
        System.out.println("---------------------------");
    }
}
