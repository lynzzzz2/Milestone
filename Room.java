package org.example;

public class Room {

    private final int ROOM_ID;
    private final String ROOM_NAME;
    private final String ROOM_TYPE;
    private final int CAPACITY;
    private final String AVAILABILITY;

    public Room(int roomId, String roomName, String roomType, int capacity, String availability) {
        this.ROOM_ID = roomId;
        this.ROOM_NAME = roomName;
        this.ROOM_TYPE = roomType;
        this.CAPACITY = capacity;
        this.AVAILABILITY = availability;
    }

    // Getters
    public int getRoomId() {
        return ROOM_ID;
    }
    public String getRoomName() {
        return ROOM_NAME;
    }
    public String getRoomType() {
        return ROOM_TYPE;
    }
    public int getCapacity() {
        return CAPACITY;
    }
    public String getAvailability() {
        return AVAILABILITY;
    }

    // Optional display (NOT required but useful)
    public void display() {
        System.out.println("Room ID    : " + ROOM_ID);
        System.out.println("Room Name  : " + ROOM_NAME);
        System.out.println("Type       : " + ROOM_TYPE);
        System.out.println("Capacity   : " + CAPACITY);
        System.out.println("Status     : " + AVAILABILITY);
        System.out.println("---------------------------");
    }
}