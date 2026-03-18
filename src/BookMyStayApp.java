import java.util.LinkedList;
import java.util.Queue;
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO) ---\n");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Get next request (peek only, no removal)
    public Reservation peekNext() {
        return queue.peek();
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v5.0");
        System.out.println("=====================================");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Akash", "Single Room"));
        bookingQueue.addRequest(new Reservation("Priya", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Rahul", "Double Room"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();

        // Show next request (without removing)
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNext();
        if (next != null) {
            next.display();
        }

        System.out.println("\nRequests are queued. No allocation done yet.");
    }
}

