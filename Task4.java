/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

import java.io.*;
import java.util.*;

// Room class
class Room implements Serializable {
    int roomNumber;
    String category;
    boolean isAvailable = true;

    public Room(int number, String category) {
        this.roomNumber = number;
        this.category = category;
    }

    public String toString() {
        return "Room " + roomNumber + " (" + category + ") - " + (isAvailable ? "Available" : "Booked");
    }
}

// Booking class
class Booking implements Serializable {
    String guestName;
    Room room;
    Date bookingDate;
    String paymentStatus;

    public Booking(String guestName, Room room, String paymentStatus) {
        this.guestName = guestName;
        this.room = room;
        this.bookingDate = new Date();
        this.paymentStatus = paymentStatus;
    }

    public String toString() {
        return "Booking for " + guestName + " | Room " + room.roomNumber + " (" + room.category + ") | Payment: " + paymentStatus + " | Date: " + bookingDate;
    }
}

// Hotel class
class Hotel {
    List<Room> rooms = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();

    // Initialize hotel rooms
    public Hotel() {
        // Add rooms by category
        for (int i = 1; i <= 5; i++) rooms.add(new Room(i, "Standard"));
        for (int i = 6; i <= 10; i++) rooms.add(new Room(i, "Deluxe"));
        for (int i = 11; i <= 15; i++) rooms.add(new Room(i, "Suite"));
        loadBookings();
    }

    // Search available rooms
    public List<Room> searchRooms(String category) {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && r.isAvailable) {
                available.add(r);
            }
        }
        return available;
    }

    // Book a room
    public Booking bookRoom(String guestName, String category) {
        List<Room> available = searchRooms(category);
        if (available.isEmpty()) {
            System.out.println("No rooms available in " + category + " category.");
            return null;
        }

        Room selected = available.get(0);
        selected.isAvailable = false;

        // Simulate payment
        System.out.println("Processing payment...");
        String paymentStatus = "Paid";

        Booking booking = new Booking(guestName, selected, paymentStatus);
        bookings.add(booking);
        saveBookings();
        System.out.println("Booking successful: " + booking);
        return booking;
    }

    // Cancel a booking
    public boolean cancelBooking(String guestName, int roomNumber) {
        for (Booking b : bookings) {
            if (b.guestName.equalsIgnoreCase(guestName) && b.room.roomNumber == roomNumber) {
                b.room.isAvailable = true;
                bookings.remove(b);
                saveBookings();
                System.out.println("Booking cancelled for " + guestName);
                return true;
            }
        }
        System.out.println("No booking found for given details.");
        return false;
    }

    // View all bookings
    public void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    // Save bookings to file
    public void saveBookings() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("bookings.dat"))) {
            out.writeObject(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load bookings from file
    @SuppressWarnings("unchecked")
    public void loadBookings() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("bookings.dat"))) {
            bookings = (List<Booking>) in.readObject();
            // Update room availability
            for (Booking b : bookings) {
                for (Room r : rooms) {
                    if (r.roomNumber == b.room.roomNumber) {
                        r.isAvailable = false;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // File may not exist at first
        }
    }

    public void listAllRooms() {
        for (Room r : rooms) {
            System.out.println(r);
        }
    }
}

// Main class
public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = new Hotel();

        while (true) {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. View All Rooms");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter room category (Standard/Deluxe/Suite): ");
                    String category = sc.nextLine();
                    List<Room> available = hotel.searchRooms(category);
                    if (available.isEmpty()) {
                        System.out.println("No available rooms in this category.");
                    } else {
                        for (Room r : available) System.out.println(r);
                    }
                }
                case 2 -> {
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter room category (Standard/Deluxe/Suite): ");
                    String category = sc.nextLine();
                    hotel.bookRoom(name, category);
                }
                case 3 -> {
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter room number: ");
                    int roomNumber = sc.nextInt();
                    hotel.cancelBooking(name, roomNumber);
                }
                case 4 -> hotel.viewBookings();
                case 5 -> hotel.listAllRooms();
                case 0 -> {
                    System.out.println("Thank you for using the system.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}