import java.util.*;

/**
 * UseCase6RoomAllocationService
 *
 * Demonstrates safe room allocation using Queue, HashMap, and Set.
 * Prevents double-booking and ensures inventory consistency.
 *
 * @author YourName
 * @version 6.0
 */

// -------------------- RESERVATION --------------------

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- BOOKING QUEUE --------------------

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory Status ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocations = new HashMap<>();
    private int roomCounter = 1;

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        System.out.println("\n--- Processing Booking Requests ---\n");

        while (!queue.isEmpty()) {

            Reservation req = queue.getNextRequest();
            String type = req.getRoomType();

            System.out.println("Processing request for " + req.getGuestName());

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId;
                do {
                    roomId = type.replace(" ", "").substring(0, 2).toUpperCase() + roomCounter++;
                } while (allocatedRoomIds.contains(roomId));

                // Store unique ID
                allocatedRoomIds.add(roomId);

                // Map room type to allocated IDs
                roomAllocations
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.decrement(type);

                // Confirm reservation
                System.out.println("Booking CONFIRMED for " + req.getGuestName()
                        + " | Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + req.getGuestName()
                        + " (No rooms available)");
            }

            System.out.println();
        }
    }

    // Display allocation summary
    public void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---\n");
        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// -------------------- MAIN CLASS --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v6.0");
        System.out.println("=====================================");

        // Initialize components
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Akash", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room"));
        queue.addRequest(new Reservation("Rahul", "Single Room")); // should fail
        queue.addRequest(new Reservation("Meena", "Suite Room"));

        // Process bookings
        service.processBookings(queue, inventory);

        // Display results
        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll bookings processed safely.");
    }
}