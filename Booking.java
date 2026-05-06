package org.example;

public class Booking {

    private final int BOOKING_ID;
    private final int USER_ID;
    private final int ROOM_ID;
    private final String DATE;
    private final String TIME;
    private final int DURATION;
    private final String STATUS;

    public Booking(int bookingId, int userId, int roomId, String date, String time, int duration, String status) {
        this.BOOKING_ID = bookingId;
        this.USER_ID = userId;
        this.ROOM_ID = roomId;
        this.DATE = date;
        this.TIME = time;
        this.DURATION = duration;
        this.STATUS = status;
    }

    public int getBookingId()  { return BOOKING_ID; }
    public int getUserId()     { return USER_ID; }
    public int getRoomId()     { return ROOM_ID; }
    public String getDate()    { return DATE; }
    public String getTime()    { return TIME; }
    public int getDuration()   { return DURATION; }
    public String getStatus()  { return STATUS; }

    public void display() {
        int endMins = timeToMinutes(TIME) + DURATION;
        String endTime = String.format("%02d:%02d", endMins / 60, endMins % 60);

        System.out.println("Booking ID : " + BOOKING_ID);
        System.out.println("User ID    : " + USER_ID);
        System.out.println("Room ID    : " + ROOM_ID);
        System.out.println("Date       : " + DATE);
        System.out.println("Start Time : " + TIME);
        System.out.println("End Time   : " + endTime);
        System.out.println("Duration   : " + DURATION + " minutes");
        System.out.println("Status     : " + STATUS);
        System.out.println("---------------------------");
    }

    private int timeToMinutes(String time) {
        try {
            String[] parts = time.trim().split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
        }
    }
}